package org.echoice.ums.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.cas.CasUtil;
import org.echoice.modules.web.controller.SpringBaseController;
import org.echoice.modules.web.json.JSONUtil;
import org.echoice.modules.web.ztree.ZTreeView;
import org.echoice.ums.dao.UmsClientDao;
import org.echoice.ums.domain.EcGroup;
import org.echoice.ums.domain.EcObjects;
import org.echoice.ums.domain.EcUser;
import org.echoice.ums.web.UmsHolder;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 应用常用获取菜单树、用户组树、用户组下用信息
 * @author wujy
 *
 */
public class UmsAppController extends SpringBaseController {
	private UmsClientDao umsClientDao;
	private String appAccessAlias="view";
	@Override
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	public ModelAndView tree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id=request.getParameter("id");
		logger.debug("tree id:"+id);
		if(StringUtils.isBlank(id)){
			String rootId=request.getParameter("rootId");
			if(StringUtils.isNotBlank(rootId)){
				id=rootId;
			}else{
				id="-1";
			}
		}
		String userAlias=CasUtil.getUserName();
		List<Long> parentList=getUmsClientDao().findAssignPermissionObjectParent(userAlias, getAppAccessAlias());
		StringBuilder strBuilder=new StringBuilder();
		strBuilder.append("|");
		for (Long parentId : parentList) {
			strBuilder.append(parentId+"|");
		}
		String parentStr=strBuilder.toString();
		
		List<EcObjects> curlist=getUmsClientDao().findAssignPermissionObjectList(userAlias, getAppAccessAlias(), Long.valueOf(id));
		List<ZTreeView> zTreelist=new ArrayList<ZTreeView>();
		String title=null;
		for (EcObjects tmpObj : curlist) {
			String tmpId=String.valueOf(tmpObj.getObjId());
			boolean isParent=false;
			if(StringUtils.indexOf(parentStr, "|"+tmpId+"|")!=-1){
				isParent=true;
			}
			
			ZTreeView treeView=new ZTreeView();
			treeView.setId(tmpId);
			treeView.setName(tmpObj.getName());
			treeView.setIsParent(isParent);
			treeView.setExtNote(tmpObj.getNote());
			treeView.setAlias(tmpObj.getAlias());
			treeView.setTitle(tmpObj.getName());
			//treeView.setOpen(true);

			//转json对象
			if(StringUtils.isNotBlank(tmpObj.getNote())){
				try {
					JSONObject object=JSON.parseObject(tmpObj.getNote());
					treeView.setExtUrl(object.getString("url"));
					treeView.setTarget(object.getString("target"));
					treeView.setReload(object.getBooleanValue("reload"));
					title=object.getString("title");
					if(StringUtils.isNotBlank(title)){
						treeView.setTitle(title);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			zTreelist.add(treeView);
		}
		//logger.info(JSONUtil.toJSONString(zTreelist));
		rendJson(response, JSONUtil.toJSONString(zTreelist));
		//rendText(response, JSONUtil.toJSONString(zTreelist));
		return null;
		
	}
	
	public ModelAndView groupTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		if(StringUtils.isBlank(id)){
			String rootId=request.getParameter("rootId");
			if(StringUtils.isNotBlank(rootId)){
				id=rootId;
			}else{
				id="-1";
			}
		}
		List<Long> parentList=getUmsClientDao().findParenGroup();
		StringBuilder strBuilder=new StringBuilder();
		strBuilder.append("|");
		for (Long parentId : parentList) {
			strBuilder.append(parentId+"|");
		}
		String parentStr=strBuilder.toString();
		
		List<EcGroup> curlist=getUmsClientDao().findChildGroup(Long.valueOf(id));
		List<ZTreeView> zTreelist=new ArrayList<ZTreeView>();
		for (EcGroup ecGroup : curlist) {
			String tmpId=String.valueOf(ecGroup.getGroupId());
			boolean isParent=false;
			if(StringUtils.indexOf(parentStr, "|"+tmpId+"|")!=-1){
				isParent=true;
			}
			ZTreeView treeView=new ZTreeView();
			treeView.setId(tmpId);
			treeView.setName(ecGroup.getName());
			treeView.setIsParent(isParent);
			treeView.setExtNote(ecGroup.getNote());
			treeView.setAlias(ecGroup.getAlias());
			zTreelist.add(treeView);
		}
		rendJson(response, JSONUtil.toJSONString(zTreelist));
		return null;
	}
	
	/**
	 * 根据用户组，查找组下的用户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView findUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		if(StringUtils.isBlank(id)){
			id="-1";
		}
		List<EcUser> list=getUmsClientDao().findUserByGroupId(Long.valueOf(id));
		String content="{\"total\":"+list.size()+",";
		content+="\"rows\":"+JSONUtil.toJSONString(list)+"}";
		rendJson(response, content);
		return null;
	}

	public ModelAndView findPermissionUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String objAlias="aq-menu-order";
		String accessModeAlias="fetchDeliver";
		List<EcUser> list=getUmsClientDao().findPermissionUser(objAlias, accessModeAlias);
		String content="{\"total\":"+list.size()+",";
		content+="\"rows\":"+JSONUtil.toJSONString(list)+"}";
		rendJson(response, content);
		return null;
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//取对象树
		int resulCode=4002;
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			String newPassword=request.getParameter("newPassword");
			String confirmPassword=request.getParameter("confirmPassword");
			String oldPassword=request.getParameter("oldPassword");
			if(StringUtils.isNotBlank(oldPassword)&&StringUtils.isNotBlank(newPassword)&&StringUtils.isNotBlank(confirmPassword)){
				if(StringUtils.equals(newPassword, confirmPassword)){
					boolean isSucess=getUmsClientDao().updateUserPassword(UmsHolder.getUserAlias(), oldPassword, newPassword);
					if(isSucess){
						resulCode=0;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("msg", e.getMessage());
		}
		map.put("code", resulCode);
		rendJson(response, JSONUtil.toJSONString(map));
		return null;
	}
	
	public UmsClientDao getUmsClientDao() {
		return umsClientDao;
	}

	public void setUmsClientDao(UmsClientDao umsClientDao) {
		this.umsClientDao = umsClientDao;
	}

	public String getAppAccessAlias() {
		return appAccessAlias;
	}

	public void setAppAccessAlias(String appAccessAlias) {
		this.appAccessAlias = appAccessAlias;
	}


	
}
