package io.renren.modules.product.dto;

import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wdh
 * @Date: 2018/12/8 17:30
 * @Description:订单详情数据传输对象
 */
public class OrderDTO {
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 亚马逊订单id
     */
    private String amazonOrderId;
    /**
     * 购买日期
     */
    private Date buyDate;
    /**
     * 订单状态标识
     */
    private String orderStatus;
    /**
     * 订单状态
     */
    private String orderState;
    /**
     * 店铺名称(店铺+国家）
     */
    private String shopName;
    /**
     * 关联产品id
     */
    private String productId;
    /**
     * 产品sku
     */
    private String productSku;
    /**
     * 产品asin码
     */
    private String productAsin;
    /**
     * 订单数量
     */
    private Integer orderNumber;
    /**
     * 寄件信息
     */
    private ProductShipAddressEntity shipAddress;
    /**
     * 国内物流
     */
    private List<DomesticLogisticsEntity> domesticLogisticsList;
    /**
     * 国际物流
     */
    // TODO: 2018/12/10

    /**
     * 订单金额（外币）
     */
    private BigDecimal orderMoneyForeign;

    /**
     * 国际币种
     */

    /**
     * Amazon佣金（外币）
     */
    private BigDecimal amazonCommissionForeign;
    /**
     * 到账金额（外币）
     */
    private BigDecimal accountMoneyForeign;
    /**
     * 国际运费
     */
    private BigDecimal interFreight;
    /**
     * 平台佣金
     */
    private BigDecimal platformCommissions;
    /**
     * 利润
     */
    private BigDecimal orderProfit;
    /**
     * 退货费用
     */
    private BigDecimal returnCost;
    /**
     * 国内物流单号
     */
    private String domesticWaybill;
    /**
     * 国外物流单号
     */
    private String abroadWaybill;


    private Long userId;

    private Long deptId;
    //更新时间
    private Date updateTime;
}
