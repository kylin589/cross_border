package io.renren.modules.sys.dto;

import java.math.BigDecimal;

/**
 * @Auther: wdh
 * @Date: 2018/12/23 03:17
 * @Description:
 */
public class UserStatisticsDto {
    //新增产品数
    private int addProductsCounts;
    //新增订单数
    private int addOrderCounts;
    //异常订单数
    private int abnormalCounts;
    //退货数
    private int returnCounts;
    //销售额
    private BigDecimal salesVolume;
    //采购成本
    private BigDecimal cost;
    //运费
    private BigDecimal orderFreight;
    //服务费
    private BigDecimal servicePrice;
    //利润
    private BigDecimal profit;
    //利润率
    private BigDecimal profitRate;

    public int getAddProductsCounts() {
        return addProductsCounts;
    }

    public void setAddProductsCounts(int addProductsCounts) {
        this.addProductsCounts = addProductsCounts;
    }

    public int getAddOrderCounts() {
        return addOrderCounts;
    }

    public void setAddOrderCounts(int addOrderCounts) {
        this.addOrderCounts = addOrderCounts;
    }

    public int getReturnCounts() {
        return returnCounts;
    }

    public int getAbnormalCounts() {
        return abnormalCounts;
    }

    public void setAbnormalCounts(int abnormalCounts) {
        this.abnormalCounts = abnormalCounts;
    }

    public void setReturnCounts(int returnCounts) {
        this.returnCounts = returnCounts;
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
}
