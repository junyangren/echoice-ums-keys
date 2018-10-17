package org.echoice.ums.domain;

import javax.persistence.Transient;

public class BaseEntity {
	@Transient
	private String appFormStartTime;

	@Transient
	private String appFormEndTime;
	
	@Transient
	private String groupName;
	
	@Transient
	private Long groupId;
	
	@Transient
	private String userName;
	
	@Transient
	private String appFormStartPartId;

	@Transient
	private String appFormEndPartId;
	
	@Transient
	private String loginGroupAlias;

	public String getAppFormStartTime() {
		return appFormStartTime;
	}

	public void setAppFormStartTime(String appFormStartTime) {
		this.appFormStartTime = appFormStartTime;
	}

	public String getAppFormEndTime() {
		return appFormEndTime;
	}

	public void setAppFormEndTime(String appFormEndTime) {
		this.appFormEndTime = appFormEndTime;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAppFormStartPartId() {
		return appFormStartPartId;
	}

	public void setAppFormStartPartId(String appFormStartPartId) {
		this.appFormStartPartId = appFormStartPartId;
	}

	public String getAppFormEndPartId() {
		return appFormEndPartId;
	}

	public void setAppFormEndPartId(String appFormEndPartId) {
		this.appFormEndPartId = appFormEndPartId;
	}

	public String getLoginGroupAlias() {
		return loginGroupAlias;
	}

	public void setLoginGroupAlias(String loginGroupAlias) {
		this.loginGroupAlias = loginGroupAlias;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
}
