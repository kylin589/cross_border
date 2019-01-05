package io.renren.modules.product.vm;

import io.renren.modules.order.entity.ProductShipAddressEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 */
public class OrderModel {

    private  String amazonOrderId;//亚马孙订单id

    private String orderItemId;//订单商品编号

    private Date buyDate;//购买日期

    private  String orderStatus;//订单状态

    private  String productSku;//产品sku

    private  String productAsin;//产品asin

    private BigDecimal orderMoney;//订单金额

    private int orderNumber;//订单总量
    //店铺名称
    private String shopName;
    //币种代码
    private String currencyCode;
    //国家
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private Long userId;

    private Long deptId;
    private Long shopId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    private ProductShipAddressEntity productShipAddressEntity;//地址信息

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductAsin() {
        return productAsin;
    }

    public void setProductAsin(String productAsin) {
        this.productAsin = productAsin;
    }

    public ProductShipAddressEntity getProductShipAddressEntity() {
        return productShipAddressEntity;
    }

    public void setProductShipAddressEntity(ProductShipAddressEntity productShipAddressEntity) {
        this.productShipAddressEntity = productShipAddressEntity;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
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

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }
}
