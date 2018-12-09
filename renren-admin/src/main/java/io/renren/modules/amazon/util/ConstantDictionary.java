package io.renren.modules.amazon.util;


/**
 * @Auther: wdh
 * @Date: 2018/11/29 17:51
 * @Description:
 */
public interface ConstantDictionary {
    public interface OrderStateCode {
        /**
         * 待付款
         */
        String ORDER_STATE_PENDING = "Pending";
        /**
         * 已付款
         */
        String ORDER_STATE_UNSHIPPED = "Unshipped";
        /**
         * 虚发货
         */
        String ORDER_STATE_SHIPPED = "Shipped";
        /**
         * 已采购
         */
        String ORDER_STATE_PURCHASED = "Purchased";
        /**
         * 待发货
         */
        String ORDER_STATE_TOBESHIPPED = "ToBeShipped";
        /**
         * 待签收
         */
        String ORDER_STATE_WAITINGRECEIPT = "WaitingReceipt";
        /**
         * 入库
         */
        String ORDER_STATE_WAREHOUSING = "Warehousing";
        /**
         * 国际已发货
         */
        String ORDER_STATE_INTLSHIPPED = "IntlShipped";
        /**
         * 取消订单
         */
        String ORDER_STATE_CANCELED = "Canceled";
        /**
         * 缺货
         */
        String ORDER_STATE_SHORTAGE = "Shortage";
        /**
         * 退货
         */
        String ORDER_STATE_RETURN = "Return";
        /**
         * 补发
         */
        String ORDER_STATE_REPLACEMENT = "Replacement";
        /**
         * 问题
         */
        String ORDER_STATE_PROBLEM = "Problem";
        /**
         * AMAZON获取到的订单状态
         */
        String[] AMAZON_ORDER_STATE = {ORDER_STATE_PENDING, ORDER_STATE_UNSHIPPED, ORDER_STATE_SHIPPED};
    }

    /**
     * 币种代码
     */
    public interface RateCode {
        /**
         * 人民币
         */
        String ZH_CODE = "CNY";
        /**
         * 美元
         */
        String US_CODE = "USD";
        /**
         * 日元
         */
        String JP_CODE = "JPY";
        /**
         * 欧元
         */
        String EU_CODE = "EUR";
        /**
         * 英镑
         */
        String UK_CODE = "GBP";
        /**
         * 澳大利亚元
         */
        String AU_CODE = "AUD";
        /**
         * 加拿大元
         */
        String CA_CODE = "CAD";
        /**
         * 墨西哥比索
         */
        String MXG_CODE = "MXN";
    }
}
