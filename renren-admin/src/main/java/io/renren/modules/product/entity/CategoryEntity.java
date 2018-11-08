package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 内部分类表
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@TableName("product_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 内部分类id
	 */
	@TableId
	private Long categoryId;
	/**
	 * 上级id
	 */
	private Long parentId;
	/**
	 * 类目名称
	 */
	private String categoryName;

	/**
	 * 设置：内部分类id
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * 获取：内部分类id
	 */
	public Long getCategoryId() {
		return categoryId;
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
}
