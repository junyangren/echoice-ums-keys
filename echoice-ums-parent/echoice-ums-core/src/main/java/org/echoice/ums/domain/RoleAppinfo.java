package org.echoice.ums.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ec_role_appinfo database table.
 * 
 */
@Entity
@Table(name="ec_role_appinfo")
@NamedQuery(name="RoleAppinfo.findAll", query="SELECT e FROM RoleAppinfo e")
public class RoleAppinfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="app_id")
	private Long appId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="op_time")
	private Date opTime;

	@Column(name="role_id")
	private Long roleId;

	public RoleAppinfo() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppId() {
		return this.appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Date getOpTime() {
		return this.opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}