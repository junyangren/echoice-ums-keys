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
import org.echoice.ums.dao.EcObjectsDao;
import org.echoice.ums.domain.EcObjects;
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
@RequestMapping("/console/objects")
public class ObjectsController{
	private static final String PAGE_SIZE="20";
	private static final String[] EXCLUDE_FIELDS=new String[]{"ecOperators"};
	
	@Autowired
	private EcObjectsDao ecObjectsDao;

	@RequestMapping("index")
	public String index() throws Exception {
		// TODO Auto-generated method stub
		return "objects/index";
	}

	@RequestMapping(value="searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,EcObjects ecObjects,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		PageBean pageBean=ecObjectsDao.searchPageCondition(ecObjects, pageNumber, pageSize);
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}

	@RequestMapping(value="edit",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String edit(HttpServletRequest request,HttpServletResponse response,EcObjects ecObjects) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		EcObjects dbObjects=getEcObjectsDao().findOne(ecObjects.getObjId());
		if(dbObjects.getParentId()!=null){
			EcObjects ecObjectsParent=getEcObjectsDao().findOne(dbObjects.getParentId());
			if(ecObjectsParent!=null){
				dbObjects.setParentName(ecObjectsParent.getName());
			}else{
				dbObjects.setParentName("对象");
			}
		}
		msgTip.setData(dbObjects);
		String respStr=JSONUtil.toJSONString(msgTip, EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="del",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String del(HttpServletRequest request,HttpServletResponse response,@RequestParam String selIds) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		List<Long> idsArr=JSON.parseArray(selIds, Long.class);
		if(idsArr!=null&&idsArr.size()>0) {
			List list=null;
			List<Long> noDelList=new ArrayList<Long>();
			for (Long id : idsArr) {
				list=getEcObjectsDao().findChildObjects(id);
				if(list==null||list.size()==0){
					getEcObjectsDao().delete(id);
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
	public String drag(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt(); 
		String dragId=request.getParameter("dragId");
		String targetId=request.getParameter("targetId");
		getEcObjectsDao().updateDrag(Long.valueOf(dragId), Long.valueOf(targetId));
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
	/**
	 * 保存对象数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String save(HttpServletRequest request,HttpServletResponse response,EcObjects ecObjects) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt(); 
		ecObjects.setAlias(ecObjects.getAlias().trim());
		ecObjects.setName(ecObjects.getName().trim());
		List<EcObjects> list=ecObjectsDao.findByAlias(ecObjects.getAlias());
		
		
		if(list!=null&&list.size()>0){
			EcObjects dbObject=list.get(0);
			if(ecObjects.getObjId()==null||(dbObject.getObjId().compareTo(ecObjects.getObjId())!=0)){
				msgTip.setMsg("对不起，资源对象标识"+ecObjects.getAlias()+"已经存在，请换一个");
				msgTip.setCode(4002);
			}
		}
		
		if(msgTip.getCode()==0) {
			ecObjects.setOpTime(new Date());
			getEcObjectsDao().save(ecObjects);
			msgTip.setData(ecObjects);
		}
		
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
	
	/**
	 * 得到树型对象树
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
		
		List list=ecObjectsDao.findAllParent();
		StringBuffer bf=new StringBuffer();
		bf.append("|");
		for (Object object : list) {
			Long temp=(Long)object;
			bf.append(temp);
			bf.append("|");
		}
		
		String strParentTree=bf.toString();
		
		List<EcObjects> childList=ecObjectsDao.findChildObjects(Long.valueOf(id));
		ZTreeView treeView = null;
		String tmpId=null;
		for (EcObjects tmpObj : childList) {
			tmpId=String.valueOf(tmpObj.getObjId());
			treeView = new ZTreeView();
			boolean isParent = false;
			if(strParentTree.indexOf("|"+tmpObj.getObjId()+"|")!=-1){
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
	
	public EcObjectsDao getEcObjectsDao() {
		return ecObjectsDao;
	}
	
	public void setEcObjectsDao(EcObjectsDao ecObjectsDao) {
		this.ecObjectsDao = ecObjectsDao;
	}
}
