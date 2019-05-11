package org.echoice.ums.web.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.echoice.modules.web.MsgTip;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.domain.AppInfo;
import org.echoice.ums.service.AppInfoService;
import org.echoice.ums.util.AesKey;
import org.echoice.ums.util.CasUmsUtil;
import org.echoice.ums.util.FileUtil;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.view.MsgTipExt;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/console/appinfo")
public class AppInfoController {
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	private static final String PAGE_SIZE = "20";
	private static final AtomicInteger APP_IDX=new AtomicInteger(0);
	
	@Autowired
	private ConfigBean configBean;
	
	@Autowired
	private AppInfoService appInfoService;

	@RequestMapping("index")
	public String index() throws Exception {
		// TODO Auto-generated method stub
		return "appinfo/index";
	}

    @RequestMapping(value = "searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,AppInfo searchForm,ServletRequest request) {
    	if(StringUtils.isBlank(searchForm.getStatus())) {
    		searchForm.setStatus(null);
    	}
        Page<AppInfo> page=this.appInfoService.findPageList(searchForm, pageNumber, pageSize);
        String respStr=JSONUtil.getGridFastJSON(page.getTotalElements(), page.getContent());
        logger.info("respStr:{}",respStr);
        return respStr;
    }
    
	@RequestMapping(value="edit",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String edit(HttpServletRequest request,HttpServletResponse response,AppInfo appInfo) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		AppInfo appInfoDb=this.appInfoService.getAppInfoDao().findOne(appInfo.getId());
		msgTip.setData(appInfoDb);
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	@RequestMapping(value="showIcon",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void showIcon(HttpServletRequest request,HttpServletResponse response,AppInfo appInfo) throws Exception {
		// TODO Auto-generated method stub
		AppInfo appInfoDb=this.appInfoService.getAppInfoDao().findOne(appInfo.getId());
		if(StringUtils.isNotBlank(appInfoDb.getIconPath())) {
			String fileExt=StringUtils.substringAfterLast(appInfoDb.getIconPath(), ".");
			response.setContentType("image/"+fileExt);
			IOUtils.copy(new FileInputStream(appInfoDb.getIconPath()), response.getOutputStream());
		}
	}
	
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String save(HttpServletRequest request,HttpServletResponse response,AppInfo appInfo) throws Exception {
		
		MsgTipExt msgTip=new MsgTipExt();
		Date now=new Date();
		if(appInfo.getId()!=null) {
			AppInfo appInfoDb=this.appInfoService.getAppInfoDao().findOne(appInfo.getId());
			if(StringUtils.isBlank(appInfo.getIconPath())) {
				appInfo.setIconPath(appInfoDb.getIconPath());
			}
			appInfo.setAppId(appInfoDb.getAppId());
			appInfo.setAppKey(appInfoDb.getAppKey());
		}else {
			String appIdSrc=FastDateFormat.getInstance("yyyyMMddHHmmss").format(now)+APP_IDX.incrementAndGet();
			String appId=Hex.encodeHexString(DigestUtils.getMd5Digest().digest(appIdSrc.getBytes()));
			String appKey=AesKey.getKeyByPassHex(appId);
			appInfo.setAppId(appId);
			appInfo.setAppKey(appKey);
		}
		appInfo.setOpTime(now);
		appInfo.setOpUser(CasUmsUtil.getUser(request));
		this.appInfoService.getAppInfoDao().save(appInfo);
		msgTip.setData(appInfo);
		logger.info(CasUmsUtil.getUser(request)+" 应用操作："+ appInfo.getAppId()+"，"+appInfo.getAppName());
		String respStr=JSONUtil.toJSONString(msgTip);
		
		return respStr;
	}
	
	@RequestMapping(value="del",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String del(HttpServletRequest request,HttpServletResponse response,@RequestParam String selIds) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		List<Long> idsArr=JSON.parseArray(selIds, Long.class);
		if(idsArr!=null&&idsArr.size()>0) {
			this.appInfoService.del(idsArr);
		}else {
			msgTip.setCode(4002);
			msgTip.setMsg("未找到记录");
		}
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}	
	
	/**
	 * 保存文件到服务器
	 * @param linkPerson
	 * @param file
	 * @param request
	 * @return 返回保存的文件路径 以及列数
	 */
	@RequestMapping(value = "fileUpload",method = RequestMethod.POST)
	@ResponseBody
	public MsgTipExt fileUpload(MultipartFile file,HttpServletRequest request,HttpServletResponse resp) {
		MsgTipExt msgTip=new MsgTipExt();
		msgTip.setMsg("文件上传成功");
		String originalFilename = file.getOriginalFilename();
		try {
			String filePath=FileUtil.saveFile(originalFilename,this.configBean.getIconPath(), file.getInputStream());
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("filePath", filePath);
			msgTip.setData(map);
		} catch (IOException e) {
			logger.error("文件上传失败：",e);
			msgTip.setCode(4002);
			msgTip.setMsg("文件上传失败："+e.getMessage());
		}
		return msgTip;
	}
	
	
	
}
