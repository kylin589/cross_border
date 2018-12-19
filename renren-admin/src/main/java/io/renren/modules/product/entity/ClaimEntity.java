package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品认领记录表
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-19 00:26:46
 */
@TableName("product_claim")
public class ClaimEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 产品认领id
	 */
	@TableId
	private Long productClaimId;
	/**
	 * 认领产品id
	 */
	private Long productId;
	/**
	 * 产品来源公司id
	 */
	private Long fromDeptId;
	/**
	 * 产品来源公司
	 */
	private String fromDeptName;
	/**
	 * 产品创建人id
	 */
	private Long fromUserId;
	/**
	 * 产品创建人
	 */
	private String fromUserName;
	/**
	 * 产品来源公司id
	 */
	private Long toDeptId;
	/**
	 * 产品来源公司
	 */
	private String toDeptName;
	/**
	 * 产品创建人id
	 */
	private Long toUserId;
	/**
	 * 产品创建人
	 */
	private String toUserName;
	/**
	 * 操作人公司id
	 */
	private Long operatorDeptId;
	/**
	 * 操作人id
	 */
	private Long operatorId;
	/**
	 * 操作人
	 */
	private String operatorName;
	/**
	 * 操作时间
	 */
	private Date operatorTime;

	/**
	 * 设置：产品认领id
	 */
	public void setProductClaimId(Long productClaimId) {
		this.productClaimId = productClaimId;
	}
	/**
	 * 获取：产品认领id
	 */
	public Long getProductClaimId() {
		return productClaimId;
	}
	/**
	 * 设置：认领产品id
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：认领产品id
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：产品来源公司id
	 */
	public void setFromDeptId(Long fromDeptId) {
		this.fromDeptId = fromDeptId;
	}
	/**
	 * 获取：产品来源公司id
	 */
	public Long getFromDeptId() {
		return fromDeptId;
	}
	/**
	 * 设置：产品来源公司
	 */
	public void setFromDeptName(String fromDeptName) {
		this.fromDeptName = fromDeptName;
	}
	/**
	 * 获取：产品来源公司
	 */
	public String getFromDeptName() {
		return fromDeptName;
	}
	/**
	 * 设置：产品创建人id
	 */
	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}
	/**
	 * 获取：产品创建人id
	 */
	public Long getFromUserId() {
		return fromUserId;
	}
	/**
	 * 设置：产品创建人
	 */
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	/**
	 * 获取：产品创建人
	 */
	public String getFromUserName() {
		return fromUserName;
	}
	/**
	 * 设置：产品来源公司id
	 */
	public void setToDeptId(Long toDeptId) {
		this.toDeptId = toDeptId;
	}
	/**
	 * 获取：产品来源公司id
	 */
	public Long getToDeptId() {
		return toDeptId;
	}
	/**
	 * 设置：产品来源公司
	 */
	public void setToDeptName(String toDeptName) {
		this.toDeptName = toDeptName;
	}
	/**
	 * 获取：产品来源公司
	 */
	public String getToDeptName() {
		return toDeptName;
	}
	/**
	 * 设置：产品创建人id
	 */
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	/**
	 * 获取：产品创建人id
	 */
	public Long getToUserId() {
		return toUserId;
	}
	/**
	 * 设置：产品创建人
	 */
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	/**
	 * 获取：产品创建人
	 */
	public String getToUserName() {
		return toUserName;
	}
	/**
	 * 设置：操作人公司id
	 */
	public void setOperatorDeptId(Long operatorDeptId) {
		this.operatorDeptId = operatorDeptId;
	}
	/**
	 * 获取：操作人公司id
	 */
	public Long getOperatorDeptId() {
		return operatorDeptId;
	}
	/**
	 * 设置：操作人id
	 */
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	/**
	 * 获取：操作人id
	 */
	public Long getOperatorId() {
		return operatorId;
	}
	/**
	 * 设置：操作人
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	/**
	 * 获取：操作人
	 */
	public String getOperatorName() {
		return operatorName;
	}
	/**
	 * 设置：操作时间
	 */
	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}
	/**
	 * 获取：操作时间
	 */
	public Date getOperatorTime() {
		return operatorTime;
	}
}
