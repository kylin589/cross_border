package io.renren.modules.order.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单备注、操作日志表
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-11 16:32:11
 */
@TableName("order_remark")
public class RemarkEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 备注id
	 */
	@TableId
	private Long remarkId;
	/**
	 * 订单id
	 */
	private Long orderId;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 操作人
	 */
	private String userName;
	/**
	 * 操作时间
	 */
	private Date updateTime;
	/**
	 * 类型（remark，log）
	 */
	private String type;
	/**
	 * 备注类型（内部备注、收件人地址异常.......）
	 */
	private String remarkType;

	/**
	 * 设置：备注id
	 */
	public void setRemarkId(Long remarkId) {
		this.remarkId = remarkId;
	}
	/**
	 * 获取：备注id
	 */
	public Long getRemarkId() {
		return remarkId;
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
	 * 获取：操作人
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：操作人
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 设置：操作时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：操作时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：类型（remark，log）
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型（remark，log）
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：备注类型（内部备注、收件人地址异常.......）
	 */
	public void setRemarkType(String remarkType) {
		this.remarkType = remarkType;
	}
	/**
	 * 获取：备注类型（内部备注、收件人地址异常.......）
	 */
	public String getRemarkType() {
		return remarkType;
	}
}
