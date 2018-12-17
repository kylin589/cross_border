package io.renren.modules.logistics.DTO;

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
    public BigDecimal price;
    /**
     * 数量
     */
    public int quantity;
    /**
     * 成本
     */
    public BigDecimal cost;
    /**
     * 利润
     */
    public BigDecimal profit;
    /**
     * 采购编号-采购参考号
     */
    public String supplyorderno;
    /**
     * 国内跟踪号--供应快递号
     */
    public String supplyexpressno;
    /**
     * 销售额
     */
    public BigDecimal saleamount;
    /**
     * 产品时间
     */
    public Date product_date;
    /**
     * 是否带电池
     */
    public boolean is_electriferous;
    /**
     是否液体
     */
    public boolean is_liquid;
}
