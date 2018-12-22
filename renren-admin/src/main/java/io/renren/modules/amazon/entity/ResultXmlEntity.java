package io.renren.modules.amazon.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 亚马逊商品上传结果表
 * 
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-21 22:55:00
 */
@TableName("amazon_result_xml")
public class ResultXmlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 上传id
	 */
	private Long uploadId;
	/**
	 * 商品上传返回的xml结果
	 */
	private String productsResultXml;
	/**
	 * 关系上传返回的xml结果
	 */
	private String relationshipsResultXml;
	/**
	 * 图片上传返回的xml结果
	 */
	private String imagesResultXml;
	/**
	 * 库存上传返回的xml结果
	 */
	private String inventoryResultXml;
	/**
	 * 价格上传返回的xml结果
	 */
	private String pricesResultXml;

	/**
	 * 设置：主键id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：上传id
	 */
	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}
	/**
	 * 获取：上传id
	 */
	public Long getUploadId() {
		return uploadId;
	}
	/**
	 * 设置：商品上传返回的xml结果
	 */
	public void setProductsResultXml(String productsResultXml) {
		this.productsResultXml = productsResultXml;
	}
	/**
	 * 获取：商品上传返回的xml结果
	 */
	public String getProductsResultXml() {
		return productsResultXml;
	}
	/**
	 * 设置：关系上传返回的xml结果
	 */
	public void setRelationshipsResultXml(String relationshipsResultXml) {
		this.relationshipsResultXml = relationshipsResultXml;
	}
	/**
	 * 获取：关系上传返回的xml结果
	 */
	public String getRelationshipsResultXml() {
		return relationshipsResultXml;
	}
	/**
	 * 设置：图片上传返回的xml结果
	 */
	public void setImagesResultXml(String imagesResultXml) {
		this.imagesResultXml = imagesResultXml;
	}
	/**
	 * 获取：图片上传返回的xml结果
	 */
	public String getImagesResultXml() {
		return imagesResultXml;
	}
	/**
	 * 设置：库存上传返回的xml结果
	 */
	public void setInventoryResultXml(String inventoryResultXml) {
		this.inventoryResultXml = inventoryResultXml;
	}
	/**
	 * 获取：库存上传返回的xml结果
	 */
	public String getInventoryResultXml() {
		return inventoryResultXml;
	}
	/**
	 * 设置：价格上传返回的xml结果
	 */
	public void setPricesResultXml(String pricesResultXml) {
		this.pricesResultXml = pricesResultXml;
	}
	/**
	 * 获取：价格上传返回的xml结果
	 */
	public String getPricesResultXml() {
		return pricesResultXml;
	}
}
