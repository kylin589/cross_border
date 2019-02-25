package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-02-23 09:12:26
 */
@TableName("product_order_item")
public class ProductOrderItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long itemId;
	/**
	 * 亚马逊订单Id
	 */
	private String amazonOrderId;


	/**
	 * 订单商品编号
	 */
	private String orderItemId;

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

	private BigDecimal productPrice = new BigDecimal(0.00);
	/**
	 * 订单商品数量
	 */
	private  int orderItemNumber;
	/**
	 * 更新日期
	 */
	private Date updatetime;

	/**
	 * 产品链接
	 */
	@TableField(exist = false)
	private DomesticLogisticsEntity DomesticLogistics;

	/**
	 * 国内物流信息
	 */
	@TableField(exist = false)
	private String amazonProductUrl;

	public String getAmazonOrderId() {
		return amazonOrderId;
	}

	public void setAmazonOrderId(String amazonOrderId) {
		this.amazonOrderId = amazonOrderId;
	}

	public int getOrderItemNumber() {
		return orderItemNumber;
	}

	public void setOrderItemNumber(int orderItemNumber) {
		this.orderItemNumber = orderItemNumber;
	}

	/**
	 * 设置：
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * 获取：
	 */
	public Long getItemId() {
		return itemId;
	}
	/**
	 * 设置：
	 */
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	/**
	 * 获取：
	 */
	public String getOrderItemId() {
		return orderItemId;
	}
	/**
	 * 设置：
	 */
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	/**
	 * 获取：
	 */
	public String getProductTitle() {
		return productTitle;
	}
	/**
	 * 设置：
	 */
	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	/**
	 * 获取：
	 */
	public String getProductSku() {
		return productSku;
	}
	/**
	 * 设置：
	 */
	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}
	/**
	 * 获取：
	 */
	public String getProductImageUrl() {
		return productImageUrl;
	}
	/**
	 * 设置：
	 */
	public void setProductAsin(String productAsin) {
		this.productAsin = productAsin;
	}
	/**
	 * 获取：
	 */
	public String getProductAsin() {
		return productAsin;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	/**
	 * 设置：
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdatetime() {
		return updatetime;
	}

	public String getAmazonProductUrl() {
		return amazonProductUrl;
	}

	public void setAmazonProductUrl(String amazonProductUrl) {
		this.amazonProductUrl = amazonProductUrl;
	}

	public DomesticLogisticsEntity getDomesticLogistics() {
		return DomesticLogistics;
	}

	public void setDomesticLogistics(DomesticLogisticsEntity domesticLogistics) {
		DomesticLogistics = domesticLogistics;
	}
}
