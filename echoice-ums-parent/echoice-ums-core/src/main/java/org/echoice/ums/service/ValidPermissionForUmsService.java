package org.echoice.ums.service;

import javax.servlet.http.HttpServletRequest;

import org.echoice.modules.web.json.bean.ExtJsActionView;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.dao.EcGroupDao;
import org.echoice.ums.dao.EcUserDao;
import org.echoice.ums.dao.UmsClientDao;

public interface ValidPermissionForUmsService {
	/**
	 * 设置ums内部系统菜单权限
	 * @param request
	 */
	public void setUserPermission(HttpServletRequest request);
	
	public ExtJsActionView auth(HttpServletRequest request);
	
	public EcUserDao getEcUserDao();
	
	public EcGroupDao getEcGroupDao();
	
	public ConfigBean getConfigBean();
	
	public UmsClientDao getUmsClientDao();
}
