package io.renren.modules.sys.dto;

import java.math.BigDecimal;

/**
 * @Auther: wdh
 * @Date: 2018/12/23 03:19
 * @Description:
 */
public class FranchiseeStatisticsDto {
    //订单数
    private int addOrderCounts = 0;
    //销售额
    private BigDecimal salesVolume = new BigDecimal(0.0);
    //采购成本
    private BigDecimal cost = new BigDecimal(0.0);
    //运费
    private BigDecimal orderFreight = new BigDecimal(0.0);
    //服务费
    private BigDecimal servicePrice = new BigDecimal(0.0);
    //成本
    private BigDecimal allCost = new BigDecimal(0.0);
    //利润
    private BigDecimal profit = new BigDecimal(0.0);
    //利润率
    private BigDecimal profitRate = new BigDecimal(0.0);

    public int getAddOrderCounts() {
        return addOrderCounts;
    }

    public void setAddOrderCounts(int addOrderCounts) {
        this.addOrderCounts = addOrderCounts;
    }

    public BigDecimal getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(BigDecimal salesVolume) {
        this.salesVolume = salesVolume;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getOrderFreight() {
        return orderFreight;
    }

    public void setOrderFreight(BigDecimal orderFreight) {
        this.orderFreight = orderFreight;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public BigDecimal getAllCost() {
        return allCost;
    }

    public void setAllCost(BigDecimal allCost) {
        this.allCost = allCost;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }
}
