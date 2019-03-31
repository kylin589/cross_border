package io.renren.modules.logistics.DTO;

import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

/**
 * 订单请求数据包类
 */
public class OrderRequestData {
    private String OrderNumber;//客户订单号 必填
    private String ShippingMethodCode;//发货方式 必填
    private String TrackingNumber;//包裹跟踪号
    private String TransactionNumber;//平台交易号（wish邮）
    private int Length;//预估包裹单边长，单位cm，非必填，默认1
    private int Width;//预估包裹单边宽，单位cm，非必填，默认1
    private int Height;//预估包裹单边高，单位cm，非必填，默认1
    private int PackageNumber;//运单包裹的件数，必须大于0的整数 必填
    private BigDecimal Weight;//预估包裹总重量，单位kg,最多3位小数 必填
    private ShippingInfo shippingInfo;//收件人信息 必填
    private SenderInfo senderInfo;//发件人信息
    private int ApplicationType;//申报类型,用于打印CN22，1-Gift,2-Sameple,3-Documents,4-Others,默认4-Other
    private boolean IsReturn;//是否退回,包裹无人签收时是否退回，1-退回，0-不退回，默认0
    private boolean EnableTariffPrepay;//关税预付服务费，1-参加关税预付，0-不参加关税预付，默认0 (渠道需开通关税预付服务)
    private int InsuranceType;//包裹投保类型，0-不参保，1-按件，2-按比例，默认0，表示不参加运输保险，具体参考包裹运输
    private BigDecimal InsureAmount;//保险的最高额度，单位RMB
    private int SensitiveTypeID;//包裹中特殊货品类型，可调用货品类型查询服务查询，可以不填写，表示普通货品
    private ApplicationInfos[] applicationInfos;//详情
    private String SourceCode;//订单来源代码

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getShippingMethodCode() {
        return ShippingMethodCode;
    }

    public void setShippingMethodCode(String shippingMethodCode) {
        ShippingMethodCode = shippingMethodCode;
    }

    public String getTrackingNumber() {
        return TrackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        TrackingNumber = trackingNumber;
    }

    public String getTransactionNumber() {
        return TransactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        TransactionNumber = transactionNumber;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getPackageNumber() {
        return PackageNumber;
    }

    public void setPackageNumber(int packageNumber) {
        PackageNumber = packageNumber;
    }


    public ShippingInfo getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public SenderInfo getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(SenderInfo senderInfo) {
        this.senderInfo = senderInfo;
    }

    public int getApplicationType() {
        return ApplicationType;
    }

    public void setApplicationType(int applicationType) {
        ApplicationType = applicationType;
    }

    public boolean isReturn() {
        return IsReturn;
    }

    public void setReturn(boolean aReturn) {
        IsReturn = aReturn;
    }

    public boolean isEnableTariffPrepay() {
        return EnableTariffPrepay;
    }

    public void setEnableTariffPrepay(boolean enableTariffPrepay) {
        EnableTariffPrepay = enableTariffPrepay;
    }

    public int getInsuranceType() {
        return InsuranceType;
    }

    public void setInsuranceType(int insuranceType) {
        InsuranceType = insuranceType;
    }

    public BigDecimal getWeight() {
        return Weight;
    }

    public void setWeight(BigDecimal weight) {
        Weight = weight;
    }

    public BigDecimal getInsureAmount() {
        return InsureAmount;
    }

    public void setInsureAmount(BigDecimal insureAmount) {
        InsureAmount = insureAmount;
    }

    public int getSensitiveTypeID() {
        return SensitiveTypeID;
    }

    public void setSensitiveTypeID(int sensitiveTypeID) {
        SensitiveTypeID = sensitiveTypeID;
    }

    public ApplicationInfos[] getApplicationInfos() {
        return applicationInfos;
    }

    public void setApplicationInfos(ApplicationInfos[] applicationInfos) {
        this.applicationInfos = applicationInfos;
    }

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String sourceCode) {
        SourceCode = sourceCode;
    }
}
