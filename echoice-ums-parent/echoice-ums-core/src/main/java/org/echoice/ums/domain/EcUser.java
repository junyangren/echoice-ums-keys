package org.echoice.ums.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * EcUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ec_user")
@TableGenerator(name="ums_ec_user_gen", 
		table="ec_common_seq",
		pkColumnName="pk_key",
		pkColumnValue="ums_ec_user",
		valueColumnName="pk_value",
		allocationSize=1,initialValue=1)
public class EcUser implements java.io.Serializable {

	// Fields

	private Long userId;
	private String name;
	private String alias;
	private String password;
	private String email;
	private String status;
	private String note;
	private Long taxis;
	private Date opTime;
	
	private String mobile;
	private String tel;
	private String address;
	private String qq;
	private String wechat;
	private String duty;
	private Long leaderId;
	private String idcard;
	
	private List<EcGroup> groupList=new ArrayList<EcGroup>();  
	private Set<EcUserGroup> ecUserGroups = new HashSet<EcUserGroup>(0);
	private Set<EcUsersAssignmen> ecUsersAssignmens = new HashSet<EcUsersAssignmen>(
			0);
	private String jobNumber;//工号
	// Constructors

	/** default constructor */
	public EcUser() {
	}

	/** full constructor */
	public EcUser(String name, String alias, String password, String email,
			String status, String note, Long taxis, Date opTime,
			Set<EcUserGroup> ecUserGroups,
			Set<EcUsersAssignmen> ecUsersAssignmens) {
		this.name = name;
		this.alias = alias;
		this.password = password;
		this.email = email;
		this.status = status;
		this.note = note;
		this.taxis = taxis;
		this.opTime = opTime;
		this.ecUserGroups = ecUserGroups;
		this.ecUsersAssignmens = ecUsersAssignmens;
	}

	// Property accessors
	@Id
	//@GeneratedValue
	@GeneratedValue(strategy=GenerationType.TABLE,generator="ums_ec_user_gen")
	@Column(name = "user_id", unique = true, nullable = false)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "alias", length = 50)
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "password", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "status", length = 4)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "note", length = 200)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "taxis")
	public Long getTaxis() {
		return this.taxis;
	}

	public void setTaxis(Long taxis) {
		this.taxis = taxis;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "op_time", length = 0)
	public Date getOpTime() {
		return this.opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ecUser")
	public Set<EcUserGroup> getEcUserGroups() {
		return this.ecUserGroups;
	}

	public void setEcUserGroups(Set<EcUserGroup> ecUserGroups) {
		this.ecUserGroups = ecUserGroups;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ecUser")
	public Set<EcUsersAssignmen> getEcUsersAssignmens() {
		return this.ecUsersAssignmens;
	}

	public void setEcUsersAssignmens(Set<EcUsersAssignmen> ecUsersAssignmens) {
		this.ecUsersAssignmens = ecUsersAssignmens;
	}
	@Transient
	public List<EcGroup> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<EcGroup> groupList) {
		this.groupList = groupList;
	}
	@Column(name = "JOB_NUMBER", length = 64)
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@Column(name = "mobile", length = 30)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "tel", length = 20)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "address", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "qq", length = 20)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "wechat", length = 50)
	public String getWechat() {
		return this.wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	@Column(name = "duty", length = 50)
	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	@Column(name = "leader_id")
	public Long getLeaderId() {
		return this.leaderId;
	}

	public void setLeaderId(Long leaderId) {
		this.leaderId = leaderId;
	}

	@Column(name = "idcard")
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
	

}