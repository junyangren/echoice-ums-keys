package org.echoice.ums.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.dao.EcUserDao;
import org.echoice.ums.dao.UmsClientDao;
import org.echoice.ums.domain.AppInfo;
import org.echoice.ums.domain.EcObjects;
import org.echoice.ums.util.FileUtil;
import org.echoice.ums.web.vo.AppInfoVo;
import org.echoice.ums.web.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/api")
public class RestApiController {
	private Logger logger=LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	private UmsClientDao umsClientDao;
	
	@Autowired
	private EcUserDao ecUserDao;
	
	@Autowired
	private ConfigBean configBean;
	
	/**
	 * 获取用户授权的应用列表记录
	 * @param tgtId
	 * @return
	 */
	@RequestMapping(value="v1/findAppList",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Result findAppList(@RequestParam(name="tgtId",required=true) String tgtId) {
		String userAlias=getOpUserAlias(tgtId);
		if(StringUtils.isBlank(userAlias)) {
			return Result.fail("用户认证失败");
		}
		AppInfoVo appInfoVo=null;
		Map<String, List<AppInfoVo>> map=Maps.newHashMap();
		List<AppInfoVo> list=null;
		Result r=Result.success();
		List<AppInfo> dblist=umsClientDao.findAssignApplist(userAlias);
		String iconUrl=null;
		String browserTypes=null;
		String appPaths=null;
		for (AppInfo appInfo : dblist) {
			list=map.get(appInfo.getAppType());
			if(list==null) {
				list=Lists.newLinkedList();
				map.put(appInfo.getAppType(), list);
			}
			
			appInfoVo=new AppInfoVo();
			appInfoVo.setAppId(appInfo.getAppId());
			appInfoVo.setAppName(appInfo.getAppName());
			iconUrl=StringUtils.substringAfter(appInfo.getIconPath(), configBean.getIconPath());
			iconUrl=configBean.getServerPrefix()+iconUrl;
			appInfoVo.setIconUrl(iconUrl);
			appInfoVo.setLoginUrl(appInfo.getAppUrl());
			browserTypes=StringUtils.join(JSON.parseArray(appInfo.getBrowserTypes(), String.class), ",");
			appInfoVo.setBrowserTypes(browserTypes);
			appPaths=StringUtils.join(JSON.parseArray(appInfo.getAppPaths(), String.class), ",");
			appInfoVo.setAppPaths(appPaths);
			appInfoVo.setTaxis(appInfo.getTaxis());
			list.add(appInfoVo);
		}
		for (String key : map.keySet()) {
			list=map.get(key);
			Collections.sort(list);
			r.addData(key, list);
		}
		return r;
	}
	
	@RequestMapping(value="v01/findAppList",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Result findAppListv01(@RequestParam(name="tgtId",required=true) String tgtId) {
		String userAlias=getOpUserAlias(tgtId);
		if(StringUtils.isBlank(userAlias)) {
			return Result.fail("用户认证失败");
		}
		
		Result r=Result.success();
		String accessAlias="viewApp";
		List<EcObjects> allAppList=umsClientDao.findAssignPermissionObjectList(userAlias,accessAlias);
		AppInfoVo appInfoVo=null;
		Map<String, List<AppInfoVo>> map=Maps.newHashMap();
		
		List<AppInfoVo> list=null;
		String iconUrl=null;
		for (EcObjects ecObjects : allAppList) {
			list=map.get(ecObjects.getType());
			if(list==null) {
				list=Lists.newLinkedList();
				map.put(ecObjects.getType(), list);
			}
			appInfoVo=new AppInfoVo();
			appInfoVo.setAppId(ecObjects.getAlias());
			appInfoVo.setAppName(ecObjects.getName());
			iconUrl=StringUtils.substringAfter(ecObjects.getIcon(), configBean.getIconPath());
			iconUrl=configBean.getServerPrefix()+iconUrl;
			appInfoVo.setIconUrl(iconUrl);
			appInfoVo.setLoginUrl(ecObjects.getNote());
			appInfoVo.setBrowserTypes(ecObjects.getNote2());
			appInfoVo.setAppPaths(ecObjects.getNote3());
			appInfoVo.setTaxis(ecObjects.getTaxis());
			list.add(appInfoVo);
		}
		
		for (String key : map.keySet()) {
			list=map.get(key);
			Collections.sort(list);
			r.addData(key, list);
		}
		return r;
	}
	
	@RequestMapping(value="v1/updateFaceImg",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Result updateFaceImg(@RequestParam(name="tgtId",required=true) String tgtId,
			@RequestParam(name="username",required=true) String username,
			@RequestParam(name="faceImg",required=true) String faceImg,
			@RequestParam(name="fileType",required=true) String fileType) {
		String opUserAlias=getOpUserAlias(tgtId);
		if(StringUtils.isBlank(opUserAlias)) {
			return Result.fail("用户认证失败");
		}
		//
		byte faceImgBytes[]=Base64.decodeBase64(faceImg);
		ByteArrayInputStream in=new ByteArrayInputStream(faceImgBytes);
		String filePath=null;
		try {
			filePath=FileUtil.saveFile2(fileType, this.configBean.getFaceImagePath(), in);
			ecUserDao.updateFaceImg(username, filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Result.fail("人像上传异常！");
		}
		
		return Result.success();
	}
	
	
	@RequestMapping(value="v1/updateFingerprint",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Result updateFingerprint(@RequestParam(name="tgtId",required=true) String tgtId,
			@RequestParam(name="username",required=true) String username,
			@RequestParam(name="fingerprint",required=true) String fingerprint,
			@RequestParam(name="fileType",required=true) String fileType) {
		String opUserAlias=getOpUserAlias(tgtId);
		if(StringUtils.isBlank(opUserAlias)) {
			return Result.fail("用户认证失败");
		}
		//
		byte faceImgBytes[]=Base64.decodeBase64(fingerprint);
		ByteArrayInputStream in=new ByteArrayInputStream(faceImgBytes);
		String filePath=null;
		try {
			filePath=FileUtil.saveFile2(fileType, this.configBean.getFingerprintPath(), in);
			ecUserDao.updateFaceImg(username, filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Result.fail("指纹上传异常！");
		}
		
		return Result.success();
	}
	
	private String getOpUserAlias(String tgtId) {
		String url=this.configBean.getCasUrlPrefix()+"/rest/v1/user/"+tgtId;
		logger.info("casUrl:{}",url);
		RestTemplate restTemplate=new RestTemplate();
		try {
			ResponseEntity<String> responseEntity=restTemplate.getForEntity(url, String.class);
			if(responseEntity.getStatusCode()==HttpStatus.OK) {
				return responseEntity.getBody();
			}
		}catch (RestClientException e) {
			// TODO: handle exception
			logger.error("RestClientException:{}",e.getMessage());
		}
		return null;
	}
}
