package io.renren.modules.logistics.DTO;

import java.util.List;

/**
 * @Auther: wdh
 * @Date: 2018/12/13 14:16
 * @Description:
 */
public class OofayOrderData {

    public OmsOrder omsOrder;
    public List<OmsOrderDetail> orderDetailList;
    public OmsShippingAddr address;

    public OmsOrder getOmsOrder() {
        return omsOrder;
    }

    public void setOmsOrder(OmsOrder omsOrder) {
        this.omsOrder = omsOrder;
    }

    public List<OmsOrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OmsOrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public OmsShippingAddr getAddress() {
        return address;
    }

    public void setAddress(OmsShippingAddr address) {
        this.address = address;
    }
}
