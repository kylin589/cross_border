package io.renren.modules.logistics.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: wdh
 * 推送——订单详情模型
 * @Date: 2018/12/13 14:18
 * @Description:
 */
public class OmsOrderDetail {
    /**
     * 产品SKU编码
     */
    public String product_id;
    /**
     * 售价（美金）
     */
    public BigDecimal price = null;
    /**
     * 数量
     */
    public int quantity;
    /**
     * 成本
     */
    public BigDecimal cost = null;
    /**
     * 利润
     */
    public BigDecimal profit = null;
    /**
     * 采购编号-采购参考号
     */
    public String supplyorderno = null;
    /**
     * 国内跟踪号--供应快递号
     */
    public String supplyexpressno;
    /**
     * 销售额
     */
    public BigDecimal saleamount = null;
    /**
     * 产品时间
     */
    public Date product_date = null;
    /**
     * 是否带电池
     */
    public boolean is_electriferous = false;
    /**
     是否液体
     */
    public boolean is_liquid = false;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getSupplyorderno() {
        return supplyorderno;
    }

    public void setSupplyorderno(String supplyorderno) {
        this.supplyorderno = supplyorderno;
    }

    public String getSupplyexpressno() {
        return supplyexpressno;
    }

    public void setSupplyexpressno(String supplyexpressno) {
        this.supplyexpressno = supplyexpressno;
    }

    public BigDecimal getSaleamount() {
        return saleamount;
    }

    public void setSaleamount(BigDecimal saleamount) {
        this.saleamount = saleamount;
    }

    public Date getProduct_date() {
        return product_date;
    }

    public void setProduct_date(Date product_date) {
        this.product_date = product_date;
    }

    public boolean isIs_electriferous() {
        return is_electriferous;
    }

    public void setIs_electriferous(boolean is_electriferous) {
        this.is_electriferous = is_electriferous;
    }

    public boolean isIs_liquid() {
        return is_liquid;
    }

    public void setIs_liquid(boolean is_liquid) {
        this.is_liquid = is_liquid;
    }
}
