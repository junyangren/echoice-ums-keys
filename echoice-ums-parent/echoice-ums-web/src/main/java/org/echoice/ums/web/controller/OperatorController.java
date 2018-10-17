package org.echoice.ums.web.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.web.json.bean.JSONCheckTreeNode;
import org.echoice.ums.config.ConfigConstants;
import org.echoice.ums.dao.EcAccssModeDao;
import org.echoice.ums.dao.EcObjectsDao;
import org.echoice.ums.dao.EcOperatorDao;
import org.echoice.ums.dao.EcPermissionDao;
import org.echoice.ums.domain.EcAccssMode;
import org.echoice.ums.domain.EcObjects;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.view.EcAccssModeView;
import org.echoice.ums.web.view.MsgTipExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/console/operator")
public class OperatorController extends UmsBaseController {
	private static final String[] EXCLUDE_FIELDS=new String[]{"ecOperators"};
	@Autowired
	private EcAccssModeDao ecAccssModeDao;
	@Autowired
	private EcOperatorDao ecOperatorDao;
	@Autowired
	private EcObjectsDao ecObjectsDao;
	@Autowired
	private EcPermissionDao ecPermissionDao;
	/**
	 * 给对象分配操作
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="assign",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String assign(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		String accssIdsStr=request.getParameter("accssIds");
	 	List<Long> accssIds=JSON.parseArray(accssIdsStr, Long.class);
	 	
	 	String objIdsStr=request.getParameter("objIds");
	 	List<Long> objIds=JSON.parseArray(objIdsStr, Long.class);
	 	
		ecOperatorDao.saveBatch(accssIds,objIds);
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	/**
	 * 移除操作对应的对象
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="remove",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		String accssIdsStr=request.getParameter("accssIds");
	 	List<Long> accssIds=JSON.parseArray(accssIdsStr, Long.class);
	 	
	 	String objIdsStr=request.getParameter("objIds");
	 	List<Long> objIds=JSON.parseArray(objIdsStr, Long.class);
	 	
		ecOperatorDao.removeBatch(accssIds,objIds);
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	/**
	 * 当选择一个操作后列出所有选择对象
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params={"action=operatorCheckTree"})
	public ModelAndView operatorCheckTree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String parentId=request.getParameter("parentId");
		String accessId=request.getParameter("accessId");
		List list=ecObjectsDao.findAllParent();
		StringBuffer bf=new StringBuffer();
		bf.append("|");
		for (Object object : list) {
			Long temp=(Long)object;
			bf.append(temp);
			bf.append("|");
		}
		
		String strParentTree=bf.toString();
		
		StringBuffer bf2=new StringBuffer();
		if(StringUtils.isNotBlank(accessId)){
			//得到操作已经分配的对象
			List checkList=ecOperatorDao.findObjectListByAccessId(Long.valueOf(accessId));
			bf2.append("|");
			for (Object object : checkList) {
				EcObjects temp=(EcObjects)object;
				bf2.append(temp.getObjId());
				bf2.append("|");
			}
		}
		String checkTreeStr=bf2.toString();
		List<EcObjects> childList=ecObjectsDao.findChildObjects(Long.valueOf(parentId));
		List<JSONCheckTreeNode> listTree=new ArrayList<JSONCheckTreeNode>();
		for (EcObjects ecObject : childList) {
			JSONCheckTreeNode treeNode=new JSONCheckTreeNode();
			treeNode.setId(ConfigConstants.OBJECT_ASSIGN_TREE+ecObject.getObjId());
			treeNode.setText(ecObject.getName());
			if(strParentTree.indexOf("|"+ecObject.getObjId()+"|")!=-1){
				treeNode.setLeaf(false);
			}else{
				treeNode.setLeaf(true);
			}
			if(StringUtils.isNotBlank(accessId)){
				if(checkTreeStr.indexOf("|"+ecObject.getObjId()+"|")!=-1){
					treeNode.setChecked(true);
				}else{
					treeNode.setChecked(false);
				}
			}
			listTree.add(treeNode);
		}
		//JSONArray jsonarr=JSONArray.fromObject(listTree);
		String data=JSON.toJSONString(listTree);
		return null;
	}
	
	@RequestMapping(value="findAccessByObjID",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String findAccessByObjID(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String objId=request.getParameter("objId");
		List<EcAccssMode> listAll=ecAccssModeDao.findAll();
		String respStr=null;
		if(StringUtils.isNotBlank(objId)) {
			List<EcAccssMode> sellist=ecOperatorDao.findAccessListByObjId(Long.valueOf(objId));
			List<EcAccssModeView> listDeal=new LinkedList<EcAccssModeView>();
			EcAccssModeView tmp=null;
			for (EcAccssMode ecAccssMode : listAll) {
				tmp=new EcAccssModeView();
				BeanUtils.copyProperties(tmp, ecAccssMode);
				listDeal.add(tmp);
				for (EcAccssMode selOne : sellist) {
					if(selOne.getAccssId().compareTo(ecAccssMode.getAccssId())==0) {
						tmp.setLAY_CHECKED(true);
						break;
					}
				}
			}
			respStr=JSONUtil.getGridFastJSON(listDeal.size(), listDeal, null, EXCLUDE_FIELDS);
		}else {
			respStr=JSONUtil.getGridFastJSON(listAll.size(), listAll, null, EXCLUDE_FIELDS);
		}
		return respStr;
	}
	
	
	/**
	 * 根据对象ID+角色，取已分配的对象的操作列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="findAccessByObjIDAndRoleId",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String findAccessByObjIDAndRoleId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String objId=request.getParameter("objId");
		String roleId=request.getParameter("roleId");
		String respStr=null;
		List<EcAccssMode> listAll=ecOperatorDao.findAccessListByObjId(Long.valueOf(objId));
		if(StringUtils.isNotBlank(roleId)) {
			List<EcAccssMode> sellist=ecPermissionDao.findAccessModeList(Long.valueOf(roleId),Long.valueOf(objId));
			List<EcAccssModeView> listDeal=new LinkedList<EcAccssModeView>();
			EcAccssModeView tmp=null;
			for (EcAccssMode ecAccssMode : listAll) {
				tmp=new EcAccssModeView();
				BeanUtils.copyProperties(tmp, ecAccssMode);
				listDeal.add(tmp);
				for (EcAccssMode selOne : sellist) {
					if(selOne.getAccssId().compareTo(ecAccssMode.getAccssId())==0) {
						tmp.setLAY_CHECKED(true);
						break;
					}
				}
			}
			respStr=JSONUtil.getGridFastJSON(listDeal.size(), listDeal, null, EXCLUDE_FIELDS);
		}else {
			respStr=JSONUtil.getGridFastJSON(listAll.size(), listAll, null, EXCLUDE_FIELDS);
		}
		return respStr;
	}
	
	public EcOperatorDao getEcOperatorDao() {
		return ecOperatorDao;
	}
	public void setEcOperatorDao(EcOperatorDao ecOperatorDao) {
		this.ecOperatorDao = ecOperatorDao;
	}

	public EcObjectsDao getEcObjectsDao() {
		return ecObjectsDao;
	}

	public void setEcObjectsDao(EcObjectsDao ecObjectsDao) {
		this.ecObjectsDao = ecObjectsDao;
	}

	public EcPermissionDao getEcPermissionDao() {
		return ecPermissionDao;
	}

	public void setEcPermissionDao(EcPermissionDao ecPermissionDao) {
		this.ecPermissionDao = ecPermissionDao;
	}

	public EcAccssModeDao getEcAccssModeDao() {
		return ecAccssModeDao;
	}

	public void setEcAccssModeDao(EcAccssModeDao ecAccssModeDao) {
		this.ecAccssModeDao = ecAccssModeDao;
	}
}
