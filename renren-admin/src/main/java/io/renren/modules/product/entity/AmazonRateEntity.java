package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 汇率表
 * 
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-12-07 14:47:29
 */
@TableName("amazon_rate")
public class AmazonRateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 汇率id
	 */
	@TableId
	private Long rateId;
	/**
	 * 币种代码
	 */
	private String rateCode;
	/**
	 * 汇率
	 */
	private BigDecimal rate;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 设置：汇率id
	 */
	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}
	/**
	 * 获取：汇率id
	 */
	public Long getRateId() {
		return rateId;
	}

	public String getRateCode() {
		return rateCode;
	}

	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	/**
	 * 设置：汇率
	 */
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	/**
	 * 获取：汇率
	 */
	public BigDecimal getRate() {
		return rate;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
