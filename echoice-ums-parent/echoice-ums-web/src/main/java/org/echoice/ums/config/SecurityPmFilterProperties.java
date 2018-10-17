package org.echoice.ums.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="ecums.security")
public class SecurityPmFilterProperties {
	private String isSecurityFilter;
	private String accessModeAlias;
	private String urlTag;
	private String userSecurityObjectsSessionName;
	private List<String> urlPatterns;
	private String filterActions;
	private int order;
	public List<String> getUrlPatterns() {
		return urlPatterns;
	}
	public void setUrlPatterns(List<String> urlPatterns) {
		this.urlPatterns = urlPatterns;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getIsSecurityFilter() {
		return isSecurityFilter;
	}
	public void setIsSecurityFilter(String isSecurityFilter) {
		this.isSecurityFilter = isSecurityFilter;
	}
	public String getAccessModeAlias() {
		return accessModeAlias;
	}
	public void setAccessModeAlias(String accessModeAlias) {
		this.accessModeAlias = accessModeAlias;
	}
	public String getUrlTag() {
		return urlTag;
	}
	public void setUrlTag(String urlTag) {
		this.urlTag = urlTag;
	}
	public String getUserSecurityObjectsSessionName() {
		return userSecurityObjectsSessionName;
	}
	public void setUserSecurityObjectsSessionName(String userSecurityObjectsSessionName) {
		this.userSecurityObjectsSessionName = userSecurityObjectsSessionName;
	}
	public String getFilterActions() {
		return filterActions;
	}
	public void setFilterActions(String filterActions) {
		this.filterActions = filterActions;
	}
}
