package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 物流单价表
 * 
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-12-07 14:47:30
 */
@TableName("product_freight_price")
public class FreightPriceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long freightPriceId;
	/**
	 * 国家代码
	 */
	private String countryCode;
	/**
	 * 类型（大包、小包）
	 */
	private String type;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setFreightPriceId(Long freightPriceId) {
		this.freightPriceId = freightPriceId;
	}
	/**
	 * 获取：
	 */
	public Long getFreightPriceId() {
		return freightPriceId;
	}
	/**
	 * 设置：国家代码
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * 获取：国家代码
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * 设置：类型（大包、小包）
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型（大包、小包）
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：单价
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * 获取：单价
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
