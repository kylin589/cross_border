package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品图片表
 * 
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:28
 */
@TableName("product_image_address")
public class ImageAddressEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 图片id
	 */
	@TableId
	private Long imageId;
	/**
	 * 图片路径
	 */
	private String imageUrl;
	/**
	 * 关联产品id
	 */
	private Long productId;
	/**
	 * 软删（1：删除）
	 */
	private String isDeleted;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建用户id
	 */
	private Long createUserId;
	/**
	 * 最后操作时间
	 */
	private Date lastOperationTime;
	/**
	 * 最后操作人id
	 */
	private Long lastOperationUserId;

	private Integer sort;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * 设置：图片id
	 */
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}
	/**
	 * 获取：图片id
	 */
	public Long getImageId() {
		return imageId;
	}
	/**
	 * 设置：图片路径
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * 获取：图片路径
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * 设置：关联产品id
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：关联产品id
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：软删（1：删除）
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：软删（1：删除）
	 */
	public String getIsDeleted() {
		return isDeleted;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：创建用户id
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：创建用户id
	 */
	public Long getCreateUserId() {
		return createUserId;
	}
	/**
	 * 设置：最后操作时间
	 */
	public void setLastOperationTime(Date lastOperationTime) {
		this.lastOperationTime = lastOperationTime;
	}
	/**
	 * 获取：最后操作时间
	 */
	public Date getLastOperationTime() {
		return lastOperationTime;
	}
	/**
	 * 设置：最后操作人id
	 */
	public void setLastOperationUserId(Long lastOperationUserId) {
		this.lastOperationUserId = lastOperationUserId;
	}
	/**
	 * 获取：最后操作人id
	 */
	public Long getLastOperationUserId() {
		return lastOperationUserId;
	}
}
