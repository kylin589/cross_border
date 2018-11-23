package io.renren.modules.amazon.dto;

import java.util.List;

public class ListOrderItemsByNextTokenResponseDto {
    /**
     * 生成的字符串，用于将信息传递给下一请求。如果返回 NextToken，将 NextToken 的值传递给 ListOrderItemsByNextToken。如果没有返回 NextToken，则不返回其他订单商品。
     * 可选项
     */
    private String nextToken;


    /**
     * 亚马逊所定义的订单编码，格式 为 3-7-7。
     */
    private String amazonOrderId;

    /**
     * 订单商品列表。
     */
    private List<OrderItemDto> orderItems;

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "ListOrderItemsByNextTokenResponseDto{" +
                "nextToken='" + nextToken + '\'' +
                ", amazonOrderId='" + amazonOrderId + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }
}
