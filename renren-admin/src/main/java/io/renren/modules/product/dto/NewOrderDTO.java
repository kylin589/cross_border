package io.renren.modules.product.dto;

import io.renren.modules.logistics.entity.AbroadLogisticsEntity;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.entity.NewOrderAbroadLogisticsEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.entity.RemarkEntity;
import io.renren.modules.product.entity.NewOrderItemEntity;
import io.renren.modules.product.entity.ProductOrderItemEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wdh
 * @Date: 2018/12/8 17:30
 * @Description:订单详情数据传输对象
 */
public class NewOrderDTO {
    /**
     * 订单id
     */
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
     * 订单异常状态标识
     */
    private String abnormalStatus;
    /**
     * 订单异常状态
     */
    private String abnormalState;
    /**
     * 店铺名称(店铺+国家）
     */
    private String shopName;

    private List<NewOrderItemEntity> orderProductList = new ArrayList<>();
    /**
     * 关联产品id
     */
    private Long productId;


    /**
     * 主图片
     */
    private String productImageUrl;
    /**
     * 产品sku
     */
    private String productSku;
    /**
     * 产品标题
     */
    private String productTitle;
    /**
     * 产品asin码
     */
    private String productAsin;
    /**
     * 订单数量
     */
    private Integer orderNumber;
    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;
    /**
     * 寄件信息
     */
    private ProductShipAddressEntity shipAddress;
    /**
     * 国内物流
     */
    private List<DomesticLogisticsEntity> domesticLogisticsList;
    /**
     * 国际物流
     */
    private NewOrderAbroadLogisticsEntity abroadLogistics;
    /**
     * 当天汇率
     */
    private BigDecimal momentRate;

    /**
     * 订单金额（外币）
     */
    private BigDecimal orderMoneyForeign;

    /**
     * 订单金额(人民币)
     */
    private BigDecimal orderMoney;
    /**
     * Amazon佣金（外币）
     */
    private BigDecimal amazonCommissionForeign;
    /**
     * Amazon佣金（人民币）
     */
    private BigDecimal amazonCommission;
    /**
     * 到账金额（外币）
     */
    private BigDecimal accountMoneyForeign;
    /**
     * 到账金额（人民币）
     */
    private BigDecimal accountMoney;
    /**
     * s
     */
    private BigDecimal interFreight;
    /**
     * 平台佣金
     */
    private BigDecimal platformCommissions;
    /**
     * 利润
     */
    private BigDecimal orderProfit;
    /**
     * 退货费用
     */
    private BigDecimal returnCost;

    /**
     * 备注列表
     */
    private List<RemarkEntity> remarkList;
    /**
     * 操作日志列表
     */
    private List<RemarkEntity> logList;
    /**
     * 产品链接
     */
    private String amazonProductUrl;

    /**
     * 利润率
     */
    private BigDecimal profitRate;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductAsin() {
        return productAsin;
    }

    public void setProductAsin(String productAsin) {
        this.productAsin = productAsin;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public ProductShipAddressEntity getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(ProductShipAddressEntity shipAddress) {
        this.shipAddress = shipAddress;
    }

    public List<DomesticLogisticsEntity> getDomesticLogisticsList() {
        return domesticLogisticsList;
    }

    public void setDomesticLogisticsList(List<DomesticLogisticsEntity> domesticLogisticsList) {
        this.domesticLogisticsList = domesticLogisticsList;
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

    public BigDecimal getOrderMoneyForeign() {
        return orderMoneyForeign;
    }

    public void setOrderMoneyForeign(BigDecimal orderMoneyForeign) {
        this.orderMoneyForeign = orderMoneyForeign;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public BigDecimal getAmazonCommissionForeign() {
        return amazonCommissionForeign;
    }

    public void setAmazonCommissionForeign(BigDecimal amazonCommissionForeign) {
        this.amazonCommissionForeign = amazonCommissionForeign;
    }

    public BigDecimal getAmazonCommission() {
        return amazonCommission;
    }

    public void setAmazonCommission(BigDecimal amazonCommission) {
        this.amazonCommission = amazonCommission;
    }

    public BigDecimal getAccountMoneyForeign() {
        return accountMoneyForeign;
    }

    public void setAccountMoneyForeign(BigDecimal accountMoneyForeign) {
        this.accountMoneyForeign = accountMoneyForeign;
    }

    public BigDecimal getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(BigDecimal accountMoney) {
        this.accountMoney = accountMoney;
    }

    public BigDecimal getInterFreight() {
        return interFreight;
    }

    public void setInterFreight(BigDecimal interFreight) {
        this.interFreight = interFreight;
    }

    public BigDecimal getPlatformCommissions() {
        return platformCommissions;
    }

    public void setPlatformCommissions(BigDecimal platformCommissions) {
        this.platformCommissions = platformCommissions;
    }

    public BigDecimal getOrderProfit() {
        return orderProfit;
    }

    public void setOrderProfit(BigDecimal orderProfit) {
        this.orderProfit = orderProfit;
    }

    public BigDecimal getReturnCost() {
        return returnCost;
    }

    public void setReturnCost(BigDecimal returnCost) {
        this.returnCost = returnCost;
    }

    public List<RemarkEntity> getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(List<RemarkEntity> remarkList) {
        this.remarkList = remarkList;
    }

    public List<RemarkEntity> getLogList() {
        return logList;
    }

    public void setLogList(List<RemarkEntity> logList) {
        this.logList = logList;
    }

    public List<NewOrderItemEntity> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<NewOrderItemEntity> orderProductList) {
        this.orderProductList = orderProductList;
    }

    public NewOrderAbroadLogisticsEntity getAbroadLogistics() {
        return abroadLogistics;
    }

    public void setAbroadLogistics(NewOrderAbroadLogisticsEntity abroadLogistics) {
        this.abroadLogistics = abroadLogistics;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getAmazonProductUrl() {
        return amazonProductUrl;
    }

    public void setAmazonProductUrl(String amazonProductUrl) {
        this.amazonProductUrl = amazonProductUrl;
    }

    public BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }
}
