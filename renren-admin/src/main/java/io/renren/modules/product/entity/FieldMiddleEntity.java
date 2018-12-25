package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2018-12-24 05:20:03
 */
@TableName("amazon_field_middle")
public class FieldMiddleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long middleId;
	/**
	 * 上传id
	 */
	private Long uploadId;
	/**
	 * 模板字段id
	 */
	private Long fieldId;
	/**
	 * 字段名称
	 */
	private String fieldName;
	/**
	 * 字段显示名称
	 */
	private String fieldDisplayName;
	/**
	 * 字段值
	 */
	private String value;

	private Integer isCustom;

	public Integer getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(Integer isCustom) {
		this.isCustom = isCustom;
	}

	/**
	 * 设置：主键id
	 */
	public void setMiddleId(Long middleId) {
		this.middleId = middleId;
	}
	/**
	 * 获取：主键id
	 */
	public Long getMiddleId() {
		return middleId;
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
	 * 设置：模板字段id
	 */
	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}
	/**
	 * 获取：模板字段id
	 */
	public Long getFieldId() {
		return fieldId;
	}
	/**
	 * 设置：字段名称
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * 获取：字段名称
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * 设置：字段显示名称
	 */
	public void setFieldDisplayName(String fieldDisplayName) {
		this.fieldDisplayName = fieldDisplayName;
	}
	/**
	 * 获取：字段显示名称
	 */
	public String getFieldDisplayName() {
		return fieldDisplayName;
	}
	/**
	 * 设置：字段值
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取：字段值
	 */
	public String getValue() {
		return value;
	}
}
