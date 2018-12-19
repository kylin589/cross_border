package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 亚马逊分类表
 *
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-16 11:11:30
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
	 * 英国节点id
	 */
	private String nodeIdUk;
	/**
	 * 德国节点id
	 */
	private String nodeIdDe;
	/**
	 * 法国节点id
	 */
	private String nodeIdFr;
	/**
	 * 意大利节点id
	 */
	private String nodeIdIt;
	/**
	 * 西班牙节点id
	 */
	private String nodeIdEs;
	/**
	 * 国家代号
	 */
	private String countryCode;

	/**
	 * 是否有下一级
	 */
	@TableField(exist=false)
	private String ifNext;

	public String getIfNext() {
		return ifNext;
	}

	public void setIfNext(String ifNext) {
		this.ifNext = ifNext;
	}

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
	/**
	 * 设置：英国节点id
	 */
	public void setNodeIdUk(String nodeIdUk) {
		this.nodeIdUk = nodeIdUk;
	}
	/**
	 * 获取：英国节点id
	 */
	public String getNodeIdUk() {
		return nodeIdUk;
	}
	/**
	 * 设置：德国节点id
	 */
	public void setNodeIdDe(String nodeIdDe) {
		this.nodeIdDe = nodeIdDe;
	}
	/**
	 * 获取：德国节点id
	 */
	public String getNodeIdDe() {
		return nodeIdDe;
	}
	/**
	 * 设置：法国节点id
	 */
	public void setNodeIdFr(String nodeIdFr) {
		this.nodeIdFr = nodeIdFr;
	}
	/**
	 * 获取：法国节点id
	 */
	public String getNodeIdFr() {
		return nodeIdFr;
	}
	/**
	 * 设置：意大利节点id
	 */
	public void setNodeIdIt(String nodeIdIt) {
		this.nodeIdIt = nodeIdIt;
	}
	/**
	 * 获取：意大利节点id
	 */
	public String getNodeIdIt() {
		return nodeIdIt;
	}
	/**
	 * 设置：西班牙节点id
	 */
	public void setNodeIdEs(String nodeIdEs) {
		this.nodeIdEs = nodeIdEs;
	}
	/**
	 * 获取：西班牙节点id
	 */
	public String getNodeIdEs() {
		return nodeIdEs;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public String toString() {
		return "AmazonCategoryEntity{" +
				"amazonCategoryId=" + amazonCategoryId +
				", parentId=" + parentId +
				", categoryName='" + categoryName + '\'' +
				", displayName='" + displayName + '\'' +
				", nodeIdUk='" + nodeIdUk + '\'' +
				", nodeIdDe='" + nodeIdDe + '\'' +
				", nodeIdFr='" + nodeIdFr + '\'' +
				", nodeIdIt='" + nodeIdIt + '\'' +
				", nodeIdEs='" + nodeIdEs + '\'' +
				", countryCode='" + countryCode + '\'' +
				", ifNext='" + ifNext + '\'' +
				'}';
	}
}
