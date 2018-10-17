package org.echoice.ums.util;

import javax.servlet.http.HttpServletRequest;

import org.echoice.ums.config.ConfigConstants;
import org.echoice.ums.dao.UmsClientDao;
import org.echoice.ums.domain.EcGroup;
import org.echoice.ums.domain.EcUser;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CasUmsUtil {
	public static final String USER_SESSION_KEY=EcUser.class.getName();
	
	public static boolean isAuthCas(HttpServletRequest request){
		Object obj=request.getSession().getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION);
    	if (obj instanceof Assertion) {
			return true;
		}
    	return false;
	}
	
    public static String getUser(HttpServletRequest request){
    	Object obj=request.getSession().getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION);
    	if (obj instanceof Assertion) {
			Assertion assertion = (Assertion) obj;
			return assertion.getPrincipal().getName();
		}else{
			return (String)obj;
		}
    }
    
	/**
	 * 查看cas登入用户(基于AssertionHolder.getAssertion().getPrincipal().getName())
	 * @return
	 */
	public static String getUserName(){
		String name=AssertionHolder.getAssertion().getPrincipal().getName();
		return name;
	}
    
	/**
	 * 查看cas登入用户详细信息
	 * @param request
	 * @return
	 */
	public static EcUser getUserInfo(HttpServletRequest request){
		EcUser umsUser=(EcUser)request.getAttribute(USER_SESSION_KEY);
		if(umsUser==null){
			WebApplicationContext applicationContext=WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
			UmsClientDao umsClientDao=(UmsClientDao)applicationContext.getBean("umsClientDao");
			umsUser=umsClientDao.getUser(getUserName());
			request.setAttribute(USER_SESSION_KEY, umsUser);
		}
		return umsUser;
	}
	
    public static boolean isAdmin(HttpServletRequest request){
    	Object isAdmin=request.getSession().getAttribute(ConfigConstants.IS_SUPER_ADMIN);
    	if(isAdmin==null) {
    		return false;
    	}else {
    		return (boolean)isAdmin;
    	}
    }
    
    public static EcGroup getUserGroup(HttpServletRequest request){
    	return (EcGroup)request.getSession().getAttribute(ConfigConstants.UMS_GROUP_SESSION);
    }
    
    public static void setUserGroup(HttpServletRequest request,EcGroup group){
    	request.getSession().setAttribute(ConfigConstants.UMS_GROUP_SESSION, group);
    }
    
}
