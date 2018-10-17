package org.echoice.ums.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.web.MsgTip;
import org.echoice.ums.dao.EcGroupAssignmentDao;
import org.echoice.ums.util.CasUmsUtil;
import org.echoice.ums.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/console/groupAssignmen")
public class GroupAssignmentController extends UmsBaseController {
	@Autowired
	private EcGroupAssignmentDao ecGroupAssignmentDao;
	@RequestMapping(value="assignRole",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String assignRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("组分配角色start:");
		MsgTip msgTip=new MsgTip();
		
		String groupIds=request.getParameter("groupIds");
		String roleIds=request.getParameter("roleIds");
		List<Long> groupIdsList= JSON.parseArray(groupIds, Long.class);
		List<Long> roleIdsList= JSON.parseArray(roleIds, Long.class);
		
		Long[] groupIdArr=groupIdsList.toArray(new Long[groupIdsList.size()]);
		Long[] roleIdsArr=roleIdsList.toArray(new Long[roleIdsList.size()]);
		
		ecGroupAssignmentDao.saveBatch(groupIdArr,roleIdsArr);
		logger.info(CasUmsUtil.getUser(request)+"操作分配角色  groupIdArr:【"+StringUtils.join(groupIdArr,",")+"】"+"roleArr:【"+StringUtils.join(roleIdsArr,",")+"】");
		logger.debug("组分配角色end:");
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	@RequestMapping(value="removeRole",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String removeRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("组移除角色start:");
		MsgTip msgTip=new MsgTip();
		String groupIds=request.getParameter("groupIds");
		String roleIds=request.getParameter("roleIds");
		List<Long> groupIdsList= JSON.parseArray(groupIds, Long.class);
		List<Long> roleIdsList= JSON.parseArray(roleIds, Long.class);
		
		Long[] groupIdArr=groupIdsList.toArray(new Long[groupIdsList.size()]);
		Long[] roleIdsArr=roleIdsList.toArray(new Long[roleIdsList.size()]);
		ecGroupAssignmentDao.removeBatch(groupIdArr,roleIdsArr);
		logger.info(CasUmsUtil.getUser(request)+"操作移除角色  groupIdArr:【"+StringUtils.join(groupIdArr,",")+"】"+"roleArr:【"+StringUtils.join(roleIdsArr,",")+"】");
		logger.debug("组移除角色end:");
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	public EcGroupAssignmentDao getEcGroupAssignmentDao() {
		return ecGroupAssignmentDao;
	}

	public void setEcGroupAssignmentDao(EcGroupAssignmentDao ecGroupAssignmentDao) {
		this.ecGroupAssignmentDao = ecGroupAssignmentDao;
	}

}
