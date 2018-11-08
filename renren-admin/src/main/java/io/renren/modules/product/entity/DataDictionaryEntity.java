package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:46
 */
@TableName("product_data_dictionary")
public class DataDictionaryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long dataId;
	/**
	 * 数据分类
	 */
	private String dataType;
	/**
	 * 数据标识
	 */
	private String dataNumber;
	/**
	 * 数据内容
	 */
	private String dataContent;
	/**
	 * 数据排序
	 */
	private Integer dataSort;

	/**
	 * 设置：
	 */
	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}
	/**
	 * 获取：
	 */
	public Long getDataId() {
		return dataId;
	}
	/**
	 * 设置：数据分类
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * 获取：数据分类
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * 设置：数据标识
	 */
	public void setDataNumber(String dataNumber) {
		this.dataNumber = dataNumber;
	}
	/**
	 * 获取：数据标识
	 */
	public String getDataNumber() {
		return dataNumber;
	}
	/**
	 * 设置：数据内容
	 */
	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}
	/**
	 * 获取：数据内容
	 */
	public String getDataContent() {
		return dataContent;
	}
	/**
	 * 设置：数据排序
	 */
	public void setDataSort(Integer dataSort) {
		this.dataSort = dataSort;
	}
	/**
	 * 获取：数据排序
	 */
	public Integer getDataSort() {
		return dataSort;
	}
}
