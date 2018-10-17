package org.echoice.ums.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.web.MsgTip;
import org.echoice.modules.web.paper.PageBean;
import org.echoice.modules.web.ztree.ZTreeView;
import org.echoice.ums.dao.EcRoleDao;
import org.echoice.ums.domain.EcRole;
import org.echoice.ums.util.CasUmsUtil;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.view.MsgTipExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
@Controller
@RequestMapping("/console/role")
public class RoleController extends UmsBaseController{
	private static final String PAGE_SIZE="20";
	private static final String[] EXCLUDE_FIELDS=new String[]{"ecPermissions","ecGroupAssignments","ecUsersAssignmens"};
	
	@Autowired
	private EcRoleDao ecRoleDao;
	
	@RequestMapping("index")
	public String index() throws Exception {
		// TODO Auto-generated method stub
		return "role/index";
	}
	
	@RequestMapping(value="searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,EcRole searchForm,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		PageBean pageBean=ecRoleDao.searchPageCondition(searchForm, pageNumber, pageSize);
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}
	
	
	@RequestMapping(value="tree",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String tree(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		ZTreeView treeView = null;
		String tmpId=null;
		for (EcRole tmpObj : childList) {
			tmpId=String.valueOf(tmpObj.getRoleId());
			treeView = new ZTreeView();
			boolean isParent = false;
			if(strParentTree.indexOf("|"+tmpObj.getRoleId()+"|")!=-1){
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
	
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String save(HttpServletRequest request,HttpServletResponse response,EcRole ecRole) throws Exception {
		MsgTipExt msgTip=new MsgTipExt(); 
		ecRole.setAlias(ecRole.getAlias().trim());
		ecRole.setName(ecRole.getName().trim());
		
		List<EcRole> list=ecRoleDao.findByAlias(ecRole.getAlias());
		if(list!=null&&list.size()>0){
			EcRole dbRole=list.get(0);
			if(ecRole.getRoleId()==null||(dbRole.getRoleId().compareTo(ecRole.getRoleId())!=0)){
				msgTip.setMsg("对不起，角色标识"+ecRole.getAlias()+"已经存在，请换一个");
				msgTip.setCode(4002);
			}
		}
		
		if(msgTip.getCode()==0) {
			ecRole.setOpTime(new Date());
			ecRoleDao.save(ecRole);
			msgTip.setData(ecRole);
		}
		logger.info(CasUmsUtil.getUser(request)+" 操作角色："+ecRole.getAlias()+"，"+ecRole.getName());
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="edit",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String edit(HttpServletRequest request,HttpServletResponse response,EcRole ecRole) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		EcRole dbRole=ecRoleDao.findOne(ecRole.getRoleId());
		if(dbRole.getParentId()!=null){
			EcRole ecRoleParent=ecRoleDao.findOne(dbRole.getParentId());
			if(ecRoleParent!=null){
				dbRole.setParentName(dbRole.getName());
			}else{
				dbRole.setParentName("角色组");
			}
		}
		msgTip.setData(dbRole);
		String respStr=JSONUtil.toJSONString(msgTip, EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="del",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String del(HttpServletRequest request,HttpServletResponse response,@RequestParam String selIds) throws Exception {
		MsgTip msgTip=new MsgTip();
		List<Long> idsArr=JSON.parseArray(selIds, Long.class);
		if(idsArr!=null&&idsArr.size()>0) {
			List list=null;
			List<Long> noDelList=new ArrayList<Long>();
			for (Long id : idsArr) {
				list=getEcRoleDao().findRoleTreeChild(id);
				if(list==null||list.size()==0){
					getEcRoleDao().delete(id);
				}else {
					noDelList.add(id);
				}
			}
			
			if(noDelList.size()>0) {
				msgTip.setMsg("ID为："+JSON.toJSONString(noDelList)+"无法删除！！");
			}
		}else {
			msgTip.setCode(4002);
			msgTip.setMsg("未找到记录");
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
		getEcRoleDao().updateDrag(Long.valueOf(dragId), Long.valueOf(targetId));
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
	
	public EcRoleDao getEcRoleDao() {
		return ecRoleDao;
	}

	public void setEcRoleDao(EcRoleDao ecRoleDao) {
		this.ecRoleDao = ecRoleDao;
	}
}
