package org.echoice.ums.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="ecums")
public class ConfigBean {
	private boolean auth;
	private String authObject;
	private String authAccessMode;
	private String authSysMgrRole;
	private int groupModeType;
	private String groupRoleParentAlias;
	private boolean levelRoleShow=false;
	private int groupAliasCreate;
	private boolean syncGroupPath;
		
	private Map<String,String> urlToObjMap;
	private Map<String,String> objAccessModeMap;
	
	private String uploadPath="c:/";
	
	private String iconPath="c:/icon/";
	
	private String faceImagePath="c:/icon/";
	
	private String fingerprintPath="c:/icon/";
	
	private String serverPrefix="http://localhost:8081/";
	
	private String casUrlPrefix;
	
	public boolean isAuth() {
		return auth;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	public String getAuthObject() {
		return authObject;
	}
	public void setAuthObject(String authObject) {
		this.authObject = authObject;
	}
	public String getAuthAccessMode() {
		return authAccessMode;
	}
	public void setAuthAccessMode(String authAccessMode) {
		this.authAccessMode = authAccessMode;
	}
	public String getAuthSysMgrRole() {
		return authSysMgrRole;
	}
	public void setAuthSysMgrRole(String authSysMgrRole) {
		this.authSysMgrRole = authSysMgrRole;
	}
	public int getGroupModeType() {
		return groupModeType;
	}
	public void setGroupModeType(int groupModeType) {
		this.groupModeType = groupModeType;
	}
	public String getGroupRoleParentAlias() {
		return groupRoleParentAlias;
	}
	public void setGroupRoleParentAlias(String groupRoleParentAlias) {
		this.groupRoleParentAlias = groupRoleParentAlias;
	}
	public boolean isLevelRoleShow() {
		return levelRoleShow;
	}
	public void setLevelRoleShow(boolean levelRoleShow) {
		this.levelRoleShow = levelRoleShow;
	}
	public int getGroupAliasCreate() {
		return groupAliasCreate;
	}
	public void setGroupAliasCreate(int groupAliasCreate) {
		this.groupAliasCreate = groupAliasCreate;
	}
	public boolean getSyncGroupPath() {
		return syncGroupPath;
	}
	public void setSyncGroupPath(boolean syncGroupPath) {
		this.syncGroupPath = syncGroupPath;
	}
	public Map<String, String> getUrlToObjMap() {
		return urlToObjMap;
	}
	public void setUrlToObjMap(Map<String, String> urlToObjMap) {
		this.urlToObjMap = urlToObjMap;
	}
	public Map<String, String> getObjAccessModeMap() {
		return objAccessModeMap;
	}
	public void setObjAccessModeMap(Map<String, String> objAccessModeMap) {
		this.objAccessModeMap = objAccessModeMap;
	}
	public String getUploadPath() {
		return uploadPath;
	}
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public String getServerPrefix() {
		return serverPrefix;
	}
	public void setServerPrefix(String serverPrefix) {
		this.serverPrefix = serverPrefix;
	}
	public String getCasUrlPrefix() {
		return casUrlPrefix;
	}
	public void setCasUrlPrefix(String casUrlPrefix) {
		this.casUrlPrefix = casUrlPrefix;
	}
	public String getFaceImagePath() {
		return faceImagePath;
	}
	public void setFaceImagePath(String faceImagePath) {
		this.faceImagePath = faceImagePath;
	}
	public String getFingerprintPath() {
		return fingerprintPath;
	}
	public void setFingerprintPath(String fingerprintPath) {
		this.fingerprintPath = fingerprintPath;
	}	
}
