package org.echoice.ums.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.web.MsgTip;
import org.echoice.modules.web.paper.PageBean;
import org.echoice.modules.web.ztree.ZTreeView;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.config.ConfigConstants;
import org.echoice.ums.dao.EcGroupDao;
import org.echoice.ums.dao.EcObjectsDao;
import org.echoice.ums.dao.EcUserDao;
import org.echoice.ums.domain.EcGroup;
import org.echoice.ums.domain.EcObjects;
import org.echoice.ums.plugins.Command;
import org.echoice.ums.plugins.bean.ResultMsg;
import org.echoice.ums.quartz.QuartzTriggerRunner;
import org.echoice.ums.service.AppPluginService;
import org.echoice.ums.service.UmsCommonService;
import org.echoice.ums.util.CasUmsUtil;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.UmsHolder;
import org.echoice.ums.web.view.MsgTipExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/console/group")
public class GroupController extends UmsBaseController {
	private static final String PAGE_SIZE="20";
	private static final String[] EXCLUDE_FIELDS=new String[]{"ecUserGroups","ecGroupAssignments"};
	
	@Autowired
	private EcGroupDao ecGroupDao;
	@Autowired
	private EcUserDao ecUserDao;
	@Autowired
	private ConfigBean configBean;
	@Autowired
	private AppPluginService appPluginService;
	@Autowired
	private UmsCommonService umsCommonService;
	private EcObjectsDao ecObjectsDao;
	
	
	@RequestMapping("index")
	public String index() throws Exception {
		// TODO Auto-generated method stub
		return "group/index";
	}
	
	@RequestMapping(value="searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,EcGroup ecGroup,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		boolean isAdmin=CasUmsUtil.isAdmin(request);
		if(!isAdmin){
			ecGroup.setGroupPath(UmsHolder.getUserGroup().getAlias());//通过层级限定
		}
		PageBean pageBean=ecGroupDao.findPageCondition(ecGroup, pageNumber, pageSize);
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="updateGroupFullName",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String updateGroupFullName(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String groupId=request.getParameter("groupId");
		String msg="用户组Path操作成功！！";
		try{
			if(StringUtils.isBlank(groupId)){
				EcObjects objects= ecObjectsDao.getObjectsByAlias(ConfigConstants.UMS_GROUP_ROOT_ID);
				if(objects!=null&&StringUtils.isNotBlank(objects.getNote())){
					groupId=objects.getNote();
				}else{
					groupId="-1";
				}
			}
			int result=ecGroupDao.updateGroupFullNameByProc(Integer.parseInt(groupId));
			if(result!=0){
				msg="操作失败，错误码："+result;
			}
		}catch (Exception e) {
			// TODO: handle exception
			msg="操作失败，原因："+e.getMessage();
		}
		return null;
	}
	
	/**
	 * 得到树型用户组树
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="tree",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String tree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		List<ZTreeView> zTreelist = new ArrayList<ZTreeView>();
		boolean isAdmin=UmsHolder.isAdmin();
		List<EcGroup> childList=null;
		if(StringUtils.isBlank(id)) {
			childList=new ArrayList<EcGroup>();
			EcGroup ecGroup=null;
			if(isAdmin) {
				ecGroup=new EcGroup();
				ecGroup.setGroupId(-1L);
				ecGroup.setName("所有");
			}else {
				ecGroup=CasUmsUtil.getUserGroup(request);
			}
			childList.add(ecGroup);
		}else {
			childList=ecGroupDao.findGroupTreeChild(Long.valueOf(id));
		}
		
		//获取根节点ID
		List list=ecGroupDao.findGroupTreeParent();
		StringBuffer bf=new StringBuffer();
		bf.append("|-1|");
		for (Object object : list) {
			Long temp=(Long)object;
			bf.append(temp);
			bf.append("|");
		}
		
		String strParentTree=bf.toString();
		
		ZTreeView treeView = null;
		String tmpId=null;
		for (EcGroup tmpObj : childList) {
			tmpId=String.valueOf(tmpObj.getGroupId());
			treeView = new ZTreeView();
			boolean isParent = false;
			if(strParentTree.indexOf("|"+tmpObj.getGroupId()+"|")!=-1){
				isParent = true;
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
		String resp=JSON.toJSONString(zTreelist);
		return resp;
	}
	
	/**
	  *  得到树型用户组树
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="treeAssignGroup",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String treeAssignGroup(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		String userId=request.getParameter("userId");
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
			bf2.append("|");
			List groupList=ecUserDao.findGroupByUserId(Long.valueOf(userId));
			for (Object object : groupList) {
				EcGroup ecGroup=(EcGroup)object;
				bf2.append(ecGroup.getGroupId());
				bf2.append("|");
			}
		}
		String checkTreeStr=bf2.toString();
		
		List list=ecGroupDao.findGroupTreeParent();
		StringBuffer bf=new StringBuffer();
		bf.append("|");
		for (Object object : list) {
			Long temp=(Long)object;
			bf.append(temp);
			bf.append("|");
		}
		
		String strParentTree=bf.toString();
		
		List<EcGroup> childList = ecGroupDao.findGroupTreeChild(Long.valueOf(id));
		ZTreeView treeView = null;
		String tmpId=null;
		boolean isParent =false;
		boolean checked=false;
		for (EcGroup tmpObj : childList) {
			tmpId=String.valueOf(tmpObj.getGroupId());
			treeView = new ZTreeView();
			isParent = false;
			if(strParentTree.indexOf("|"+tmpObj.getGroupId()+"|")!=-1){
				isParent = true;
			}
			
			//选中已经分配的组
			checked=false;
			if(StringUtils.isNotBlank(userId)){
				if(checkTreeStr.indexOf("|"+tmpObj.getGroupId()+"|")!=-1){
					checked=true;
				}
			}
			treeView.setChecked(checked);
			
			treeView.setId(tmpId);
			treeView.setName(tmpObj.getName());
			treeView.setIsParent(isParent);
			treeView.setExtNote(tmpObj.getNote());
			treeView.setAlias(tmpObj.getAlias());
			treeView.setTitle(tmpObj.getName());
			treeView.setIsParent(isParent);
			zTreelist.add(treeView);
		}
		String resp=JSON.toJSONString(zTreelist);
		return resp;
	}
	/**
	 * 得到子节点用户组列表
	 * @param request
	 * @param parentId
	 * @return
	 */
	private List<EcGroup> getChildGroupList(HttpServletRequest request,String parentId) {
		boolean isUserGoup=true;
		if(StringUtils.isNotBlank(parentId)&&"-1".equals(parentId)){
			boolean isAdmin=CasUmsUtil.isAdmin(request);
			if(isAdmin){
				isUserGoup=false;
			}
		}else{
			isUserGoup=false;
		}
		List<EcGroup> childList=null;
		if(isUserGoup){
			//取得用户所在用户组列表
			if(configBean.getGroupModeType()==1){
				childList=new ArrayList<EcGroup>();
				childList.add(CasUmsUtil.getUserGroup(request));
			}else{
				childList=ecUserDao.findGroupByUserAndParenRoleAlias(CasUmsUtil.getUser(request),configBean.getGroupRoleParentAlias());
			}
			//
		}else{
			childList=ecGroupDao.findGroupTreeChild(Long.valueOf(parentId));
		}
		return childList;
	}
	
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String save(HttpServletRequest request,HttpServletResponse response,EcGroup ecGroup) throws Exception {
		MsgTipExt msgTip=new MsgTipExt();
		ecGroup.setName(ecGroup.getName().trim());
		if(StringUtils.isNotBlank(ecGroup.getAlias())) {
			ecGroup.setAlias(ecGroup.getAlias().trim());
			List<EcGroup> list=ecGroupDao.findByAlias(ecGroup.getAlias());
			if(list!=null&&list.size()>0){
				EcGroup dbGroup=list.get(0);
				if(ecGroup.getGroupId()==null||(dbGroup.getGroupId().compareTo(ecGroup.getGroupId())!=0)){
					msgTip.setMsg("对不起，用户组标识 "+ecGroup.getAlias()+" 已经存在，请换一个");
					msgTip.setCode(4002);
				}
			}
		}
		if(msgTip.getCode()==0) {
			umsCommonService.saveGroup(ecGroup);
			if(configBean.getSyncGroupPath()){
				QuartzTriggerRunner.runGroupSyncTask();
			}
			msgTip.setData(ecGroup);
		}
		
		logger.info(CasUmsUtil.getUser(request)+" 操作用户组："+ecGroup.getAlias()+"，"+ecGroup.getName());
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
		
	@RequestMapping(value="edit",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String edit(HttpServletRequest request,HttpServletResponse response,EcGroup searcForm) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		EcGroup one=getEcGroupDao().findOne(searcForm.getGroupId());
		if(one.getParentId()!=null){
			EcGroup parent=getEcGroupDao().findOne(one.getParentId());
			if(parent!=null){
				one.setParentName(parent.getName());
			}else{
				one.setParentName("用户组");
			}
		}
		msgTip.setData(one);
		String respStr=JSONUtil.toJSONString(msgTip, EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="del",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String del(HttpServletRequest request,HttpServletResponse response,@RequestParam String selIds) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		List<Long> idsArr=JSON.parseArray(selIds, Long.class);
		String idsStr=StringUtils.join(idsArr,",");
		ResultMsg resultMsg=null;
		List<Command<ResultMsg, String>> groupFilterList=appPluginService.getGroupFilterList();
		if(groupFilterList!=null&&groupFilterList.size()>0){
			for (Command<ResultMsg, String> tmp : groupFilterList) {
				resultMsg=tmp.execute(idsStr);
				if(!resultMsg.isResult()){
					msgTip.setCode(4002);
					msgTip.setMsg(resultMsg.getMsg());
					break;
				}
			}
		}
		
		if(msgTip.getCode()==0) {
			List<EcGroup> groupList=ecGroupDao.findGroupListByIds(idsStr);
			ecGroupDao.deleteGroupByIds(idsStr);
			for (EcGroup ecGroup : groupList) {
				logger.info(CasUmsUtil.getUser(request)+" 删除用户组："+ecGroup.getAlias()+"，"+ecGroup.getName());
			}
		}
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}	
		
	/**
	 * 节点拖动
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="drag",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String drag(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt(); 
		String dragId=request.getParameter("dragId");
		String targetId=request.getParameter("targetId");
		umsCommonService.saveDragGroup(Long.valueOf(dragId), Long.valueOf(targetId));
		if(configBean.getSyncGroupPath()){
			QuartzTriggerRunner.runGroupSyncTask();
		}
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
	
	public EcGroupDao getEcGroupDao() {
		return ecGroupDao;
	}
	public void setEcGroupDao(EcGroupDao ecGroupDao) {
		this.ecGroupDao = ecGroupDao;
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

	public AppPluginService getAppPluginService() {
		return appPluginService;
	}
	public void setAppPluginService(AppPluginService appPluginService) {
		this.appPluginService = appPluginService;
	}
	public UmsCommonService getUmsCommonService() {
		return umsCommonService;
	}

	public void setUmsCommonService(UmsCommonService umsCommonService) {
		this.umsCommonService = umsCommonService;
	}

	public EcObjectsDao getEcObjectsDao() {
		return ecObjectsDao;
	}

	public void setEcObjectsDao(EcObjectsDao ecObjectsDao) {
		this.ecObjectsDao = ecObjectsDao;
	}

	
}
