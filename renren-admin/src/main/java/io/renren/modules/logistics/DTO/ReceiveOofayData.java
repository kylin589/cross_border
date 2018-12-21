package io.renren.modules.logistics.dto;

import java.io.Serializable;

/**
 * @Auther: wdh
 * @Date: 2018/12/13 14:16
 * @Description:
 */
public class ReceiveOofayData implements Serializable {

    //订单状态 1备货；2缺货；4问题；3发货；5退款；6妥投；7代发；10物流问题
    public String statusStr;
    //OmsOrderDetailext.warehousing_record_list 如果有，并且storage_time不为null表示入库了
    //是否入库
    public boolean isWarehousing;
    //运费
    public String interFreight;
    //运单号
    public String abroadWaybill;
    //跟踪号
    public String trackWaybill;

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

    public String getAbroadWaybill() {
        return abroadWaybill;
    }

    public void setAbroadWaybill(String abroadWaybill) {
        this.abroadWaybill = abroadWaybill;
    }

    public String getTrackWaybill() {
        return trackWaybill;
    }

    public void setTrackWaybill(String trackWaybill) {
        this.trackWaybill = trackWaybill;
    }
}
