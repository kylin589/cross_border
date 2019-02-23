package io.renren.modules.logistics.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 14:26
 * @Description:
 */
public class OrderFulfillment {
    private String amazonOrderID; //亚马逊订单ID

    private String fulfillmentDate;//完成日期
    private FulfillmentData fulfillmentData;//完成数据
    private List<Item> items=new ArrayList<>();//商品
    public String getAmazonOrderID() {
        return amazonOrderID;
    }

    public void setAmazonOrderID(String amazonOrderID) {
        this.amazonOrderID = amazonOrderID;
    }
    public String getFulfillmentDate() {
        return fulfillmentDate;
    }
    public void setFulfillmentDate(String fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }

    public FulfillmentData getFulfillmentData() {
        return fulfillmentData;
    }

    public void setFulfillmentData(FulfillmentData fulfillmentData) {
        this.fulfillmentData = fulfillmentData;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public OrderFulfillment() {
        super();
    }



}