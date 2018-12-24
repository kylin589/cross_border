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
 * @date 2018-12-24 07:26:40
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
	 * 上传类型(products：商品，relationships：关系，images：图片，inventory：库存，prices：价格)
	 */
	private String type;
	/**
	 * 0：等待上传；1：正在上传；2：上传成功；3：上传失败，4：有警告
	 */
	private Integer state;
	/**
	 * 上传返回的xml结果
	 */
	private String xml;
	/**
	 * 创建时间
	 */
	private Date creationTime;

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
	 * 设置：上传类型(products：商品，relationships：关系，images：图片，inventory：库存，prices：价格)
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：上传类型(products：商品，relationships：关系，images：图片，inventory：库存，prices：价格)
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：0：等待上传；1：正在上传；2：上传成功；3：上传失败，4：有警告
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：0：等待上传；1：正在上传；2：上传成功；3：上传失败，4：有警告
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：上传返回的xml结果
	 */
	public void setXml(String xml) {
		this.xml = xml;
	}
	/**
	 * 获取：上传返回的xml结果
	 */
	public String getXml() {
		return xml;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}
}
