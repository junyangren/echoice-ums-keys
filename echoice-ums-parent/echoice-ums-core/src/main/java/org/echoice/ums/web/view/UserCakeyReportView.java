package org.echoice.ums.web.view;

import java.io.Serializable;

public class UserCakeyReportView implements Serializable{
	private String status;
	private Long total;
	private String reportTime;
	
	public UserCakeyReportView() {
	}
	
	public UserCakeyReportView(String status, Long total) {
		this.status = status;
		this.total = total;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
}
