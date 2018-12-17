package io.renren.modules.product.entity;

import java.math.BigDecimal;

/**
 * @Auther: wdh
 * @Date: 2018/12/6 13:56
 * @Description:
 */
public class OrderStatisticsEntity {
    //订单数量
    private int orderCounts;
    //总金额
    private BigDecimal orderMoney;
    //利润
    private BigDecimal orderProfit;
    //退款数
    private int returnCounts;
    //退货费用
    private BigDecimal returnCost;
    //采购成本
    private BigDecimal purchasePrice;
    //运费
    private BigDecimal orderFreight;

    public int getOrderCounts() {
        return orderCounts;
    }

    public void setOrderCounts(int orderCounts) {
        this.orderCounts = orderCounts;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public BigDecimal getOrderProfit() {
        return orderProfit;
    }

    public void setOrderProfit(BigDecimal orderProfit) {
        this.orderProfit = orderProfit;
    }

    public int getReturnCounts() {
        return returnCounts;
    }

    public void setReturnCounts(int returnCounts) {
        this.returnCounts = returnCounts;
    }

    public BigDecimal getReturnCost() {
        return returnCost;
    }

    public void setReturnCost(BigDecimal returnCost) {
        this.returnCost = returnCost;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getOrderFreight() {
        return orderFreight;
    }

    public void setOrderFreight(BigDecimal orderFreight) {
        this.orderFreight = orderFreight;
    }
}
