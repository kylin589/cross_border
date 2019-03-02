package io.renren.modules.logistics.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：订单发货物流信息，主要用于绑定订单表中的订单Id,进行发货。
 */
public class Shipping {


    private Header header;//头

    private String messageType;//类型

    private List<Message> messages=new ArrayList<>();//实体类

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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Shipping() {
        super();
    }

}

