package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 亚马逊分类表
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@TableName("product_amazon_category")
public class AmazonCategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 亚马逊分类id
	 */
	@TableId
	private Long amazonCategoryId;
	/**
	 * 上级id
	 */
	private Long parentId;
	/**
	 * 类目名称
	 */
	private String categoryName;
	/**
	 * 展示名称（中文+英文）
	 */
	private String displayName;

	/**
	 * 设置：亚马逊分类id
	 */
	public void setAmazonCategoryId(Long amazonCategoryId) {
		this.amazonCategoryId = amazonCategoryId;
	}
	/**
	 * 获取：亚马逊分类id
	 */
	public Long getAmazonCategoryId() {
		return amazonCategoryId;
	}
	/**
	 * 设置：上级id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：上级id
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：类目名称
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * 获取：类目名称
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * 设置：展示名称（中文+英文）
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * 获取：展示名称（中文+英文）
	 */
	public String getDisplayName() {
		return displayName;
	}
}
