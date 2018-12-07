package io.renren.modules.logistics.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-03 11:46:03
 */
@TableName("order_domestic_logistics")
public class DomesticLogisticsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 国内物流id
	 */
	@TableId
	private Long domesticLogisticsId;
	/**
	 * 订单id
	 */
	private Long orderId;
	/**
	 * 运单号
	 */
	private String waybill;
	/**
	 * 物流公司
	 */
	private String logisticsCompany;
	/**
	 * 发货日期
	 */
	private Date issuanceDate;
	/**
	 * 
	 */
	private Long userId;
	/**
	 * 
	 */
	private Long deptId;
	/**
	 * 
	 */
	private Date createDate;

	/**
	 * 设置：国内物流id
	 */
	public void setDomesticLogisticsId(Long domesticLogisticsId) {
		this.domesticLogisticsId = domesticLogisticsId;
	}
	/**
	 * 获取：国内物流id
	 */
	public Long getDomesticLogisticsId() {
		return domesticLogisticsId;
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
	 * 设置：运单号
	 */
	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}
	/**
	 * 获取：运单号
	 */
	public String getWaybill() {
		return waybill;
	}
	/**
	 * 设置：物流公司
	 */
	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}
	/**
	 * 获取：物流公司
	 */
	public String getLogisticsCompany() {
		return logisticsCompany;
	}
	/**
	 * 设置：发货日期
	 */
	public void setIssuanceDate(Date issuanceDate) {
		this.issuanceDate = issuanceDate;
	}
	/**
	 * 获取：发货日期
	 */
	public Date getIssuanceDate() {
		return issuanceDate;
	}
	/**
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：
	 */
	public Date getCreateDate() {
		return createDate;
	}
}
