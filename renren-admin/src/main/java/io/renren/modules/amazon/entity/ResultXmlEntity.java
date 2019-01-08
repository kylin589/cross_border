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
 * @date 2019-01-07 22:42:16
 */
@TableName("amazon_result_xml")
public class ResultXmlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long resultId;
	/**
	 * sku
	 */
	private String sku;
	/**
	 * 商品id
	 */
	private Long productId;
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
	 * 错误结果
	 */
	private String result;
	/**
	 * 返回结果类型（Error，错误，Warning，警告）
	 */
	private String resultType;
	/**
	 * 上传返回的错误代码
	 */
	private String resultCode;
	/**
	 * 创建时间
	 */
	private Date creationTime;

	/**
	 * 设置：主键id
	 */
	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}
	/**
	 * 获取：主键id
	 */
	public Long getResultId() {
		return resultId;
	}
	/**
	 * 设置：sku
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}
	/**
	 * 获取：sku
	 */
	public String getSku() {
		return sku;
	}
	/**
	 * 设置：商品id
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：商品id
	 */
	public Long getProductId() {
		return productId;
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
	 * 设置：错误结果
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * 获取：错误结果
	 */
	public String getResult() {
		return result;
	}
	/**
	 * 设置：返回结果类型（Error，错误，Warning，警告）
	 */
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	/**
	 * 获取：返回结果类型（Error，错误，Warning，警告）
	 */
	public String getResultType() {
		return resultType;
	}
	/**
	 * 设置：上传返回的错误代码
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * 获取：上传返回的错误代码
	 */
	public String getResultCode() {
		return resultCode;
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