package org.echoice.ums.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.web.MediaTypes;
import org.echoice.modules.web.MsgTip;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.config.ConfigConstants;
import org.echoice.ums.dao.UmsClientDao;
import org.echoice.ums.domain.EcGroup;
import org.echoice.ums.domain.EcObjects;
import org.echoice.ums.domain.EcUser;
import org.echoice.ums.web.UmsAppBean;
import org.echoice.ums.web.UmsHolder;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SecurityPmFilter implements Filter{
	private static final Logger logger=LoggerFactory.getLogger(SecurityPmFilter.class);
	public static final String CONST_UMS_GROUP="CONST_UMS_GROUP";
	private static final Map<String,List<EcObjects>> securityObjectListMap=new HashMap<String, List<EcObjects>>();
	private Map<String,String> excludeSecurityMap=new HashMap<String, String>();
	private boolean isSecurityFilter=true;
	private String userSecurityObjectsSessionName="_USER_SECURITY_UMS_OBJECTS";
	private String accessModeAlias="view";
	private String urlTag="url";
	private String[] filterActionArr=null;
	
	private UmsClientDao umsClientDao;
	private ConfigBean configBean;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		logger.info("SecurityPmFilter init param start");
		String isSecurityFilterS=filterConfig.getInitParameter("isSecurityFilter");
		isSecurityFilter=Boolean.valueOf(isSecurityFilterS);
		accessModeAlias=filterConfig.getInitParameter("accessModeAlias");
		userSecurityObjectsSessionName=filterConfig.getInitParameter("userSecurityObjectsSessionName");
		urlTag=filterConfig.getInitParameter("urlTag");
		String filterActions=filterConfig.getInitParameter("filterActions");
		filterActionArr=StringUtils.splitByWholeSeparator(filterActions, "|");
		
		String excludeSecurityUrls=filterConfig.getInitParameter("excludeSecurityUrls");
		if(excludeSecurityUrls!=null&&!("".equals(excludeSecurityUrls))){
			String[] excludeSecurityUrlArr=StringUtils.splitByWholeSeparator(excludeSecurityUrls, "|");
			for (int i = 0; i < excludeSecurityUrlArr.length; i++) {
				excludeSecurityMap.put(excludeSecurityUrlArr[i], excludeSecurityUrlArr[i]);
			}
		}
		
		WebApplicationContext applicationContext=WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		umsClientDao=(UmsClientDao)applicationContext.getBean("umsClientDao");
		configBean=(ConfigBean)applicationContext.getBean("configBean");
		initSecurityObjectMap();
		logger.info("SecurityPmFilter init param end");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		try {
			HttpServletRequest httpReq=(HttpServletRequest)request;
			HttpServletResponse httpResp=(HttpServletResponse)response;
			
			//模拟数据
			//simulatedData(httpReq);
			//end
			String userName=getUserName(httpReq);
			
			if(StringUtils.isBlank(userName)) {
				//httpReq.getRequestDispatcher("/login").forward(request, response);
				String contextPath=httpReq.getContextPath();
				httpResp.sendRedirect(contextPath+"/login");
				return;
			}
			
			String initSecurityObjectMap=request.getParameter("_initSecurityObjectMap");
			if("true".equalsIgnoreCase(initSecurityObjectMap)){
				initSecurityObjectMap();
			}
			
			Boolean isAdmin=(Boolean)httpReq.getSession().getAttribute(ConfigConstants.IS_SUPER_ADMIN);
			
			//isAdmin=umsClientDao.checkHasPermission(userName, this.configBean.getAuthObject(), "super");
			
			if(isAdmin==null) {
				isAdmin=umsClientDao.checkHasPermission(userName, this.configBean.getAuthObject(), "super");
				httpReq.getSession().setAttribute(ConfigConstants.IS_SUPER_ADMIN,isAdmin);
			}
			
			boolean isPm=true;
			if(!isAdmin&&this.isSecurityFilter){
				
				//获取访问地址
				String urlPath=httpReq.getServletPath();
				logger.debug("req urlPath:{}",urlPath);
				
				String urlPathPrefix=StringUtils.substringBeforeLast(urlPath, "/");
				String action=StringUtils.substringAfterLast(urlPath, "/");
				logger.debug("deal urlPathPrefix:{}",urlPathPrefix);
				//查看是否受保护URL地址
				List<EcObjects> secObjectList=securityObjectListMap.get(urlPathPrefix);
				if(secObjectList!=null){
					isPm=false;
					//取用户的权限的资源对象，并放入session中
					Object userSecurityObj=httpReq.getSession().getAttribute(userSecurityObjectsSessionName);
					Map<String,EcObjects> userSecurityObjectMap=null;
					if(userSecurityObj==null){
						userSecurityObjectMap=new HashMap<String, EcObjects>();
						List<EcObjects> list=umsClientDao.findAssignPermissionObjectList(userName, this.accessModeAlias);
						if(list!=null&&list.size()>0){
							for (EcObjects ecObjects : list) {
								logger.debug("userSecurityObject:"+ecObjects.getAlias()+","+ecObjects.getName());
								userSecurityObjectMap.put(ecObjects.getAlias(), ecObjects);
							}
						}
						httpReq.getSession().setAttribute(userSecurityObjectsSessionName, userSecurityObjectMap);
					}else{
						userSecurityObjectMap=(Map<String,EcObjects>)userSecurityObj;
					}
					//比对资源权限
					EcObjects judgeObject=null;
					
					for (EcObjects secObject : secObjectList) {
						judgeObject=userSecurityObjectMap.get(secObject.getAlias());
						if(judgeObject!=null){
							isPm=true;
							break;
						}
					}
					
					if(!isPm){//对于rest返回做处理
						//对删除、修改、保存操作处理
						if(StringUtils.equalsIgnoreCase("post", httpReq.getMethod())) {
							for (String oneFilterAction : filterActionArr) {
								if(StringUtils.startsWithIgnoreCase(oneFilterAction, action)) {
									httpResp.setContentType(MediaTypes.JSON_UTF_8);
									MsgTip msgTip=new MsgTip();
									msgTip.setCode(403);
									msgTip.setMsg("权限不足，请联系管理员！");
									String respStr=JSON.toJSONString(msgTip);
									response.getWriter().write(respStr);
									return;
								}
							}
						}
					}
				}
				
			}
			
			UmsAppBean umsAppBean=new UmsAppBean();
	    	umsAppBean.setAdmin(isAdmin);
			umsAppBean.setUserAlias(userName);
			
			//获取用户组信息
			EcGroup userGroup=(EcGroup)httpReq.getSession(false).getAttribute(ConfigConstants.UMS_GROUP_SESSION);
			if(userGroup==null) {
				List<EcGroup> ecGroupList=umsClientDao.findGroupsByUserAlias(userName);
				if(ecGroupList!=null&&ecGroupList.size()>0) {
					userGroup=ecGroupList.get(0);
					httpReq.getSession(false).setAttribute(ConfigConstants.UMS_GROUP_SESSION, userGroup);
				}
			}
			
			umsAppBean.setEcGroup(userGroup);
			UmsHolder.setUmsAppBean(umsAppBean);
			
			chain.doFilter(request, response);
		}finally {
			UmsHolder.clear();
		}
	}
	
	/**
	 * 初始化系统保护资源对象
	 */
	public void initSecurityObjectMap(){
		securityObjectListMap.clear();
		List<EcObjects> menuList=umsClientDao.findObjectsByAccessModeAlias(accessModeAlias);
		JSONObject jsonObject=null;
		String url=null;
		int index=0;
		String exclueUrl=null;
		List<EcObjects> tmpList=null;
		for (EcObjects ecObjects : menuList) {
			logger.info(""+ecObjects.getAlias()+","+ecObjects.getName()+","+ecObjects.getNote());
			if(ecObjects.getNote()!=null&&!("".equals(ecObjects.getNote()))){
				try{
					jsonObject=JSON.parseObject(ecObjects.getNote());
					url=jsonObject.getString(urlTag);
					index=url.indexOf("?");
					if(index!=-1){
						url=url.substring(0,index);
					}
					
					if('/'!=url.charAt(0)&&!(StringUtils.startsWith(url,"http://"))&&!(StringUtils.startsWith(url,"https://"))){
						url="/"+url;
					}
					
					logger.info("before deal cfg urlPath:"+url);
					String urlPathPrefix=StringUtils.substringBeforeLast(url, "/");
					logger.info("after deal cfg urlPath:"+urlPathPrefix);
					
					//排除url
					exclueUrl=excludeSecurityMap.get(urlPathPrefix);
					if(exclueUrl==null){
						tmpList=securityObjectListMap.get(urlPathPrefix);
						if(tmpList==null){
							tmpList=new ArrayList<EcObjects>();
						}
						tmpList.add(ecObjects);
						securityObjectListMap.put(urlPathPrefix, tmpList);
					}
				}catch (Exception e) {
					// TODO: handle exception
					//logger.error("parseJson erro:",e);
				}
			}
		}
	}
	
    private String getUserName(HttpServletRequest request){
    	Object obj=request.getSession().getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION);
    	if (obj instanceof Assertion) {
			Assertion assertion = (Assertion) obj;
			return assertion.getPrincipal().getName();
		}else{
			return (String)obj;
		}
    }
	
	private void simulatedData(HttpServletRequest request) {
		//request.getSession().setAttribute(ConfigConstants.IS_SUPER_ADMIN, true);
		request.getSession().setAttribute(AuthenticationFilter.CONST_CAS_ASSERTION,"admin");
		//List<EcGroup> groupList=umsClientDao.findGroupsByUserAlias("test");
		//CasUmsUtil.setUserGroup(request, groupList.get(0));
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
