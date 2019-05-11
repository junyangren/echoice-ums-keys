package org.echoice.ums.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * The persistent class for the ec_app_info database table.
 * 
 */
@Entity
@Table(name="ec_app_info")
@NamedQuery(name="AppInfo.findAll", query="SELECT e FROM AppInfo e")
public class AppInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="app_id")
	private String appId;

	@Column(name="app_key")
	private String appKey;

	@Column(name="app_name")
	private String appName;

	@Column(name="app_type")
	private String appType;

	@Column(name="browser_types")
	private String browserTypes;

	@Column(name="app_paths")
	private String appPaths;

	@Column(name="icon_path")
	private String iconPath;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="op_time")
	private Date opTime;

	@Column(name="op_user")
	private String opUser;

	@Column(name="taxis")
	private Long taxis;
	
	@Column(name="status")
	private String status;
	
	@Column(name="app_url")
	private String appUrl;
	
	@Column(name="note")
	private String note;
	
	@javax.persistence.Transient
	private Boolean checked=false;

	public AppInfo() {
		
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return this.appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppType() {
		return this.appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppPaths() {
		return appPaths;
	}

	public void setAppPaths(String appPaths) {
		this.appPaths = appPaths;
	}

	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public Date getOpTime() {
		return this.opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getOpUser() {
		return this.opUser;
	}

	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

	public Long getTaxis() {
		return this.taxis;
	}

	public void setTaxis(Long taxis) {
		this.taxis = taxis;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getBrowserTypes() {
		return browserTypes;
	}

	public void setBrowserTypes(String browserTypes) {
		this.browserTypes = browserTypes;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}