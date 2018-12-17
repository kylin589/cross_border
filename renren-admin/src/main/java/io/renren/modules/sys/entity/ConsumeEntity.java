package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 消费记录
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 17:36:01
 */
@TableName("company_consume")
public class ConsumeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 公司消费id
	 */
	@TableId
	private Long companyConsumeId;
	/**
	 * 公司id
	 */
	private Long deptId;
	/**
	 * 公司名称
	 */
	private String deptName;
	/**
	 * 操作人id
	 */
	private Long userId;
	/**
	 * 操作人
	 */
	private String userName;
	/**
	 * 类型（服务费、物流费）
	 */
	private String type;
	/**
	 * 订单id
	 */
	private Long orderId;
	/**
	 * 金额
	 */
	private BigDecimal money;
	/**
	 * 消费前余额
	 */
	private BigDecimal beforeBalance;
	/**
	 * 消费后余额
	 */
	private BigDecimal afterBalance;
	/**
	 * 运单号
	 */
	private String abroadWaybill;
	/**
	 * 操作时间
	 */
	private Date createTime;

	/**
	 * 设置：公司消费id
	 */
	public void setCompanyConsumeId(Long companyConsumeId) {
		this.companyConsumeId = companyConsumeId;
	}
	/**
	 * 获取：公司消费id
	 */
	public Long getCompanyConsumeId() {
		return companyConsumeId;
	}
	/**
	 * 设置：公司id
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：公司id
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：公司名称
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * 获取：公司名称
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * 设置：操作人id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：操作人id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：操作人
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：操作人
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：类型（服务费、物流费）
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型（服务费、物流费）
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：订单id
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：订单id
	 */
	public Long getOrderId() {
		return orderId;
	}
	/**
	 * 设置：金额
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：金额
	 */
	public BigDecimal getMoney() {
		return money;
	}
	/**
	 * 设置：消费前余额
	 */
	public void setBeforeBalance(BigDecimal beforeBalance) {
		this.beforeBalance = beforeBalance;
	}
	/**
	 * 获取：消费前余额
	 */
	public BigDecimal getBeforeBalance() {
		return beforeBalance;
	}
	/**
	 * 设置：消费后余额
	 */
	public void setAfterBalance(BigDecimal afterBalance) {
		this.afterBalance = afterBalance;
	}
	/**
	 * 获取：消费后余额
	 */
	public BigDecimal getAfterBalance() {
		return afterBalance;
	}
	/**
	 * 设置：运单号
	 */
	public void setAbroadWaybill(String abroadWaybill) {
		this.abroadWaybill = abroadWaybill;
	}
	/**
	 * 获取：运单号
	 */
	public String getAbroadWaybill() {
		return abroadWaybill;
	}
	/**
	 * 设置：操作时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：操作时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
