package io.renren.modules.logistics.entity;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 14:25
 * @Description:
 */
//配送数据
public class FulfillmentData {

    private String carrierName;//物流名称

    private String shippingMethod;//物流配送方式

    private String shipperTrackingNumber;//物流追踪号码

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }


    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getShipperTrackingNumber() {
        return shipperTrackingNumber;
    }

    public void setShipperTrackingNumber(String shipperTrackingNumber) {
        this.shipperTrackingNumber = shipperTrackingNumber;
    }

    public FulfillmentData() {
        super();
    }


}