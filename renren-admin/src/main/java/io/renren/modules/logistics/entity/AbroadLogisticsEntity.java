package io.renren.modules.logistics.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 国际物流
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-18 23:28:12
 */
@TableName("order_abroad_logistics")
public class AbroadLogisticsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 国外物流
	 */
	@TableId
	private Long abroadLogisticsId;
	/**
	 * 订单id
	 */
	private Long orderId;
	/**
	 * 国际物流单号
	 */
	private String abroadWaybill;
	/**
	 * 国际追踪号
	 */
	private String trackWaybill;
	/**
	 * 国际物流状态编码
	 */
	private String status;
	/**
	 * 国际物流状态
	 */
	private String state;
	/**
	 * 实际重量
	 */
	private String actualWeight;
	/**
	 * 国际运费
	 */
	private BigDecimal interFreight;
	/**
	 * 国外物流公司
	 */
	private String destTransportCompany;
	/**
	 * 国内跟踪号
	 */
	private String domesticTrackWaybill;
	/**
	 * 国外物流渠道
	 */
	private String destChannel;
	/**
	 * 是否同步（默认0：没有  1：有）
	 */
	private int isSynchronization = 0;

	private Date createTime = new Date();

	private Date updateTime = new Date();
	/**
	 * 发货时间
	 */
	private Date shipTime;
	/**
	 * 目的地查询网址
	 */
	private String destQueryUrl;
	/**
	 * 服务查询网址
	 */
	private String serviceQueryUrl;
	/**
	 * 联系电话
	 */
	private String mobile;

	/**
	 * 设置：国外物流
	 */
	public void setAbroadLogisticsId(Long abroadLogisticsId) {
		this.abroadLogisticsId = abroadLogisticsId;
	}
	/**
	 * 获取：国外物流
	 */
	public Long getAbroadLogisticsId() {
		return abroadLogisticsId;
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
	 * 设置：国际物流单号
	 */
	public void setAbroadWaybill(String abroadWaybill) {
		this.abroadWaybill = abroadWaybill;
	}
	/**
	 * 获取：国际物流单号
	 */
	public String getAbroadWaybill() {
		return abroadWaybill;
	}
	/**
	 * 设置：国际追踪号
	 */
	public void setTrackWaybill(String trackWaybill) {
		this.trackWaybill = trackWaybill;
	}
	/**
	 * 获取：国际追踪号
	 */
	public String getTrackWaybill() {
		return trackWaybill;
	}
	/**
	 * 设置：国际物流状态编码
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：国际物流状态编码
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：国际物流状态
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 获取：国际物流状态
	 */
	public String getState() {
		return state;
	}
	/**
	 * 设置：国际运费
	 */
	public void setInterFreight(BigDecimal interFreight) {
		this.interFreight = interFreight;
	}
	/**
	 * 获取：国际运费
	 */
	public BigDecimal getInterFreight() {
		return interFreight;
	}
	/**
	 * 设置：是否同步（默认0：没有  1：有）
	 */
	public void setIsSynchronization(int isSynchronization) {
		this.isSynchronization = isSynchronization;
	}
	/**
	 * 获取：是否同步（默认0：没有  1：有）
	 */
	public int getIsSynchronization() {
		return isSynchronization;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDestTransportCompany() {
		return destTransportCompany;
	}

	public void setDestTransportCompany(String destTransportCompany) {
		this.destTransportCompany = destTransportCompany;
	}

	public String getDestChannel() {
		return destChannel;
	}

	public void setDestChannel(String destChannel) {
		this.destChannel = destChannel;
	}

	public String getActualWeight() {
		return actualWeight;
	}

	public void setActualWeight(String actualWeight) {
		this.actualWeight = actualWeight;
	}

	public String getDomesticTrackWaybill() {
		return domesticTrackWaybill;
	}

	public void setDomesticTrackWaybill(String domesticTrackWaybill) {
		this.domesticTrackWaybill = domesticTrackWaybill;
	}

	public Date getShipTime() {
		return shipTime;
	}

	public void setShipTime(Date shipTime) {
		this.shipTime = shipTime;
	}

	public String getDestQueryUrl() {
		return destQueryUrl;
	}

	public void setDestQueryUrl(String destQueryUrl) {
		this.destQueryUrl = destQueryUrl;
	}

	public String getServiceQueryUrl() {
		return serviceQueryUrl;
	}

	public void setServiceQueryUrl(String serviceQueryUrl) {
		this.serviceQueryUrl = serviceQueryUrl;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
