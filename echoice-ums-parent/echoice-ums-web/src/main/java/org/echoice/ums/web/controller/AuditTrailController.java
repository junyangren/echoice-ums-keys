package org.echoice.ums.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.echoice.modules.web.paper.PageBean;
import org.echoice.ums.dao.AuditTrailDao;
import org.echoice.ums.domain.AuditTrailEntity;
import org.echoice.ums.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/console/auditTrail")
public class AuditTrailController extends UmsBaseController {
	private static final String PAGE_SIZE="20";
	@Autowired
	private AuditTrailDao auditTrailDao;

	@RequestMapping("index")
	public String index() throws Exception {
		// TODO Auto-generated method stub
		return "auditTrail/index";
	}	
	
	@RequestMapping(value="searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,AuditTrailEntity searchForm,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		PageBean pageBean=this.auditTrailDao.findPageCondition(searchForm, pageNumber, pageSize);	
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList());
		
		return respStr;
	}

	public AuditTrailDao getAuditTrailDao() {
		return auditTrailDao;
	}

	public void setAuditTrailDao(AuditTrailDao auditTrailDao) {
		this.auditTrailDao = auditTrailDao;
	}
	

	
}
