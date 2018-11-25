package io.renren.modules.amazon.dto;

public class OrderDto {
    // TODO: 2018/11/19 日本缺少 PaymentExecutionDetail 参考：http://docs.developer.amazonservices.com/zh_CN/orders/2013-09-01/Orders_Datatypes.html#PaymentExecutionDetailItem

    /**
     * 亚马逊所定义的订单编码，格式为 3-7-7。
     */
    private String amazonOrderId;

    /**
     * 卖家所定义的订单编码。
     * 可选项
     */
    private String sellerOrderId;

    /**
     * 创建订单的日期。
     */
    private String purchaseDate;

    /**
     * 订单的最后更新日期。
     */
    private String lastUpdateDate;

    /**
     * 当前的订单状态。
     */
    private String orderStatus;

    /**
     * 订单配送方式：亚马逊配送 (AFN) 或卖家自行配送 (MFN)。
     * 可选项
     */
    private String fulfillmentChannel;

    /**
     * 订单中第一件商品的销售渠道。
     * 可选项
     */
    private String salesChannel;

    /**
     * 订单中第一件商品的订单渠道。
     * 可选项
     */
    private String orderChannel;

    /**
     * 货件服务水平。
     * 可选项
     */
    private String shipServiceLevel;

    /**
     * 订单的配送地址。名称。
     */
    private String name;

    /**
     * 订单的配送地址。街道地址。
     */
    private String addressLine1;

    /**
     * 其他街道地址信息（如果需要）。
     */
    private String addressLine2;

    /**
     * 其他街道地址信息（如果需要）。
     */
    private String addressLine3;

    /**
     * 城市。
     */
    private String city;

    /**
     * 区县。
     */
    private String county;

    /**
     * 区。
     */
    private String district;

    /**
     * 省/自治区/直辖市或地区。
     */
    private String stateOrRegion;

    /**
     * 邮政编码。
     */
    private String postalCode;

    /**
     * 两位数国家/地区代码。格式为 ISO 3166-1-alpha 2 。
     */
    private String countryCode;
    /**
     * 电话
     * jhy
     */
    private String phone;



    /**
     * 地址类型
     * jhy
     */

    private String addressType;
    /**
     * 订单费用。三位数的货币代码。格式为 ISO 4217。
     */
    private String currencyCode;

    /**
     * 订单费用。货币金额
     */
    private String amount;

    /**
     * 已配送的商品数量。
     * 可选项
     */
    private String numberOfItemsShipped;

    /**
     * 未配送的商品数量。
     * 可选项
     */
    private String numberOfItemsUnshipped;

    /**
     * 订单的主要付款方式。
     * PaymentMethod 值：
     * <p>
     * COD - 货到付款。仅适用于中国 (CN) 和日本 (JP)。
     * CVS - 便利店。仅适用于日本 (JP)。
     * Other - COD 和 CVS 之外的付款方式。
     * 注： 可使用多种次级付款方式为 PaymentMethod = COD的订单付款。每种次级付款方式均表示为 PaymentExecutionDetailItem 对象。
     * 可选项
     */
    private String paymentMethod;
    /**
     * jhy
     */
    private String paymentMethodDetail;


    /**
     * 订单生成所在商城的匿名编码。
     */
    private String marketplaceId;

    /**
     * 买家的匿名电子邮件地址。
     * 可选项
     */
    private String buyerEmail;

    /**
     * 买家姓名。
     * 可选项
     */
    private String buyerName;

    /**
     * 订单的配送服务级别分类。
     * ShipmentServiceLevelCategory 值：
     * <p>
     * Expedited
     * FreeEconomy
     * NextDay
     * SameDay
     * SecondDay
     * Scheduled
     * Standard
     * <p>
     * 可选项
     */
    private String shipmentServiceLevelCategory;

    /**
     * 卖家自定义的配送方式，属于Checkout by Amazon (CBA) 所支持的四种标准配送设置中的一种。有关更多信息，请参阅您所在商城 Amazon Payments 帮助中心的“设置灵活配送方式”主题。
     * 注： CBA 仅适用于美国 (US)、英国 (UK) 和德国 (DE) 的卖家。
     * 可选项
     */
    private String cbaDisplayableShippingLabel;
    /**
     * jhy
     */
    private  String shippedByAmazonTFM;



    /**
     * 订单类型。
     * OrderType 值：
     * <p>
     * StandardOrder - 包含当前有库存商品的订单。
     * Preorder -所含预售商品（发布日期晚于当前日期）的订单。
     * 注： Preorder 仅在日本 (JP) 是可行的OrderType 值。
     */
    private String orderType;

    /**
     * 您承诺的订单发货时间范围的第一天。日期格式为 ISO 8601。
     * 可选。仅对卖家配送网络 (MFN) 订单返回。
     * <p>
     * 注： 可能不会对 2013 年 2 月 1 日之前的订单返回 EarliestShipDate。
     */
    private String earliestShipDate;

    /**
     * 您承诺的订单发货时间范围的最后一天。日期格式为 ISO 8601。
     * 可选。对卖家配送网络 (MFN)	和亚马逊物流 (AFN) 订单返回。
     * <p>
     * 注： 可能不会对 2013 年 2 月 1 日之前的订单返回 LatestShipDate 。
     */
    private String latestShipDate;

    /**
     * 您承诺的订单送达时间范围的第一天。日期格式为 ISO 8601。
     * 可选。仅对没有 PendingAvailability、Pending 或 Canceled状态的 MFN 订单返回。
     */
    private String earliestDeliveryDate;

    /**
     * 您承诺的订单送达时间范围的最后一天。日期格式为 ISO 8601。
     * 可选。仅对没有 PendingAvailability、Pending 或 Canceled状态的 MFN 订单返回。
     */
    private String latestDeliveryDate;

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public String getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(String sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFulfillmentChannel() {
        return fulfillmentChannel;
    }

    public void setFulfillmentChannel(String fulfillmentChannel) {
        this.fulfillmentChannel = fulfillmentChannel;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(String orderChannel) {
        this.orderChannel = orderChannel;
    }

    public String getShipServiceLevel() {
        return shipServiceLevel;
    }

    public void setShipServiceLevel(String shipServiceLevel) {
        this.shipServiceLevel = shipServiceLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStateOrRegion() {
        return stateOrRegion;
    }

    public void setStateOrRegion(String stateOrRegion) {
        this.stateOrRegion = stateOrRegion;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNumberOfItemsShipped() {
        return numberOfItemsShipped;
    }

    public void setNumberOfItemsShipped(String numberOfItemsShipped) {
        this.numberOfItemsShipped = numberOfItemsShipped;
    }

    public String getNumberOfItemsUnshipped() {
        return numberOfItemsUnshipped;
    }

    public void setNumberOfItemsUnshipped(String numberOfItemsUnshipped) {
        this.numberOfItemsUnshipped = numberOfItemsUnshipped;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    public void setMarketplaceId(String marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getShipmentServiceLevelCategory() {
        return shipmentServiceLevelCategory;
    }

    public void setShipmentServiceLevelCategory(String shipmentServiceLevelCategory) {
        this.shipmentServiceLevelCategory = shipmentServiceLevelCategory;
    }

    public String getCbaDisplayableShippingLabel() {
        return cbaDisplayableShippingLabel;
    }

    public void setCbaDisplayableShippingLabel(String cbaDisplayableShippingLabel) {
        this.cbaDisplayableShippingLabel = cbaDisplayableShippingLabel;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getEarliestShipDate() {
        return earliestShipDate;
    }

    public void setEarliestShipDate(String earliestShipDate) {
        this.earliestShipDate = earliestShipDate;
    }

    public String getLatestShipDate() {
        return latestShipDate;
    }

    public void setLatestShipDate(String latestShipDate) {
        this.latestShipDate = latestShipDate;
    }

    public String getEarliestDeliveryDate() {
        return earliestDeliveryDate;
    }

    public void setEarliestDeliveryDate(String earliestDeliveryDate) {
        this.earliestDeliveryDate = earliestDeliveryDate;
    }

    public String getLatestDeliveryDate() {
        return latestDeliveryDate;
    }

    public void setLatestDeliveryDate(String latestDeliveryDate) {
        this.latestDeliveryDate = latestDeliveryDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
    public String getPaymentMethodDetail() {
        return paymentMethodDetail;
    }

    public void setPaymentMethodDetail(String paymentMethodDetail) {
        this.paymentMethodDetail = paymentMethodDetail;
    }
    public String getShippedByAmazonTFM() {
        return shippedByAmazonTFM;
    }

    public void setShippedByAmazonTFM(String shippedByAmazonTFM) {
        this.shippedByAmazonTFM = shippedByAmazonTFM;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "amazonOrderId='" + amazonOrderId + '\'' +
                ", sellerOrderId='" + sellerOrderId + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", fulfillmentChannel='" + fulfillmentChannel + '\'' +
                ", salesChannel='" + salesChannel + '\'' +
                ", orderChannel='" + orderChannel + '\'' +
                ", shipServiceLevel='" + shipServiceLevel + '\'' +
                ", name='" + name + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", addressLine3='" + addressLine3 + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", district='" + district + '\'' +
                ", stateOrRegion='" + stateOrRegion + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", phone='" + phone + '\'' +
                ", addressType='" + addressType + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", amount='" + amount + '\'' +
                ", numberOfItemsShipped='" + numberOfItemsShipped + '\'' +
                ", numberOfItemsUnshipped='" + numberOfItemsUnshipped + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentMethodDetail='" + paymentMethodDetail + '\'' +
                ", marketplaceId='" + marketplaceId + '\'' +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", shipmentServiceLevelCategory='" + shipmentServiceLevelCategory + '\'' +
                ", cbaDisplayableShippingLabel='" + cbaDisplayableShippingLabel + '\'' +
                ", shippedByAmazonTFM='" + shippedByAmazonTFM + '\'' +
                ", orderType='" + orderType + '\'' +
                ", earliestShipDate='" + earliestShipDate + '\'' +
                ", latestShipDate='" + latestShipDate + '\'' +
                ", earliestDeliveryDate='" + earliestDeliveryDate + '\'' +
                ", latestDeliveryDate='" + latestDeliveryDate + '\'' +
                '}';
    }
}
