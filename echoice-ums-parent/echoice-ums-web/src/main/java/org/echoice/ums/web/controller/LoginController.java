package org.echoice.ums.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.cas.CasUtil;
import org.echoice.modules.encrypt.MD5;
import org.echoice.modules.web.MsgTip;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.dao.EcGroupDao;
import org.echoice.ums.dao.EcUserDao;
import org.echoice.ums.domain.EcGroup;
import org.echoice.ums.domain.EcUser;
import org.echoice.ums.service.ValidPermissionForUmsService;
import org.echoice.ums.util.CasUmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController{
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ConfigBean configBean;
	@Autowired
	private EcUserDao ecUserDao;
	@Autowired
	private EcGroupDao ecGroupDao;
	@Autowired
	private ValidPermissionForUmsService validPermissionForUmsService; 
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String index(HttpServletRequest request,HttpServletResponse response) throws Exception {
		return "login";
	}

	@RequestMapping(value="/login",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public MsgTip login(HttpServletRequest request,HttpServletResponse response) throws Exception {
		MsgTip msgTip=new MsgTip();
		String userAlias=request.getParameter("loginName");
		String password=request.getParameter("password");
		String authCode=request.getParameter("authCode");
		String authCodeSession=(String)request.getSession().getAttribute(CaptchaImageCreateController.KEY_CAPTCHA);
		
		if(!(StringUtils.equalsIgnoreCase(authCode, authCodeSession))) {
			msgTip.setCode(303);
			msgTip.setMsg("对不起，验证码错误");
			return msgTip;
		}
		
		List<EcUser> list=ecUserDao.findByAlias(userAlias);
		if(list==null||list.size()<=0){
			msgTip.setCode(301);
			msgTip.setMsg("对不起，用户名错误");
			return msgTip;
		}
		
		EcUser ecUser=list.get(0);
		String passWordDb=ecUser.getPassword();
		MD5 md5=new MD5();
		String userPassWord=md5.getMD5ofStr(userAlias+password);
		//用户密码校验
		if(!(StringUtils.equalsIgnoreCase(passWordDb, userPassWord))){
			msgTip.setCode(302);
			msgTip.setMsg("对不起，用户密码错误");
			return msgTip;	
		}
		
		request.getSession().removeAttribute(CaptchaImageCreateController.KEY_CAPTCHA);
		request.getSession().setAttribute(CasUtil.CONST_CAS_ASSERTION, ecUser.getAlias());
		return msgTip;
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
		request.getSession().invalidate();
		return "redirect:/login";
	}
	
	@RequestMapping(value="/selGroup",params={"action=selGroup"})
	public ModelAndView selGroup(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String groupId=request.getParameter("groupId");
		EcGroup group=ecGroupDao.findOne(Long.valueOf(groupId));
		CasUmsUtil.setUserGroup(request, group);
		return new ModelAndView("redirect:/index.jsp");
	}
	
	public EcUserDao getEcUserDao() {
		return ecUserDao;
	}
	public void setEcUserDao(EcUserDao ecUserDao) {
		this.ecUserDao = ecUserDao;
	}
	
	public ValidPermissionForUmsService getValidPermissionForUmsService() {
		return validPermissionForUmsService;
	}
	public void setValidPermissionForUmsService(
			ValidPermissionForUmsService validPermissionForUmsService) {
		this.validPermissionForUmsService = validPermissionForUmsService;
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
