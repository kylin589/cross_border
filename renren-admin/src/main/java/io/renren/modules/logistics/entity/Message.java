package io.renren.modules.logistics.entity;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 14:24
 * @Description:
 */
//消息类
public class Message {

    private String messageID;//消息ID
    private OrderFulfillment orderFulfillment;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public OrderFulfillment getOrderFulfillment() {
        return orderFulfillment;
    }
    public void setOrderFulfillment(OrderFulfillment orderFulfillment) {
        this.orderFulfillment = orderFulfillment;
    }

    public Message() {
        super();
    }

}
