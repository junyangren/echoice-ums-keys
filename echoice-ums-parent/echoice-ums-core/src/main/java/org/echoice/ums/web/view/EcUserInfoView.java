package org.echoice.ums.web.view;

import org.echoice.ums.domain.EcUser;

public class EcUserInfoView extends EcUser{
	private String groupName;
	private Long groupId;
	private String groupIds;
	private String groupNames;
	private String confirmPassword;
	private String hardwareSn;//
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
	public String getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}
	public String getConfirmPassword() {
		
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getHardwareSn() {
		return hardwareSn;
	}
	public void setHardwareSn(String hardwareSn) {
		this.hardwareSn = hardwareSn;
	}

}
