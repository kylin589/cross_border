package io.renren.modules.order.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单配送地址表
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-10 10:16:27
 */
@TableName("product_ship_address")
public class ProductShipAddressEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 配送地址id
	 */
	@TableId
	private Long shipAddressId;
	/**
	 * 订单id
	 */
	private Long orderId;
	/**
	 * 收件人
	 */
	private String shipName;
	/**
	 * 收件人地址（国家）
	 */
	private String shipCountry;
	/**
	 * 收件人电话
	 */
	private String shipTel;
	/**
	 * 收件人邮编
	 */
	private String shipZip;
	/**
	 * 州、区域
	 */
	private String shipRegion;
	/**
	 * 城市
	 */
	private String shipCity;
	/**
	 * 区县
	 */
	private String shipCounty;
	/**
	 * 区
	 */
	private String shipDistrict;
	/**
	 * 街道
	 */
	private String shipAddressLine;

	/**
	 * 设置：配送地址id
	 */
	public void setShipAddressId(Long shipAddressId) {
		this.shipAddressId = shipAddressId;
	}
	/**
	 * 获取：配送地址id
	 */
	public Long getShipAddressId() {
		return shipAddressId;
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
	 * 设置：收件人
	 */
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	/**
	 * 获取：收件人
	 */
	public String getShipName() {
		return shipName;
	}
	/**
	 * 设置：收件人地址（国家）
	 */
	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}
	/**
	 * 获取：收件人地址（国家）
	 */
	public String getShipCountry() {
		return shipCountry;
	}
	/**
	 * 设置：收件人电话
	 */
	public void setShipTel(String shipTel) {
		this.shipTel = shipTel;
	}
	/**
	 * 获取：收件人电话
	 */
	public String getShipTel() {
		return shipTel;
	}
	/**
	 * 设置：收件人邮编
	 */
	public void setShipZip(String shipZip) {
		this.shipZip = shipZip;
	}
	/**
	 * 获取：收件人邮编
	 */
	public String getShipZip() {
		return shipZip;
	}
	/**
	 * 设置：州、区域
	 */
	public void setShipRegion(String shipRegion) {
		this.shipRegion = shipRegion;
	}
	/**
	 * 获取：州、区域
	 */
	public String getShipRegion() {
		return shipRegion;
	}
	/**
	 * 设置：城市
	 */
	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}
	/**
	 * 获取：城市
	 */
	public String getShipCity() {
		return shipCity;
	}
	/**
	 * 设置：区县
	 */
	public void setShipCounty(String shipCounty) {
		this.shipCounty = shipCounty;
	}
	/**
	 * 获取：区县
	 */
	public String getShipCounty() {
		return shipCounty;
	}
	/**
	 * 设置：区
	 */
	public void setShipDistrict(String shipDistrict) {
		this.shipDistrict = shipDistrict;
	}
	/**
	 * 获取：区
	 */
	public String getShipDistrict() {
		return shipDistrict;
	}
	/**
	 * 设置：街道
	 */
	public void setShipAddressLine(String shipAddressLine) {
		this.shipAddressLine = shipAddressLine;
	}
	/**
	 * 获取：街道
	 */
	public String getShipAddressLine() {
		return shipAddressLine;
	}
}
