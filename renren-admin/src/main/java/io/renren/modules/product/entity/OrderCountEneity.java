package io.renren.modules.product.entity;

/**
 * @Auther: wdh
 * @Date: 2018/12/6 13:56
 * @Description:
 */
public class OrderCountEneity {
    private int orderCounts;
    private int orderMoney;
    private int orderProfit;

    public int getOrderCounts() {
        return orderCounts;
    }

    public void setOrderCounts(int orderCounts) {
        this.orderCounts = orderCounts;
    }

    public int getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(int orderMoney) {
        this.orderMoney = orderMoney;
    }

    public int getOrderProfit() {
        return orderProfit;
    }

    public void setOrderProfit(int orderProfit) {
        this.orderProfit = orderProfit;
    }
}
