package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-17 11:09:16
 */
@TableName("company_recharge")
public class RechargeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 公司充值id
	 */
	@TableId
	private Long companyRechargeId;
	/**
	 * 操作人
	 */
	private String userName;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 余额
	 */
	private BigDecimal balance;
	/**
	 * 充值时间
	 */
	private Date rechargeTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 公司id
	 */
	private Long deptId;

	/**
	 * 设置：公司充值id
	 */
	public void setCompanyRechargeId(Long companyRechargeId) {
		this.companyRechargeId = companyRechargeId;
	}
	/**
	 * 获取：公司充值id
	 */
	public Long getCompanyRechargeId() {
		return companyRechargeId;
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
	 * 设置：类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	/**
	 * 获取：余额
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	/**
	 * 设置：充值时间
	 */
	public void setRechargeTime(Date rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
	/**
	 * 获取：充值时间
	 */
	public Date getRechargeTime() {
		return rechargeTime;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
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
}
