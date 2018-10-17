package org.echoice.ums.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
* 描述：test模型
* @author test
* @date 2018/10/01
*/
@Entity
@Table(name = "ec_user_cakey")
@NamedQuery(name="UserCakey.findAll", query="SELECT a FROM UserCakey a")
public class UserCakey extends BaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
    /**
    *自增主键ID
    *BIGINT
    */
    @TableGenerator(name="ID_UserCakey",
            table="ec_common_seq",
            pkColumnName="pk_key",
            valueColumnName="pk_value",
            pkColumnValue="ec_user_cakey",
            allocationSize=1,
            initialValue=1)
    @GeneratedValue(strategy=GenerationType.TABLE,generator="ID_UserCakey")    
    @Id
    @Column(name = "id" ,length=19)
    private Long id;
    
    /**
    *身份证号
    *VARCHAR
    */
    @NotBlank(message="身份证号不能为空")
    @Size(min=1,max=32,message="身份证号1~32个字符")
    @Column(name = "idcard" ,length=32)
    private String idcard;
    
    /**
    *key硬件介质SN
    *VARCHAR
    */
    @NotBlank(message="key硬件介质SN不能为空")
    @Size(min=1,max=32,message="key硬件介质SN1~32个字符")
    @Column(name = "hardware_sn" ,length=32)
    private String hardwareSn;
    
    /**
    *状态:01,未领取;02,已领取;03,已丢失;04,离职归还
    *VARCHAR
    */
    @NotBlank(message="状态不能为空")
    @Size(min=1,max=2,message="状态1~2个字符")
    @Column(name = "status" ,length=2)
    private String status;
    
    /**
    *创建时间
    *DATETIME
    */
	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
    @Column(name = "create_time" ,length=19)
    private Date createTime;
    
    /**
    *创建用户
    *VARCHAR
    */
    @Column(name = "create_user" ,length=64)
    private String createUser;
    
    /**
    *修改时间
    *DATETIME
    */
	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
    @Column(name = "op_time" ,length=19)
    private Date opTime;
    
    /**
    *修改用户
    *VARCHAR
    */
    @Column(name = "op_user" ,length=64)
    private String opUser;
    
    
	public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
    
	public String getHardwareSn() {
        return this.hardwareSn;
    }

    public void setHardwareSn(String hardwareSn) {
        this.hardwareSn = hardwareSn;
    }
    
	public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
	public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
	public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
    
}