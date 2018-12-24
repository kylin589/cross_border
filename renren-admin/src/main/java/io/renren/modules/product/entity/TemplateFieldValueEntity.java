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
 * @date 2018-12-23 23:19:40
 */
@TableName("amazon_template_field_value")
public class TemplateFieldValueEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 可选值id
	 */
	@TableId
	private Long valueId;
	/**
	 * 字段id
	 */
	private Long fieldId;
	/**
	 * 中国值
	 */
	private String cnValue;
	/**
	 * 英国值
	 */
	private String ukValue;
	/**
	 * 法国值
	 */
	private String frValue;
	/**
	 * 德国值
	 */
	private String deValue;
	/**
	 * 意大利值
	 */
	private String itValue;
	/**
	 * 西班牙值
	 */
	private String esValue;
	/**
	 * 日本值
	 */
	private String jpValue;
	/**
	 * 澳大利亚值
	 */
	private String auValue;
	/**
	 * 加拿大值
	 */
	private String caValue;
	/**
	 * 美国值
	 */
	private String usValue;
	/**
	 * 墨西哥值
	 */
	private String mxValue;

	/**
	 * 设置：可选值id
	 */
	public void setValueId(Long valueId) {
		this.valueId = valueId;
	}
	/**
	 * 获取：可选值id
	 */
	public Long getValueId() {
		return valueId;
	}
	/**
	 * 设置：字段id
	 */
	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}
	/**
	 * 获取：字段id
	 */
	public Long getFieldId() {
		return fieldId;
	}
	/**
	 * 设置：中国值
	 */
	public void setCnValue(String cnValue) {
		this.cnValue = cnValue;
	}
	/**
	 * 获取：中国值
	 */
	public String getCnValue() {
		return cnValue;
	}
	/**
	 * 设置：英国值
	 */
	public void setUkValue(String ukValue) {
		this.ukValue = ukValue;
	}
	/**
	 * 获取：英国值
	 */
	public String getUkValue() {
		return ukValue;
	}
	/**
	 * 设置：法国值
	 */
	public void setFrValue(String frValue) {
		this.frValue = frValue;
	}
	/**
	 * 获取：法国值
	 */
	public String getFrValue() {
		return frValue;
	}
	/**
	 * 设置：德国值
	 */
	public void setDeValue(String deValue) {
		this.deValue = deValue;
	}
	/**
	 * 获取：德国值
	 */
	public String getDeValue() {
		return deValue;
	}
	/**
	 * 设置：意大利值
	 */
	public void setItValue(String itValue) {
		this.itValue = itValue;
	}
	/**
	 * 获取：意大利值
	 */
	public String getItValue() {
		return itValue;
	}
	/**
	 * 设置：西班牙值
	 */
	public void setEsValue(String esValue) {
		this.esValue = esValue;
	}
	/**
	 * 获取：西班牙值
	 */
	public String getEsValue() {
		return esValue;
	}
	/**
	 * 设置：日本值
	 */
	public void setJpValue(String jpValue) {
		this.jpValue = jpValue;
	}
	/**
	 * 获取：日本值
	 */
	public String getJpValue() {
		return jpValue;
	}
	/**
	 * 设置：澳大利亚值
	 */
	public void setAuValue(String auValue) {
		this.auValue = auValue;
	}
	/**
	 * 获取：澳大利亚值
	 */
	public String getAuValue() {
		return auValue;
	}
	/**
	 * 设置：加拿大值
	 */
	public void setCaValue(String caValue) {
		this.caValue = caValue;
	}
	/**
	 * 获取：加拿大值
	 */
	public String getCaValue() {
		return caValue;
	}
	/**
	 * 设置：美国值
	 */
	public void setUsValue(String usValue) {
		this.usValue = usValue;
	}
	/**
	 * 获取：美国值
	 */
	public String getUsValue() {
		return usValue;
	}
	/**
	 * 设置：墨西哥值
	 */
	public void setMxValue(String mxValue) {
		this.mxValue = mxValue;
	}
	/**
	 * 获取：墨西哥值
	 */
	public String getMxValue() {
		return mxValue;
	}
}
