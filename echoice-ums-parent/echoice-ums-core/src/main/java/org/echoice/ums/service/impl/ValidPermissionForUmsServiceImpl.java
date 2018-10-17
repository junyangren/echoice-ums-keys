package org.echoice.ums.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.cas.CasUtil;
import org.echoice.modules.encrypt.MD5;
import org.echoice.modules.web.json.bean.ExtJsActionView;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.config.ConfigConstants;
import org.echoice.ums.dao.EcGroupDao;
import org.echoice.ums.dao.EcPermissionDao;
import org.echoice.ums.dao.EcUserDao;
import org.echoice.ums.dao.EcUsersAssignmenDao;
import org.echoice.ums.dao.UmsClientDao;
import org.echoice.ums.domain.EcObjects;
import org.echoice.ums.domain.EcUser;
import org.echoice.ums.service.ValidPermissionForUmsService;
import org.echoice.ums.util.CasUmsUtil;
import org.echoice.ums.web.view.UmsAccordionMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidPermissionForUmsServiceImpl implements ValidPermissionForUmsService {
	private Logger logger=LoggerFactory.getLogger(ValidPermissionForUmsService.class);
	private ConfigBean configBean; 
	private EcPermissionDao ecPermissionDao;
	private EcUsersAssignmenDao ecUsersAssignmenDao;	
	private EcUserDao ecUserDao;
	private UmsClientDao umsClientDao;
	private EcGroupDao ecGroupDao; 
	
	public ExtJsActionView auth(HttpServletRequest request) {
		// TODO Auto-generated method stub
		ExtJsActionView actionView=new ExtJsActionView(); 
		String userAlias=request.getParameter("alias");
		if(configBean.isAuth()){
			String password=request.getParameter("password");
			String authPassword=request.getParameter("authPassword");
			if(StringUtils.isBlank(userAlias)||StringUtils.isBlank(password)||StringUtils.isBlank(authPassword)){
				actionView.setCode(2001);
				actionView.addErrorCodeMsg("msg", "用户名、密码、验证码不能为空");
				return actionView;
			}
			
			//获取随机验证码
			String radomStrID=(String)request.getSession().getAttribute(CasUtil.VALIDATECODE_SESSION_NAME);
			if(StringUtils.isBlank(radomStrID)){
				actionView.setCode(2001);
				actionView.addErrorCodeMsg("msg", "用户名、密码、验证码不能为空");
				return actionView;
			}
			
			if(!(radomStrID.equalsIgnoreCase(authPassword))){
				actionView.setCode(2002);
				actionView.addErrorCodeMsg("msg", "对不起，验证码错误");
				return actionView;
			}
			
			//获取登录用户的信息
			List<EcUser> list=ecUserDao.findByAlias(userAlias);
			if(list==null||list.size()<=0){
				logger.info(userAlias+"：用户名错误");
				actionView.setCode(2003);
				actionView.addErrorCodeMsg("msg", "对不起，用户名错误");
				return actionView;
			}
			
			//密码验证
			EcUser ecUser=list.get(0);
			String passWordDb=ecUser.getPassword();
			MD5 md5=new MD5();
			String userPassWord=md5.getMD5ofStr(userAlias+password);
			if(!(passWordDb.equals(userPassWord))){
				logger.info(userAlias+"：用户密码错误");
				actionView.setCode(2004);
				actionView.addErrorCodeMsg("msg", "对不起，用户密码错误");
				return actionView;
			}
		}
		request.getSession().setAttribute(CasUtil.CONST_CAS_ASSERTION, userAlias);
		return actionView;
	}
	
	public void setUserPermission(HttpServletRequest request){
		Object tmp=request.getSession().getAttribute(ConfigConstants.ACCORDION_SESSION_TAG);
		if(tmp==null){
			String userAlias=CasUmsUtil.getUser(request);
			logger.info("{},setUserPermission start",userAlias);
			//得到ums系统菜单列表start
			List<EcObjects> topAccordionMenu=ecPermissionDao.findAssignPermissionObjectList(userAlias, configBean.getAuthAccessMode(),configBean.getAuthObject());
			List<UmsAccordionMenu> parentMenuList=new ArrayList<UmsAccordionMenu>();
			if(topAccordionMenu!=null&&topAccordionMenu.size()>0){
				Long level3ParenIdArr[]=new Long[topAccordionMenu.size()];
				int i=0;
				UmsAccordionMenu umsAccordionMenu=null;
				for (EcObjects ecObjects : topAccordionMenu) {
					umsAccordionMenu=new UmsAccordionMenu();
					umsAccordionMenu.setParentMenuObj(ecObjects);
					parentMenuList.add(umsAccordionMenu);
					level3ParenIdArr[i]=ecObjects.getObjId();
					i++;
				}
				List<EcObjects> level3MenuList=ecPermissionDao.findAssignPermissionObjectList(userAlias, configBean.getAuthAccessMode(), level3ParenIdArr);
				if(level3MenuList!=null&&level3MenuList.size()>0){
					Long parentId=null;
					Long parentIdTmp=null;
					for (EcObjects level3Objects : level3MenuList) {
						parentId=level3Objects.getParentId();
						for (UmsAccordionMenu umsAccordionMenu2 : parentMenuList) {
							parentIdTmp=umsAccordionMenu2.getParentMenuObj().getObjId();
							if(parentId.intValue()==parentIdTmp.intValue()){
								umsAccordionMenu2.addSumbMenuObj(level3Objects);
								break;
							}
						}
					}
				}
			}
			request.getSession().setAttribute(ConfigConstants.ACCORDION_SESSION_TAG, parentMenuList);
			logger.info("{},setUserPermission end",userAlias);
			//得到ums系统菜单列表end
			
			//判断用户是否为超级管理员
			boolean isAdmin=ecUsersAssignmenDao.checkIsAssignByAlias(userAlias, configBean.getAuthSysMgrRole());
			if(isAdmin){
				request.getSession().setAttribute(ConfigConstants.IS_SUPER_ADMIN, "YES");
			}
		}
	}
	
	public EcPermissionDao getEcPermissionDao() {
		return ecPermissionDao;
	}
	public void setEcPermissionDao(EcPermissionDao ecPermissionDao) {
		this.ecPermissionDao = ecPermissionDao;
	}

	public EcUsersAssignmenDao getEcUsersAssignmenDao() {
		return ecUsersAssignmenDao;
	}

	public void setEcUsersAssignmenDao(EcUsersAssignmenDao ecUsersAssignmenDao) {
		this.ecUsersAssignmenDao = ecUsersAssignmenDao;
	}

	public EcUserDao getEcUserDao() {
		return ecUserDao;
	}

	public void setEcUserDao(EcUserDao ecUserDao) {
		this.ecUserDao = ecUserDao;
	}

	public UmsClientDao getUmsClientDao() {
		return umsClientDao;
	}

	public void setUmsClientDao(UmsClientDao umsClientDao) {
		this.umsClientDao = umsClientDao;
	}

	public EcGroupDao getEcGroupDao() {
		return ecGroupDao;
	}

	public void setEcGroupDao(EcGroupDao ecGroupDao) {
		this.ecGroupDao = ecGroupDao;
	}

	public ConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(ConfigBean configBean) {
		this.configBean = configBean;
	}
	
	
}
