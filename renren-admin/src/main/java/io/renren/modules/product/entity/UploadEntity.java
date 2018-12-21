package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
	//开始id
	private Long startId;
	//结束id
	private Long endId;
	//上传id,逗号隔开
	private String uploadIds;
	//筛选后要上传的id列表
	@TableField(exist = false)
	private List<ProductsEntity>  uploadProductsList;
	//国家代码
	@TableField(exist = false)
	private String countryCode;
	/**
	 * 店铺id
	 */
	private Long grantShopId;
	/**
	 * 店铺
	 */
	private String grantShop;
	/**
	 * 操作类型（默认0：上传   1：修改）
	 */
	private Integer operateType;
	/**
	 * 操作项（0:产品基本信息1:关系 2:图片3:库存4:价格）以逗号隔开
	 */
	private String operateItem;
	/**
	 * 亚马逊分类id
	 */
	private Long amazonCategoryId;
	/**
	 * 亚马逊分类
	 */
	private String amazonCategoryNodeId;
	/**
	 * 亚马逊分类
	 */
	private String amazonCategory;
	/**
	 * 亚马逊模板id
	 */
	private Long amazonTemplateId;
	/**
	 * 亚马逊模板
	 */
	private String amazonTemplate;
	/**
	 * 是否有分类属性（默认0：没有  1：有）
	 */
	private Integer isAttribute;
	/**
	 * 分类属性id
	 */
	private Long attributeId;
	/**
	 * (默认0：等待上传；1：正在上传；2：上传成功；3：上传失败)
	 */
	private Integer uploadState = 0;
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
	private Date uploadTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 公司id
	 */
	private Long deptId;

	public Long getGrantShopId() {
		return grantShopId;
	}

	public void setGrantShopId(Long grantShopId) {
		this.grantShopId = grantShopId;
	}

	public String getGrantShop() {
		return grantShop;
	}

	public void setGrantShop(String grantShop) {
		this.grantShop = grantShop;
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

	public Long getStartId() {
		return startId;
	}

	public void setStartId(Long startId) {
		this.startId = startId;
	}

	public Long getEndId() {
		return endId;
	}

	public void setEndId(Long endId) {
		this.endId = endId;
	}

	public String getUploadIds() {
		return uploadIds;
	}

	public void setUploadIds(String uploadIds) {
		this.uploadIds = uploadIds;
	}

	public List<ProductsEntity> getUploadProductsList() {
		return uploadProductsList;
	}

	public void setUploadProductsList(List<ProductsEntity> uploadProductsList) {
		this.uploadProductsList = uploadProductsList;
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
	 * 设置：亚马逊分类
	 */
	public void setAmazonCategory(String amazonCategory) {
		this.amazonCategory = amazonCategory;
	}
	/**
	 * 获取：亚马逊分类
	 */
	public String getAmazonCategory() {
		return amazonCategory;
	}

	public String getAmazonCategoryNodeId() {
		return amazonCategoryNodeId;
	}

	public void setAmazonCategoryNodeId(String amazonCategoryNodeId) {
		this.amazonCategoryNodeId = amazonCategoryNodeId;
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
	 * 设置：亚马逊模板
	 */
	public void setAmazonTemplate(String amazonTemplate) {
		this.amazonTemplate = amazonTemplate;
	}
	/**
	 * 获取：亚马逊模板
	 */
	public String getAmazonTemplate() {
		return amazonTemplate;
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
	 * 设置：上传时间
	 */
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	/**
	 * 获取：上传时间
	 */
	public Date getUploadTime() {
		return uploadTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
