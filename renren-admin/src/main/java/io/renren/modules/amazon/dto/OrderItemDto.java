package io.renren.modules.amazon.dto;
/**
 * @auther: jhy
 * @date: 2018/11/22 10:15
 */
public class OrderItemDto {
    /**
     *
     */
    private String ASIN;
    /**
     * 商品的卖家 SKU。
     * 可选项
     */
    private String sellerSKU;
    /**
     * 亚马逊定义的订单商品识别号。
     */
    private String orderItemId;
    /**
     * 商品名称。
     * 可选项
     */
    private String title;
    /**
     *
     */
    private int quantityOrdered;
    /**
     * 已配送的商品数量。
     * 可选项
     */
    private int quantityShipped;
    /**
     * 订单商品的售价。
     */
    private MoneyDto itemPrice;
    /**
     * 运费
     * 可选项
     */
    private MoneyDto shippingPrice;
    /**
     * 商品的礼品包装金额。
     * 可选项
     */
    private MoneyDto giftWrapPrice;
    /**
     * 商品价格的税费。
     * 可选项
     */
    private MoneyDto itemTax;
    /**
     * 运费的税费。
     * 可选项
     */
    private MoneyDto shippingTax;
    /**
     * 礼品包装金额的税费。
     * 可选项
     */
    private MoneyDto giftWrapTax;
    /**
     * 运费的折扣。
     * 可选项
     */
    private MoneyDto shippingDiscount;
    /**
     * 报价中的全部促销折扣总计。
     * 可选项
     */
    private MoneyDto promotionDiscount;
    /**
     * PromotionId 元素列表。
     * 可选项
     */
    private String promotionIds;
    /**
     * COD 服务费用。
     * 注： CODFee 是仅在日本 (JP) 使用的响应元素。
     * 可选项
     */
    private MoneyDto CODFee;
    /**
     * 货到付款费用的折扣。
     * 注： CODFeeDiscount 是仅在日本 (JP) 使用的响应元素。
     * 可选项
     */
    private MoneyDto CODFeeDiscount;
    /**
     * 买家提供的礼品消息。
     * 可选项
     */
    private String giftMessageText;
    /**
     * 买家指定的礼品包装等级。
     * 可选项
     */
    private String giftWrapLevel;
    /**
     * 卖家描述的商品状况。
     * 可选项
     */
    private String conditionNote;
    /**
     * 商品的状况。
     * ConditionId 值：
     * New
     * Used
     * Collectible
     * Refurbished
     * Preorder
     * Club
     * 可选项
     */
    private String conditionId;
    /**
     * 商品的子状况。
     * ConditionSubtypeId 值：
     * New
     * Mint
     * Very Good
     * Good
     * Acceptable
     * Poor
     * Club
     * OEM
     * Warranty
     * Refurbished Warranty
     * Refurbished
     * Open Box
     * Any
     * Other
     * 可选项
     */
    private String conditionSubtypeId;
    /**
     * 订单预约送货上门的开始日期（目的地时区）。日期格式为 ISO 8601。
     * 注： 预约送货上门仅适用于日本 (JP)。
     * 可选。仅当订单为预约送货上门时才返回。
     */
    private String scheduledDeliveryStartDate;
    /**
     * 订单预约送货上门的终止日期（目的地时区）。日期格式为 ISO 8601。
     * 注： 预约送货上门仅适用于日本 (JP)。
     * 可选。仅当订单为预约送货上门时才返回。
     */
    private String scheduledDeliveryEndDate;

    public String getASIN() {
        return ASIN;
    }

    public void setASIN(String ASIN) {
        this.ASIN = ASIN;
    }

    public String getSellerSKU() {
        return sellerSKU;
    }

    public void setSellerSKU(String sellerSKU) {
        this.sellerSKU = sellerSKU;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public int getQuantityShipped() {
        return quantityShipped;
    }

    public void setQuantityShipped(int quantityShipped) {
        this.quantityShipped = quantityShipped;
    }

    public MoneyDto getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(MoneyDto itemPrice) {
        this.itemPrice = itemPrice;
    }

    public MoneyDto getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(MoneyDto shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public MoneyDto getGiftWrapPrice() {
        return giftWrapPrice;
    }

    public void setGiftWrapPrice(MoneyDto giftWrapPrice) {
        this.giftWrapPrice = giftWrapPrice;
    }

    public MoneyDto getItemTax() {
        return itemTax;
    }

    public void setItemTax(MoneyDto itemTax) {
        this.itemTax = itemTax;
    }

    public MoneyDto getShippingTax() {
        return shippingTax;
    }

    public void setShippingTax(MoneyDto shippingTax) {
        this.shippingTax = shippingTax;
    }

    public MoneyDto getGiftWrapTax() {
        return giftWrapTax;
    }

    public void setGiftWrapTax(MoneyDto giftWrapTax) {
        this.giftWrapTax = giftWrapTax;
    }

    public MoneyDto getShippingDiscount() {
        return shippingDiscount;
    }

    public void setShippingDiscount(MoneyDto shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
    }

    public MoneyDto getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(MoneyDto promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public String getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(String promotionIds) {
        this.promotionIds = promotionIds;
    }

    public MoneyDto getCODFee() {
        return CODFee;
    }

    public void setCODFee(MoneyDto CODFee) {
        this.CODFee = CODFee;
    }

    public MoneyDto getCODFeeDiscount() {
        return CODFeeDiscount;
    }

    public void setCODFeeDiscount(MoneyDto CODFeeDiscount) {
        this.CODFeeDiscount = CODFeeDiscount;
    }

    public String getGiftMessageText() {
        return giftMessageText;
    }

    public void setGiftMessageText(String giftMessageText) {
        this.giftMessageText = giftMessageText;
    }

    public String getGiftWrapLevel() {
        return giftWrapLevel;
    }

    public void setGiftWrapLevel(String giftWrapLevel) {
        this.giftWrapLevel = giftWrapLevel;
    }

    public String getConditionNote() {
        return conditionNote;
    }

    public void setConditionNote(String conditionNote) {
        this.conditionNote = conditionNote;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getConditionSubtypeId() {
        return conditionSubtypeId;
    }

    public void setConditionSubtypeId(String conditionSubtypeId) {
        this.conditionSubtypeId = conditionSubtypeId;
    }

    public String getScheduledDeliveryStartDate() {
        return scheduledDeliveryStartDate;
    }

    public void setScheduledDeliveryStartDate(String scheduledDeliveryStartDate) {
        this.scheduledDeliveryStartDate = scheduledDeliveryStartDate;
    }

    public String getScheduledDeliveryEndDate() {
        return scheduledDeliveryEndDate;
    }

    public void setScheduledDeliveryEndDate(String scheduledDeliveryEndDate) {
        this.scheduledDeliveryEndDate = scheduledDeliveryEndDate;
    }

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "ASIN='" + ASIN + '\'' +
                ", sellerSKU='" + sellerSKU + '\'' +
                ", orderItemId='" + orderItemId + '\'' +
                ", title='" + title + '\'' +
                ", quantityOrdered=" + quantityOrdered +
                ", quantityShipped=" + quantityShipped +
                ", itemPrice=" + itemPrice +
                ", shippingPrice=" + shippingPrice +
                ", giftWrapPrice=" + giftWrapPrice +
                ", itemTax=" + itemTax +
                ", shippingTax=" + shippingTax +
                ", giftWrapTax=" + giftWrapTax +
                ", shippingDiscount=" + shippingDiscount +
                ", promotionDiscount=" + promotionDiscount +
                ", promotionIds='" + promotionIds + '\'' +
                ", CODFee=" + CODFee +
                ", CODFeeDiscount=" + CODFeeDiscount +
                ", giftMessageText='" + giftMessageText + '\'' +
                ", giftWrapLevel='" + giftWrapLevel + '\'' +
                ", conditionNote='" + conditionNote + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", conditionSubtypeId='" + conditionSubtypeId + '\'' +
                ", scheduledDeliveryStartDate='" + scheduledDeliveryStartDate + '\'' +
                ", scheduledDeliveryEndDate='" + scheduledDeliveryEndDate + '\'' +
                '}';
    }
}
