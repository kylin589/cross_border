package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品上传
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@TableName("product_upload")
public class UploadEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 上传id
	 */
	@TableId
	private Long uploadId;
	/**
	 * 产品id
	 */
	private Long productId;
	/**
	 * 店铺id
	 */
	private Long authorizeShop;
	/**
	 * 操作类型（默认0：上传   1：修改）
	 */
	private Integer operateType;
	/**
	 * 操作项（0:产品基本信息1:关系 2:图片3:库存4:价格）以逗号隔开
	 */
	private String operateItem;
	/**
	 * 亚马逊分类一级id
	 */
	private Long amazonCategoryOneId;
	/**
	 * 亚马逊分类二级id
	 */
	private Long amazonCategoryTwoId;
	/**
	 * 亚马逊分类三级id
	 */
	private Long amazonCategoryThreeId;
	/**
	 * 亚马逊分类四级id
	 */
	private Long amazonCategoryFourId;
	/**
	 * 亚马逊分类五级id
	 */
	private Long amazonCategoryFiveId;
	/**
	 * 亚马逊模板id
	 */
	private Long amazonTemplateId;
	/**
	 * 是否有分类属性（默认0：没有  1：有）
	 */
	private Integer isAttribute;
	/**
	 * 分类属性id
	 */
	private Long attributeId;
	/**
	 * (默认0：正在上传1：上传成功2：上传失败)
	 */
	private Integer uploadState;
	/**
	 * 返回错误代码（上传失败）
	 */
	private String returnCode;
	/**
	 * 返回错误（上传失败）
	 */
	private String returnError;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date uploadTime;

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
	 * 设置：店铺id
	 */
	public void setAuthorizeShop(Long authorizeShop) {
		this.authorizeShop = authorizeShop;
	}
	/**
	 * 获取：店铺id
	 */
	public Long getAuthorizeShop() {
		return authorizeShop;
	}
	/**
	 * 设置：操作类型（默认0：上传   1：修改）
	 */
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	/**
	 * 获取：操作类型（默认0：上传   1：修改）
	 */
	public Integer getOperateType() {
		return operateType;
	}
	/**
	 * 设置：操作项（0:产品基本信息1:关系 2:图片3:库存4:价格）以逗号隔开
	 */
	public void setOperateItem(String operateItem) {
		this.operateItem = operateItem;
	}
	/**
	 * 获取：操作项（0:产品基本信息1:关系 2:图片3:库存4:价格）以逗号隔开
	 */
	public String getOperateItem() {
		return operateItem;
	}
	/**
	 * 设置：亚马逊分类一级id
	 */
	public void setAmazonCategoryOneId(Long amazonCategoryOneId) {
		this.amazonCategoryOneId = amazonCategoryOneId;
	}
	/**
	 * 获取：亚马逊分类一级id
	 */
	public Long getAmazonCategoryOneId() {
		return amazonCategoryOneId;
	}
	/**
	 * 设置：亚马逊分类二级id
	 */
	public void setAmazonCategoryTwoId(Long amazonCategoryTwoId) {
		this.amazonCategoryTwoId = amazonCategoryTwoId;
	}
	/**
	 * 获取：亚马逊分类二级id
	 */
	public Long getAmazonCategoryTwoId() {
		return amazonCategoryTwoId;
	}
	/**
	 * 设置：亚马逊分类三级id
	 */
	public void setAmazonCategoryThreeId(Long amazonCategoryThreeId) {
		this.amazonCategoryThreeId = amazonCategoryThreeId;
	}
	/**
	 * 获取：亚马逊分类三级id
	 */
	public Long getAmazonCategoryThreeId() {
		return amazonCategoryThreeId;
	}
	/**
	 * 设置：亚马逊分类四级id
	 */
	public void setAmazonCategoryFourId(Long amazonCategoryFourId) {
		this.amazonCategoryFourId = amazonCategoryFourId;
	}
	/**
	 * 获取：亚马逊分类四级id
	 */
	public Long getAmazonCategoryFourId() {
		return amazonCategoryFourId;
	}
	/**
	 * 设置：亚马逊分类五级id
	 */
	public void setAmazonCategoryFiveId(Long amazonCategoryFiveId) {
		this.amazonCategoryFiveId = amazonCategoryFiveId;
	}
	/**
	 * 获取：亚马逊分类五级id
	 */
	public Long getAmazonCategoryFiveId() {
		return amazonCategoryFiveId;
	}
	/**
	 * 设置：亚马逊模板id
	 */
	public void setAmazonTemplateId(Long amazonTemplateId) {
		this.amazonTemplateId = amazonTemplateId;
	}
	/**
	 * 获取：亚马逊模板id
	 */
	public Long getAmazonTemplateId() {
		return amazonTemplateId;
	}
	/**
	 * 设置：是否有分类属性（默认0：没有  1：有）
	 */
	public void setIsAttribute(Integer isAttribute) {
		this.isAttribute = isAttribute;
	}
	/**
	 * 获取：是否有分类属性（默认0：没有  1：有）
	 */
	public Integer getIsAttribute() {
		return isAttribute;
	}
	/**
	 * 设置：分类属性id
	 */
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}
	/**
	 * 获取：分类属性id
	 */
	public Long getAttributeId() {
		return attributeId;
	}
	/**
	 * 设置：(默认0：正在上传1：上传成功2：上传失败)
	 */
	public void setUploadState(Integer uploadState) {
		this.uploadState = uploadState;
	}
	/**
	 * 获取：(默认0：正在上传1：上传成功2：上传失败)
	 */
	public Integer getUploadState() {
		return uploadState;
	}
	/**
	 * 设置：返回错误代码（上传失败）
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * 获取：返回错误代码（上传失败）
	 */
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * 设置：返回错误（上传失败）
	 */
	public void setReturnError(String returnError) {
		this.returnError = returnError;
	}
	/**
	 * 获取：返回错误（上传失败）
	 */
	public String getReturnError() {
		return returnError;
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
	 * 设置：
	 */
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	/**
	 * 获取：
	 */
	public Date getUploadTime() {
		return uploadTime;
	}
}
