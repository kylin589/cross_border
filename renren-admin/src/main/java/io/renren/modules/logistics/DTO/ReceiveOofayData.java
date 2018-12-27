package io.renren.modules.logistics.DTO;

import java.io.Serializable;

/**
 * @Auther: wdh
 * @Date: 2018/12/13 14:16
 * @Description:
 */
public class ReceiveOofayData implements Serializable {

    //订单状态 1备货；2缺货；4问题；3发货；5退款；6妥投；7代发；10物流问题
    public String statusStr = "1";
    //OmsOrderDetailext.warehousing_record_list 如果有，并且storage_time不为null表示入库了
    //是否入库
    public boolean isWarehousing = false;
    //运费
    public String interFreight;
    //国外物流公司
    public String destTransportCompany;
    //国外物流渠道
    public String destChannel;
    //国内跟踪号
    public String domesticTrackWaybill;
    //国际跟踪号
    public String trackWaybill;
    //发货时间
    public String shipTime;
    //实际重量
    public String actualWeight;
    /**
     * 目的地查询网址
     */
    private String destQueryUrl;
    /**
     * 服务查询网址
     */
    private String serviceQueryUrl;
    /**
     * 联系电话
     */
    private String mobile;

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public boolean isWarehousing() {
        return isWarehousing;
    }

    public void setWarehousing(boolean warehousing) {
        isWarehousing = warehousing;
    }

    public String getInterFreight() {
        return interFreight;
    }

    public void setInterFreight(String interFreight) {
        this.interFreight = interFreight;
    }

    public String getTrackWaybill() {
        return trackWaybill;
    }

    public void setTrackWaybill(String trackWaybill) {
        this.trackWaybill = trackWaybill;
    }

    public String getDestTransportCompany() {
        return destTransportCompany;
    }

    public void setDestTransportCompany(String destTransportCompany) {
        this.destTransportCompany = destTransportCompany;
    }

    public String getDestChannel() {
        return destChannel;
    }

    public void setDestChannel(String destChannel) {
        this.destChannel = destChannel;
    }

    public String getDomesticTrackWaybill() {
        return domesticTrackWaybill;
    }

    public void setDomesticTrackWaybill(String domesticTrackWaybill) {
        this.domesticTrackWaybill = domesticTrackWaybill;
    }

    public String getShipTime() {
        return shipTime;
    }

    public void setShipTime(String shipTime) {
        this.shipTime = shipTime;
    }

    public String getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getDestQueryUrl() {
        return destQueryUrl;
    }

    public void setDestQueryUrl(String destQueryUrl) {
        this.destQueryUrl = destQueryUrl;
    }

    public String getServiceQueryUrl() {
        return serviceQueryUrl;
    }

    public void setServiceQueryUrl(String serviceQueryUrl) {
        this.serviceQueryUrl = serviceQueryUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
