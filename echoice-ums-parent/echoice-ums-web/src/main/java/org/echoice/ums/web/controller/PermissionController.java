package org.echoice.ums.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.echoice.modules.web.json.ExtJsUtil;
import org.echoice.modules.web.paper.PageBean;
import org.echoice.ums.dao.EcPermissionDao;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.view.GroupPermissionView;
import org.echoice.ums.web.view.MsgTipExt;
import org.echoice.ums.web.view.PermissionView;
import org.echoice.ums.web.view.UserPermissionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
@Controller
@RequestMapping("/console/permission")
public class PermissionController extends UmsBaseController {
	
	private static final String PAGE_SIZE="20";
	
	private static final String[] EXCLUDE_FIELDS=new String[]{"ecOperators"};
	
	@Autowired
	private EcPermissionDao ecPermissionDao;

	/**
	 * 分配角色中的对象+操作权限
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="assign",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String assignPermission(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		String accssIdsStr=request.getParameter("accssIds");
	 	List<Long> accssIds=JSON.parseArray(accssIdsStr, Long.class);
	 	
	 	String roleIdsStr=request.getParameter("roleIds");
	 	List<Long> roleIds=JSON.parseArray(roleIdsStr, Long.class);
	 	
	 	String objIdStr=request.getParameter("objId");
		
		ecPermissionDao.savePermission(roleIds, Long.valueOf(objIdStr), accssIds);
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	/**
	 * 移除角色中的对象+操作权限
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="remove",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String removePermission(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		String accssIdsStr=request.getParameter("accssIds");
	 	List<Long> accssIds=JSON.parseArray(accssIdsStr, Long.class);
	 	
	 	String roleIdsStr=request.getParameter("roleIds");
	 	List<Long> roleIds=JSON.parseArray(roleIdsStr, Long.class);
	 	
	 	String objIdStr=request.getParameter("objId");
	 	
		ecPermissionDao.removePermission(roleIds, Long.valueOf(objIdStr), accssIds);
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	/**
	 * 查看角色拥用哪些对象操作权限
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="showRolePermission",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String showRolePermission(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,PermissionView permissionView) throws Exception {
		// TODO Auto-generated method stub		
		PageBean pageBean=ecPermissionDao.findRolePermissionPage(permissionView, pageNumber, pageSize);
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}
	
	/**
	 * 查看角色分配了哪些用户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="showRoleAssignUser",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String showRoleAssignUser(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String roleId=request.getParameter("roleId");
		String alias=request.getParameter("alias");
		String name=request.getParameter("name");
		PageBean pageBean=ecPermissionDao.findRoleAssingUserPage(Long.valueOf(roleId),alias,name, pageNumber, pageSize);
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}
	
	/**
	 * 将用户从授权角色中移除
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="removeRoleAssignUser",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String removeRoleAssignUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		String uaIdsStr=request.getParameter("ids");
	 	List<Long> uaIds=JSON.parseArray(uaIdsStr, Long.class);
		
		ecPermissionDao.removeRoleAssignUserByUaIdsArr(uaIds);
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	/**
	 * 查看用户分配的权限
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="showUserPermission",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String showUserPermission(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,UserPermissionView permissionView
            ,HttpServletRequest request,HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		PageBean pageBean=ecPermissionDao.findUserPermissionPage(permissionView, pageNumber, pageSize);
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}
	/**
	 * 根据对象ID+操作ID，查看权限分配的角色
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="showPermissionRoleList",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String showPermissionRoleList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String objId=request.getParameter("objId");
		Long[] idsAccessLongArr=ExtJsUtil.transJsonIDArrayToLong(request, "accessIds");
		List list=ecPermissionDao.showPermissionRoleList(Long.valueOf(objId),idsAccessLongArr);
		String respStr=JSONUtil.getGridFastJSON(list.size(), list,null, EXCLUDE_FIELDS);
		return respStr;
	}
	
	/**
	 * 查看用户组拥用的权限
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="showGroupPermission",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String showGroupPermission(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,GroupPermissionView permissionView,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String groupId=request.getParameter("groupId");	
		PageBean pageBean=ecPermissionDao.findGroupPermissionPage(permissionView, pageNumber, pageSize);
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}
	
	/**
	 * 根据PaId,将权限从角色中移除
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="removePermissionByPaId",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String removePermissionByPaId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();	
		Long[] paIds=ExtJsUtil.transJsonIDArrayToLong(request, "ids");
		ecPermissionDao.removePermissionByPaIdsArr(paIds);
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	public EcPermissionDao getEcPermissionDao() {
		return ecPermissionDao;
	}

	public void setEcPermissionDao(EcPermissionDao ecPermissionDao) {
		this.ecPermissionDao = ecPermissionDao;
	}
	
}
