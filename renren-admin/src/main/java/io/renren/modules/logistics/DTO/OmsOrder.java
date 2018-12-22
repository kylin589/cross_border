package io.renren.modules.logistics.DTO;

import java.math.BigDecimal;

/**
 * @Auther: wdh
 * 推送——订单基础数据模型
 * @Date: 2018/12/13 14:17
 * @Description:
 */
public class OmsOrder {

    /**
     * 订单号sn
     */
    public String order_sn;
    /**
     * 币种单位：EUR,USD,CAD,JPY
     */
    public String order_currency;
    /**
     * 订单时间
     */
    public String order_date;
    /**
     * 总利润额
     */
    public BigDecimal profitamount = null;
    /**
     * 总成本
     */
    public BigDecimal costamount = null;
    /**
     * 运费成本
     */
    public BigDecimal feedamount = null;
    /**
     * 送货地址
     */
    public String delivery_address = null;
    /**
     * 国家英文简称
     */
    public String order_memo;

    public String memo = null;
    /**
     * 销售额（人民币）
     * 最后是已经减去佣金
     */
    public BigDecimal saleamount = null;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_currency() {
        return order_currency;
    }

    public void setOrder_currency(String order_currency) {
        this.order_currency = order_currency;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public BigDecimal getProfitamount() {
        return profitamount;
    }

    public void setProfitamount(BigDecimal profitamount) {
        this.profitamount = profitamount;
    }

    public BigDecimal getCostamount() {
        return costamount;
    }

    public void setCostamount(BigDecimal costamount) {
        this.costamount = costamount;
    }

    public BigDecimal getFeedamount() {
        return feedamount;
    }

    public void setFeedamount(BigDecimal feedamount) {
        this.feedamount = feedamount;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getOrder_memo() {
        return order_memo;
    }

    public void setOrder_memo(String order_memo) {
        this.order_memo = order_memo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getSaleamount() {
        return saleamount;
    }

    public void setSaleamount(BigDecimal saleamount) {
        this.saleamount = saleamount;
    }
}
