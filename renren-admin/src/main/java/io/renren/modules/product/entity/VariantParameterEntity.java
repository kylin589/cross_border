package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 变体参数
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@TableName("product_variant_parameter")
public class VariantParameterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 变体参数id
	 */
	@TableId
	private Long paramsId;
	/**
	 * 变体类型（color、size）
	 */
	private String paramsType;
	/**
	 * 变体值(多个值用逗号隔开存储)
	 */
	private String paramsValue;

	/**
	 * 设置：变体参数id
	 */
	public void setParamsId(Long paramsId) {
		this.paramsId = paramsId;
	}
	/**
	 * 获取：变体参数id
	 */
	public Long getParamsId() {
		return paramsId;
	}
	/**
	 * 设置：变体类型（color、size）
	 */
	public void setParamsType(String paramsType) {
		this.paramsType = paramsType;
	}
	/**
	 * 获取：变体类型（color、size）
	 */
	public String getParamsType() {
		return paramsType;
	}
	/**
	 * 设置：变体值(多个值用逗号隔开存储)
	 */
	public void setParamsValue(String paramsValue) {
		this.paramsValue = paramsValue;
	}
	/**
	 * 获取：变体值(多个值用逗号隔开存储)
	 */
	public String getParamsValue() {
		return paramsValue;
	}

	@Override
	public String toString() {
		return "VariantParameterEntity{" +
				"paramsId=" + paramsId +
				", paramsType='" + paramsType + '\'' +
				", paramsValue='" + paramsValue + '\'' +
				'}';
	}
}
