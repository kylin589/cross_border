package io.renren.modules.logistics.entity;

/**
 * 功能描述：订单发货物流信息，主要用于绑定订单表中的订单Id,进行发货。
 */
public class Shipping {


    private Header header;//头

    private String messageType;//类型

    private Message message;//实体类

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }


    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }



    public Shipping() {
        super();
    }

}

