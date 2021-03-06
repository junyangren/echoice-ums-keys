package org.echoice.ums.domain;

import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
* 描述：caKey操作工单模型
* @author test
* @date 2018/10/01
*/
@Entity
@Table(name = "ec_cakey_order")
@NamedQuery(name="CakeyOrder.findAll", query="SELECT a FROM CakeyOrder a")
public class CakeyOrder extends BaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
    /**
    *自增主键ID
    *BIGINT
    */
    @TableGenerator(name="ID_CakeyOrder",
            table="ec_common_seq",
            pkColumnName="pk_key",
            valueColumnName="pk_value",
            pkColumnValue="ec_cakey_order",
            allocationSize=1,
            initialValue=1)
    @GeneratedValue(strategy=GenerationType.TABLE,generator="ID_CakeyOrder")    
    @Id
    @Column(name = "id" ,length=19)
    private Long id;
    
    /**
    *工单号
    *VARCHAR
    */
    @NotBlank(message="工单号不能为空")
    @Size(min=1,max=32,message="工单号1~32个字符")
    @Column(name = "order_id" ,length=32)
    private String orderId;
    
    /**
    *操作数量
    *BIGINT
    */
    @NotNull(message="操作数量不能为空")
    @Column(name = "op_count" ,length=19)
    private Long opCount;
    
    /**
    *操作类型:01,入库;02,发放;03,标记丢失;04,离职归还
    *VARCHAR
    */
    @NotBlank(message="操作类型不能为空")
    @Size(min=1,max=2,message="操作类型1~2个字符")
    @Column(name = "op_type" ,length=2)
    private String opType;
    
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

    /**
    *签名PDF
    *VARCHAR
    */
    @Column(name = "sign_pdf" ,length=256)
    private String signPdf;
    
    @Transient
    private List<CakeyOrderDetail> cakeyOrderDetailList;
    
	public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
	public Long getOpCount() {
        return this.opCount;
    }

    public void setOpCount(Long opCount) {
        this.opCount = opCount;
    }
    
	public String getOpType() {
        return this.opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
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

	public String getSignPdf() {
		return signPdf;
	}

	public void setSignPdf(String signPdf) {
		this.signPdf = signPdf;
	}

	public List<CakeyOrderDetail> getCakeyOrderDetailList() {
		return cakeyOrderDetailList;
	}

	public void setCakeyOrderDetailList(List<CakeyOrderDetail> cakeyOrderDetailList) {
		this.cakeyOrderDetailList = cakeyOrderDetailList;
	}
    
}