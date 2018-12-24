package io.renren.modules.product.vm;

import io.renren.modules.order.entity.ProductShipAddressEntity;

/**
 * 订单实体类
 */
public class OrderModel {
    private  Long order_id;//主键id

    private  String amazonOrderId;//亚马孙订单id

    private  String buyDate;//购买日期

    private  String orderStatus;//订单状态

    private  String productSku;//产品sku

    private  String productAsin;//产品asin

    private String orderMoney;//订单金额

    private int orderNumber;//订单总量

    private io.renren.modules.order.entity.ProductShipAddressEntity ProductShipAddressEntity;//地址信息

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
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

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public ProductShipAddressEntity getProductShipAddressEntity() {
        return ProductShipAddressEntity;
    }

    public void setProductShipAddressEntity(ProductShipAddressEntity productShipAddressEntity) {
        ProductShipAddressEntity = productShipAddressEntity;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
