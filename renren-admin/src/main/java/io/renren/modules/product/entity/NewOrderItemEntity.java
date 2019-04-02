package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.entity.NewOrderDomesticLogisticsEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 新子订单表
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 14:50:57
 */
@TableName("new_order_item")
public class NewOrderItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    *
    */
    @TableId
    private Long itemId;
    /**
    * 亚马逊订单id
    */
    private String amazonOrderId;
    /**
    * 亚马逊订单商品id
    */
    private String orderItemId;
    /**
    * 产品id
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
    * 产品图片
    */
    private String productImageUrl;
    /**
    * 产品asin码
    */
    private String productAsin;
    /**
    * 产品
    */
    private BigDecimal productPrice;
    /**
    * 产品数量
    */
    private Integer orderItemNumber;
    /**
    * 更新日期
    */
    private Date updatetime;

    /**
    * 国内物流信息
    */
    @TableField(exist = false)
    private NewOrderDomesticLogisticsEntity domesticLogistics;

    /**
    * 产品链接
    */
    @TableField(exist = false)
    private String amazonProductUrl;

    /**
    * 设置：
    */
    public void setItemId(Long itemId) {
    this.itemId = itemId;
    }

    /**
    * 获取：
    */
    public Long getItemId() {
    return itemId;
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
    * 设置：亚马逊订单商品id
    */
    public void setOrderItemId(String orderItemId) {
    this.orderItemId = orderItemId;
    }

    /**
    * 获取：亚马逊订单商品id
    */
    public String getOrderItemId() {
    return orderItemId;
    }
    /**
    * 设置：产品id
    */
    public void setProductId(Long productId) {
    this.productId = productId;
    }

    /**
    * 获取：产品id
    */
    public Long getProductId() {
    return productId;
    }
    /**
    * 设置：产品标题
    */
    public void setProductTitle(String productTitle) {
    this.productTitle = productTitle;
    }

    /**
    * 获取：产品标题
    */
    public String getProductTitle() {
    return productTitle;
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
    * 设置：产品图片
    */
    public void setProductImageUrl(String productImageUrl) {
    this.productImageUrl = productImageUrl;
    }

    /**
    * 获取：产品图片
    */
    public String getProductImageUrl() {
    return productImageUrl;
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
    * 设置：产品
    */
    public void setProductPrice(BigDecimal productPrice) {
    this.productPrice = productPrice;
    }

    /**
    * 获取：产品
    */
    public BigDecimal getProductPrice() {
    return productPrice;
    }
    /**
    * 设置：产品数量
    */
    public void setOrderItemNumber(Integer orderItemNumber) {
    this.orderItemNumber = orderItemNumber;
    }

    /**
    * 获取：产品数量
    */
    public Integer getOrderItemNumber() {
    return orderItemNumber;
    }
    /**
    * 设置：更新日期
    */
    public void setUpdatetime(Date updatetime) {
    this.updatetime = updatetime;
    }

    /**
    * 获取：更新日期
    */
    public Date getUpdatetime() {
    return updatetime;
    }

    public NewOrderDomesticLogisticsEntity getDomesticLogistics() {
        return domesticLogistics;
    }

    public void setDomesticLogistics(NewOrderDomesticLogisticsEntity domesticLogistics) {
        this.domesticLogistics = domesticLogistics;
    }

    public String getAmazonProductUrl() {
        return amazonProductUrl;
    }

    public void setAmazonProductUrl(String amazonProductUrl) {
        this.amazonProductUrl = amazonProductUrl;
    }
}
