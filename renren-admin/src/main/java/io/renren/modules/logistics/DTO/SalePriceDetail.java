package io.renren.modules.logistics.DTO;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: wdh
 * @Date: 2019/1/14 11:12
 * @Description:
 */
public class SalePriceDetail implements Serializable {
    /**
     * 成本
     */
    private BigDecimal cost = new BigDecimal(10);
    /**
     * 重量（kg）
     */
    private BigDecimal item_weight;
    /**
     * 利润率
     */
    private BigDecimal profit = new BigDecimal(0.46);
    /**
     * 长
     */
    private BigDecimal item_length = new BigDecimal(1.0);
    /**
     * 宽
     */
    private BigDecimal item_width = new BigDecimal(1.0);
    /**
     * 高
     */
    private BigDecimal item_height = new BigDecimal(1.0);

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getItem_weight() {
        return item_weight;
    }

    public void setItem_weight(BigDecimal item_weight) {
        this.item_weight = item_weight;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getItem_length() {
        return item_length;
    }

    public void setItem_length(BigDecimal item_length) {
        this.item_length = item_length;
    }

    public BigDecimal getItem_width() {
        return item_width;
    }

    public void setItem_width(BigDecimal item_width) {
        this.item_width = item_width;
    }

    public BigDecimal getItem_height() {
        return item_height;
    }

    public void setItem_height(BigDecimal item_height) {
        this.item_height = item_height;
    }

    @Override
    public String toString() {
        return "cost=" + cost +
                ", item_weight=" + item_weight +
                ", profit=" + profit +
                ", item_length=" + item_length +
                ", item_width=" + item_width +
                ", item_height=" + item_height +
                '}';
    }
}
