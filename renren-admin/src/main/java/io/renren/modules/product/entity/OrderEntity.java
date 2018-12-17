package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-19 14:59:13
 */
@TableName("product_order")
public class OrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单id
	 */
	@TableId
	private Long orderId;
	/**
	 * 亚马逊订单id
	 */
	private String amazonOrderId;
	/**
	 * 购买日期
	 */
	private Date buyDate;
	/**
	 * 订单状态标识
	 */
	private String orderStatus;
	/**
	 * 订单状态
	 */
	private String orderState;
	/**
	 * 异常状态标识
	 */
	private String abnormalStatus;
	/**
	 * 订单异常状态
	 */
	private String abnormalState;
	/**
	 * 国家代码
	 */
	private String countryCode;
	/**
	 * 店铺名称(店铺+国家）
	 */
	private String shopName;
	/**
	 * 关联产品id
	 */
	private String productId;
	/**
	 * 产品sku
	 */
	private String productSku;
	/**
	 * 产品asin码
	 */
	private String productAsin;
	/**
	 * 订单数量
	 */
	private Integer orderNumber;
	/**
	 * 当天汇率
	 */
	private BigDecimal momentRate;
	/**
	 * 采购价格
	 */
	private BigDecimal purchasePrice = new BigDecimal(0.00);
	/**
	 * 订单金额
	 */
	private BigDecimal orderMoney = new BigDecimal(0.00);
	/**
	 * Amazon佣金
	 */
	private BigDecimal amazonCommission = new BigDecimal(0.00);
	/**
	 * 到账金额
	 */
	private BigDecimal accountMoney = new BigDecimal(0.00);
	/**
	 * 国际运费
	 */
	private BigDecimal interFreight = new BigDecimal(0.00);
	/**
	 * 平台佣金
	 */
	private BigDecimal platformCommissions = new BigDecimal(0.00);
	/**
	 * 利润
	 */
	private BigDecimal orderProfit = new BigDecimal(0.00);
	/**
	 * 退货费用
	 */
	private BigDecimal returnCost = new BigDecimal(0.00);
	/**
	 * 国内物流单号
	 */
	private String domesticWaybill;
	/**
	 * 国外物流单号
	 */
	private String abroadWaybill;


	private Long userId;

	private Long deptId;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 国内物流
	 */
//	@TableField(exist = false)
//	private List<>
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
	 * 设置：亚马逊订单id
	 */
	public void setAmazonOrderId(String amazonOrderId) {
		this.amazonOrderId = amazonOrderId;
	}
	/**
	 * 获取：亚马逊订单id
	 */
	public String getAmazonOrderId() {
		return amazonOrderId;
	}
	/**
	 * 设置：购买日期
	 */
	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}
	/**
	 * 获取：购买日期
	 */
	public Date getBuyDate() {
		return buyDate;
	}
	/**
	 * 设置：内部订单标识
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	/**
	 * 获取：内部订单标识
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getAbnormalStatus() {
		return abnormalStatus;
	}

	public void setAbnormalStatus(String abnormalStatus) {
		this.abnormalStatus = abnormalStatus;
	}

	public String getAbnormalState() {
		return abnormalState;
	}

	public void setAbnormalState(String abnormalState) {
		this.abnormalState = abnormalState;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * 设置：店铺名称(店铺+国家）
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	/**
	 * 获取：店铺名称(店铺+国家）
	 */
	public String getShopName() {
		return shopName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * 设置：产品sku
	 */
	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	/**
	 * 获取：产品sku
	 */
	public String getProductSku() {
		return productSku;
	}
	/**
	 * 设置：产品asin码
	 */
	public void setProductAsin(String productAsin) {
		this.productAsin = productAsin;
	}
	/**
	 * 获取：产品asin码
	 */
	public String getProductAsin() {
		return productAsin;
	}
	/**
	 * 设置：订单数量
	 */
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * 获取：订单数量
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}

	public BigDecimal getMomentRate() {
		return momentRate;
	}

	public void setMomentRate(BigDecimal momentRate) {
		this.momentRate = momentRate;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * 设置：订单金额
	 */
	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}
	/**
	 * 获取：订单金额
	 */
	public BigDecimal getOrderMoney() {
		return orderMoney;
	}
	/**
	 * 设置：Amazon佣金
	 */
	public void setAmazonCommission(BigDecimal amazonCommission) {
		this.amazonCommission = amazonCommission;
	}
	/**
	 * 获取：Amazon佣金
	 */
	public BigDecimal getAmazonCommission() {
		return amazonCommission;
	}
	/**
	 * 设置：到账金额
	 */
	public void setAccountMoney(BigDecimal accountMoney) {
		this.accountMoney = accountMoney;
	}
	/**
	 * 获取：到账金额
	 */
	public BigDecimal getAccountMoney() {
		return accountMoney;
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
	 * 设置：平台佣金
	 */
	public void setPlatformCommissions(BigDecimal platformCommissions) {
		this.platformCommissions = platformCommissions;
	}
	/**
	 * 获取：平台佣金
	 */
	public BigDecimal getPlatformCommissions() {
		return platformCommissions;
	}
	/**
	 * 设置：利润
	 */
	public void setOrderProfit(BigDecimal orderProfit) {
		this.orderProfit = orderProfit;
	}
	/**
	 * 获取：利润
	 */
	public BigDecimal getOrderProfit() {
		return orderProfit;
	}
	/**
	 * 设置：退货费用
	 */
	public void setReturnCost(BigDecimal returnCost) {
		this.returnCost = returnCost;
	}
	/**
	 * 获取：退货费用
	 */
	public BigDecimal getReturnCost() {
		return returnCost;
	}

	public String getDomesticWaybill() {
		return domesticWaybill;
	}

	public void setDomesticWaybill(String domesticWaybill) {
		this.domesticWaybill = domesticWaybill;
	}

	public String getAbroadWaybill() {
		return abroadWaybill;
	}

	public void setAbroadWaybill(String abroadWaybill) {
		this.abroadWaybill = abroadWaybill;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
