package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 物流成本
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@TableName("product_freight_cost")
public class FreightCostEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 物流成本id
	 */
	@TableId
	private Long freightCostId;
	/**
	 * 运费
	 */
	private BigDecimal freight;
	/**
	 * 售价
	 */
	private BigDecimal price;
	/**
	 * 外币
	 */
	private BigDecimal foreignCurrency;
	/**
	 * 优化
	 */
	private BigDecimal optimization;
	/**
	 * 最终售价
	 */
	private BigDecimal finalPrice;
	/**
	 * 利润
	 */
	private BigDecimal profit;

	/**
	 *产品类型（大包，小包）
	 */
	private String type;

	/**
	 *利润率
	 */
	private String profitRate;

	public String getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(String profitRate) {
		this.profitRate = profitRate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 设置：物流成本id
	 */
	public void setFreightCostId(Long freightCostId) {
		this.freightCostId = freightCostId;
	}
	/**
	 * 获取：物流成本id
	 */
	public Long getFreightCostId() {
		return freightCostId;
	}
	/**
	 * 设置：运费
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	/**
	 * 获取：运费
	 */
	public BigDecimal getFreight() {
		return freight;
	}
	/**
	 * 设置：售价
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * 获取：售价
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * 设置：外币
	 */
	public void setForeignCurrency(BigDecimal foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}
	/**
	 * 获取：外币
	 */
	public BigDecimal getForeignCurrency() {
		return foreignCurrency;
	}
	/**
	 * 设置：优化
	 */
	public void setOptimization(BigDecimal optimization) {
		this.optimization = optimization;
	}
	/**
	 * 获取：优化
	 */
	public BigDecimal getOptimization() {
		return optimization;
	}
	/**
	 * 设置：最终售价
	 */
	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}
	/**
	 * 获取：最终售价
	 */
	public BigDecimal getFinalPrice() {
		return finalPrice;
	}
	/**
	 * 设置：利润
	 */
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	/**
	 * 获取：利润
	 */
	public BigDecimal getProfit() {
		return profit;
	}
}
