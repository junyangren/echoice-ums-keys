package org.echoice.ums.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.web.MsgTip;
import org.echoice.modules.web.ztree.ZTreeView;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.dao.EcGroupAssignmentDao;
import org.echoice.ums.dao.EcRoleDao;
import org.echoice.ums.dao.EcUserDao;
import org.echoice.ums.dao.EcUsersAssignmenDao;
import org.echoice.ums.domain.EcRole;
import org.echoice.ums.util.CasUmsUtil;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.view.UserGroupTotalView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
@Controller
@RequestMapping("/console/usersAssignmen")
public class UsersAssignmenController extends UmsBaseController {
	@Autowired
	private EcRoleDao ecRoleDao; 
	@Autowired
	private EcUsersAssignmenDao ecUsersAssignmenDao;
	@Autowired
	private EcGroupAssignmentDao ecGroupAssignmentDao;
	@Autowired
	private EcUserDao ecUserDao;
	@Autowired
	private ConfigBean configBean;
	/**
	 * 给用户分配角色
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="assignRole",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String assignRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		logger.debug("用户分配角色start:");
		String userIds=request.getParameter("userIds");
		String roleIds=request.getParameter("roleIds");
		List<Long> userIdsList= JSON.parseArray(userIds, Long.class);
		List<Long> roleIdsList= JSON.parseArray(roleIds, Long.class);
		
		Long[] userIdArr=userIdsList.toArray(new Long[userIdsList.size()]);
		Long[] roleIdsArr=roleIdsList.toArray(new Long[roleIdsList.size()]);
		
		ecUsersAssignmenDao.saveBatch(userIdArr,roleIdsArr);
		logger.info(CasUmsUtil.getUser(request)+"操作分配角色  userIdArr:"+userIds+"roleArr:"+roleIds);
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	/**
	 * 用户分配组
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="assignGroup",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String assignGroup(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		logger.debug("用户组分配start:");

		String userIds=request.getParameter("userIds");
		String groupIds=request.getParameter("groupIds");
		List<Long> userIdsList= JSON.parseArray(userIds, Long.class);
		List<Long> groupIdsList= JSON.parseArray(groupIds, Long.class);
		
		Long[] userIdArr=userIdsList.toArray(new Long[userIdsList.size()]);
		Long[] groupIdsArr=groupIdsList.toArray(new Long[groupIdsList.size()]);
		
		ecUsersAssignmenDao.saveAssignGroups(userIdArr,groupIdsArr);
		logger.info(CasUmsUtil.getUser(request)+"操作用户组分配  userIdArr:"+userIds+""+"groupArr:"+groupIds);
		logger.debug("用户组分配end:");
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	/**
	 * 将角色从用户中移除
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="removeRole",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String removeRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		logger.debug("用户移除角色start:");
		String userIds=request.getParameter("userIds");
		String roleIds=request.getParameter("roleIds");
		List<Long> userIdsList= JSON.parseArray(userIds, Long.class);
		List<Long> roleIdsList= JSON.parseArray(roleIds, Long.class);
		
		Long[] userIdArr=userIdsList.toArray(new Long[userIdsList.size()]);
		Long[] roleIdsArr=roleIdsList.toArray(new Long[roleIdsList.size()]);
		
		ecUsersAssignmenDao.removeBatch(userIdArr,roleIdsArr);
		logger.info(CasUmsUtil.getUser(request)+"操作移除角色  userIdArr:"+userIds+"roleArr:"+roleIds);
		logger.debug("用户移除角色end:");
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	@RequestMapping(value="removeAssignGroup",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String removeAssignGroup(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		logger.debug("用户移组start:");

		String userIds=request.getParameter("userIds");
		String groupIds=request.getParameter("groupIds");
		List<Long> userIdsList= JSON.parseArray(userIds, Long.class);
		List<Long> groupIdsList= JSON.parseArray(groupIds, Long.class);
		
		Long[] userIdArr=userIdsList.toArray(new Long[userIdsList.size()]);
		Long[] groupIdsArr=groupIdsList.toArray(new Long[groupIdsList.size()]);
		
		List<UserGroupTotalView> list= ecUsersAssignmenDao.findUserGroupCount(userIdArr);
		List<UserGroupTotalView> list2= ecUsersAssignmenDao.findUserGroupCount(userIdArr,groupIdsArr);
		//判断用户移除后至少保留一个组
		long tmpId1;
		long tmpId2;
		boolean isPass=true;
		for (UserGroupTotalView userGroupTotalView : list) {
			tmpId1=userGroupTotalView.getUserId();
			for (UserGroupTotalView userGroupTotalView2 : list2) {
				tmpId2=userGroupTotalView2.getUserId();
				if(tmpId1==tmpId2){
					if(userGroupTotalView.getGroupCount()==userGroupTotalView2.getGroupCount()){
						isPass=false;
						break;
					}
				}
			}
			if(!isPass){
				break;
			}
		}
		
		if(!isPass){
			msgTip.setMsg("用户所在组移除失败，用户必须至少拥有一个所在组");
			msgTip.setCode(4002);
		}
		
		if(msgTip.getCode()==0) {
			ecUsersAssignmenDao.removeAssingGroup(userIdArr,groupIdsArr);
			logger.info(CasUmsUtil.getUser(request)+"操作用户移组  userIdArr:"+userIds+"groupArr:"+groupIds);		
			logger.debug("用户移组end:");
		}
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}

	@RequestMapping(value="roleCheckTree",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String roleCheckTree(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		String userId=request.getParameter("userId");
		String groupId=request.getParameter("groupId");
		
		List<ZTreeView> zTreelist = new ArrayList<ZTreeView>();
		if(StringUtils.isBlank(id)) {
			 String rootId=request.getParameter("rootId");
			 if(StringUtils.isNotBlank(rootId)){
				 id=rootId; 
			 }else{ 
				 id="-1";
			 }
			 ZTreeView treeView = new ZTreeView();
			 treeView.setId(id);
			 treeView.setName("所有");
			 treeView.setIsParent(true);
			 treeView.setOpen(true);
			 zTreelist.add(treeView);
			 return JSON.toJSONString(zTreelist);
		}
		
		StringBuffer bf2=new StringBuffer();
		if(StringUtils.isNotBlank(userId)){
			//得到用户已经分配角色
			List checkList=ecUsersAssignmenDao.findAssignRoleList(Long.valueOf(userId));
			bf2.append("|");
			for (Object object : checkList) {
				EcRole temp=(EcRole)object;
				bf2.append(temp.getRoleId());
				bf2.append("|");
			}
		}
		
		if(StringUtils.isNotBlank(groupId)){
			//得到用户组已经分配角色
			List checkList=ecGroupAssignmentDao.findAssignRoleList(Long.valueOf(groupId));
			bf2.append("|");
			for (Object object : checkList) {
				EcRole temp=(EcRole)object;
				bf2.append(temp.getRoleId());
				bf2.append("|");
			}
		}
		
		String checkTreeStr=bf2.toString();
		
		List list=ecRoleDao.findRoleTreeParent();
		StringBuffer bf=new StringBuffer();
		bf.append("|");
		for (Object object : list) {
			Long temp=(Long)object;
			bf.append(temp);
			bf.append("|");
		}
		
		String strParentTree=bf.toString();
		
		List<EcRole> childList=ecRoleDao.findRoleTreeChild(Long.valueOf(id));
		boolean isAdmin=CasUmsUtil.isAdmin(request);
		List<EcRole> commonUserRoleList=null;
		if(!isAdmin) {//非管理员用于过滤树菜单
			commonUserRoleList=ecUsersAssignmenDao.findAssignRoleList(CasUmsUtil.getUser(request));
		}
		
		ZTreeView treeView = null;
		String tmpId=null;
		boolean isShow=true;
		for (EcRole tmpObj : childList) {
			tmpId=String.valueOf(tmpObj.getRoleId());
			treeView = new ZTreeView();
			boolean isParent = false;
			if(strParentTree.indexOf("|"+tmpObj.getRoleId()+"|")!=-1){
				isParent = true;
			}
			
			if(!isAdmin&&!isParent) {
				isShow=false;
				for (EcRole commonUserRole : commonUserRoleList) {
					if(tmpObj.getRoleId().compareTo(commonUserRole.getRoleId())==0) {
						isShow=true;
						break;
					}
				}
			}
			
			if(isShow) {
				//当选择个人用户时，对分配的角色选中
				if(StringUtils.isNotBlank(userId)){
					if(checkTreeStr.indexOf("|"+tmpObj.getRoleId()+"|")!=-1){
						treeView.setChecked(true);
					}else{
						treeView.setChecked(false);
					}	
				}
				
				if(StringUtils.isNotBlank(groupId)){
					if(checkTreeStr.indexOf("|"+tmpObj.getRoleId()+"|")!=-1){
						treeView.setChecked(true);
					}else{
						treeView.setChecked(false);
					}	
				}
				
				treeView.setId(tmpId);
				treeView.setName(tmpObj.getName());
				treeView.setIsParent(isParent);
				treeView.setExtNote(tmpObj.getNote());
				treeView.setAlias(tmpObj.getAlias());
				treeView.setTitle(tmpObj.getName());
				treeView.setIsParent(isParent);
				zTreelist.add(treeView);
			}

		}
		
		String resp=JSON.toJSONString(zTreelist);
		return resp;
	}
	
	public EcUsersAssignmenDao getEcUsersAssignmenDao() {
		return ecUsersAssignmenDao;
	}

	public void setEcUsersAssignmenDao(EcUsersAssignmenDao ecUsersAssignmenDao) {
		this.ecUsersAssignmenDao = ecUsersAssignmenDao;
	}

	public EcRoleDao getEcRoleDao() {
		return ecRoleDao;
	}

	public void setEcRoleDao(EcRoleDao ecRoleDao) {
		this.ecRoleDao = ecRoleDao;
	}
	public EcGroupAssignmentDao getEcGroupAssignmentDao() {
		return ecGroupAssignmentDao;
	}
	public void setEcGroupAssignmentDao(EcGroupAssignmentDao ecGroupAssignmentDao) {
		this.ecGroupAssignmentDao = ecGroupAssignmentDao;
	}
	public EcUserDao getEcUserDao() {
		return ecUserDao;
	}
	public void setEcUserDao(EcUserDao ecUserDao) {
		this.ecUserDao = ecUserDao;
	}
	public ConfigBean getConfigBean() {
		return configBean;
	}
	public void setConfigBean(ConfigBean configBean) {
		this.configBean = configBean;
	}

}
