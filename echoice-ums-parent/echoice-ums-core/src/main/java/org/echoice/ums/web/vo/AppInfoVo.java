package org.echoice.ums.web.vo;

public class AppInfoVo implements Comparable<AppInfoVo>{
	private String appId;
	private String appName;
	private String loginUrl;
	private String browserTypes;
	private String iconUrl;
	private String appPaths;
	private Long taxis;
	
	@Override
	public int compareTo(AppInfoVo o) {
		// TODO Auto-generated method stub
		if(this.getTaxis().longValue()<o.getTaxis().longValue()){
			return -1;
		}else{
			return 1;
		}
	}
	
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public String getBrowserTypes() {
		return browserTypes;
	}
	public void setBrowserTypes(String browserTypes) {
		this.browserTypes = browserTypes;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getAppPaths() {
		return appPaths;
	}
	public void setAppPaths(String appPaths) {
		this.appPaths = appPaths;
	}
	public Long getTaxis() {
		return taxis;
	}
	public void setTaxis(Long taxis) {
		this.taxis = taxis;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}	
	
	
}
