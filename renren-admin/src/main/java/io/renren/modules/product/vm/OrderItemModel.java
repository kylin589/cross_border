package io.renren.modules.product.vm;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品实体类
 */
public class OrderItemModel {
    /*
     订单商品主键id
     */
    private Long itemId;
    /**
     * 亚马逊订单id
     */
    private String amazonOrderId;

    /**
     * 订单商品编号
     */
    private String orderItemId;

    /**
     * 商品id
     */
    private Long productId;


    /**
     * 商品标题
     */
    private String productTitle;
    /**
     * 商品sku
     */
    private String productSku;
    /**
     * 商品图片url
     */
    private String productImageUrl;
    /**
     * 商品编码
     */
    private String productAsin;
    /**
     * 商品价格
     */
    private BigDecimal productPrice = new BigDecimal(0.00);
    /**
     * 订单商品数量
     */
    private  int orderItemNumber;
    /**
     * 更新日期
     */
    private Date updatetime;

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductAsin() {
        return productAsin;
    }

    public void setProductAsin(String productAsin) {
        this.productAsin = productAsin;
    }


    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getOrderItemNumber() {
        return orderItemNumber;
    }

    public void setOrderItemNumber(int orderItemNumber) {
        this.orderItemNumber = orderItemNumber;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
