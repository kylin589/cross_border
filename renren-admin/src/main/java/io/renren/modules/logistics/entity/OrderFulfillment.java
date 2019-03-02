package io.renren.modules.logistics.entity;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 14:26
 * @Description:
 */
public class OrderFulfillment {
    private String amazonOrderID; //亚马逊订单ID

    private String fulfillmentDate;//完成日期
    private FulfillmentData fulfillmentData;//完成数据
    private Item item;//商品
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public OrderFulfillment() {
        super();
    }



}