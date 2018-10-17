package org.echoice.ums.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.echoice.ums.domain.UserCakey;
import org.echoice.ums.service.UserCakeyService;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.UmsHolder;
import org.echoice.ums.web.view.MsgTipExt;
import org.echoice.ums.web.view.UserCakeyReportView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
/**
* 描述：用户KYE信息表控制层
* @author wujy
* @date 2018/10/01
*/
@Controller
@RequestMapping(value = "/console/userCakey")
public class UserCakeyController{
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	private static final String PAGE_SIZE = "20";
	@Autowired
	private UserCakeyService userCakeyService;
	
	@RequestMapping(value = "index")
	public String index(){
		return "userCakey/index";
	}

    @RequestMapping(value = "searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,UserCakey searchForm,ServletRequest request) {
    	if(StringUtils.isBlank(searchForm.getIdcard())) {
    		searchForm.setIdcard("-1");
    	}
        Page<UserCakey> page=userCakeyService.getUserCakeyDao().findPageList(searchForm, pageNumber, pageSize);
        String respStr=JSONUtil.getGridFastJSON(page.getTotalElements(), page.getContent(), null, null);
        logger.debug("respStr:{}",respStr);
        return respStr;
    }
    
    @RequestMapping(value = "searchAdvancedJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String searchAdvancedJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,UserCakey searchForm,ServletRequest request) {
        Page<UserCakey> page=userCakeyService.getUserCakeyDao().findAdvancedPageList(searchForm, pageNumber, pageSize);
        String respStr=JSONUtil.getGridFastJSON(page.getTotalElements(), page.getContent(), null, null);
        logger.debug("respStr:{}",respStr);
        return respStr;
    }
	
	@RequestMapping(value = "save",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String save(@Valid UserCakey userCakey,ServletRequest request) {
		MsgTipExt msgTip=new MsgTipExt();
		try{
			Date now=new Date();
			long count=userCakeyService.getUserCakeyDao().countByIdcardAndHardwareSn(userCakey.getIdcard(), userCakey.getHardwareSn());
			
			if(count>0) {//存在不添加
				msgTip.setCode(4002);
				msgTip.setMsg(String.format("[%s]已经存在", userCakey.getHardwareSn()));
			}else {
				userCakey.setCreateUser(UmsHolder.getUserAlias());
				userCakey.setCreateTime(now);
				userCakey.setOpUser(UmsHolder.getUserAlias());
				userCakey.setOpTime(now);
				userCakeyService.getUserCakeyDao().save(userCakey);
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("异常：",e);
			msgTip.setCode(4002);
			msgTip.setMsg("异常："+e.getMessage());
		}
		String respStr=JSON.toJSONString(msgTip);
		return respStr;
	}
	
	@RequestMapping(value = "update",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String update(ServletRequest request) {
		MsgTipExt msgTip=new MsgTipExt();
		String updateStatus=request.getParameter("updateStatus");
		String userId=request.getParameter("userId");
		String userCaKeys=request.getParameter("userCaKeys");
		List<UserCakey> list=JSON.parseArray(userCaKeys, UserCakey.class);
		try{
			msgTip=userCakeyService.saveForOptKeys(Long.valueOf(userId),list,updateStatus);
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("异常：",e);
			msgTip.setCode(4002);
			msgTip.setMsg("异常："+e.getMessage());
		}
		String respStr=JSON.toJSONString(msgTip);
		return respStr;
	}
	
	@RequestMapping(value = "issueByDept",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String issueByDept(UserCakey userCakey,ServletRequest request) {
		MsgTipExt msgTip=new MsgTipExt();
		try{
			msgTip=userCakeyService.saveIssueByDept(userCakey);
			//msgTip.setData("00001");
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("异常：",e);
			msgTip.setCode(4002);
			msgTip.setMsg("异常："+e.getMessage());
		}
		String respStr=JSON.toJSONString(msgTip);
		return respStr;
	}
	
	@RequestMapping(value = "countByStatus",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String countByStatus(HttpServletRequest request) {
		MsgTipExt msgTip=new MsgTipExt();
		List<UserCakeyReportView> dataList=userCakeyService.getUserCakeyDao().countByStatus();
		msgTip.setData(dataList);
		String respStr=JSON.toJSONString(msgTip);
		return respStr;
	}	
	
}
