package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
	 * 币种代码
	 */
	private String rateCode;
	/**
	 * 店铺名称(店铺+国家）
	 */
	private String shopName;

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
	 * 订单金额（人民币）
	 */
	private BigDecimal orderMoneyCny = new BigDecimal(0.00);
	/**
	 * Amazon佣金
	 */
	private BigDecimal amazonCommission = new BigDecimal(0.00);
	/**
	 * Amazon佣金（人民币）
	 */
	private BigDecimal amazonCommissionCny = new BigDecimal(0.00);
	/**
	 * 到账金额
	 */
	private BigDecimal accountMoney = new BigDecimal(0.00);
	/**
	 * 到账金额（人民币）
	 */
	private BigDecimal accountMoneyCny = new BigDecimal(0.00);
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

	private Long shopId;

	private Long userId;

	private Long deptId;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 主图片url
	 */
	private String productImageUrl;

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	/**
	 * 利润率
	 */
	private BigDecimal profitRate = new BigDecimal(0.00);
	/**
	 * 是否为旧数剧(0：新，1：旧)
	 */
	private int isOld = 0;

	/**
	 * 用户名
	 */
	@TableField(exist = false)
	private String userName;

	/*
     * 公司名称
     */
	@TableField(exist = false)
	private String deptName;

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

	public String getRateCode() {
		return rateCode;
	}

	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	public BigDecimal getOrderMoneyCny() {
		return orderMoneyCny;
	}

	public void setOrderMoneyCny(BigDecimal orderMoneyCny) {
		this.orderMoneyCny = orderMoneyCny;
	}

	public BigDecimal getAmazonCommissionCny() {
		return amazonCommissionCny;
	}

	public void setAmazonCommissionCny(BigDecimal amazonCommissionCny) {
		this.amazonCommissionCny = amazonCommissionCny;
	}

	public BigDecimal getAccountMoneyCny() {
		return accountMoneyCny;
	}

	public void setAccountMoneyCny(BigDecimal accountMoneyCny) {
		this.accountMoneyCny = accountMoneyCny;
	}

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public int getIsOld() {
		return isOld;
	}

	public void setIsOld(int isOld) {
		this.isOld = isOld;
	}



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}




	private String orderItemId;

	/**
	 * 关联产品id
	 */
	private Long productId;
	/**
	 * 产品标题
	 */
	private String productTitle;
	/**
	 * 产品sku
	 */
	private String productSku;
	/**
	 * 产品asin码
	 */
	private String productAsin;

		public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
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

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
}
