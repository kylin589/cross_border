package io.renren.modules.amazon.dto;

import org.springframework.core.annotation.Order;

import java.util.List;

public class ListOrdersResponseDto {

    /**
     * 生成的字符串，用于将信息传递给下一请求。如果返回 NextToken，将 NextToken 的值传递给 ListOrdersByNextToken。如果没有返回 NextToken，则不返回其他配送订单。
     * 可选项
     */
    private String nextToken;

    /**
     * 如果您使用 LastUpdatedAfter 请求参数，将返回日期。如果您还使用 LastUpdatedBefore 请求参数，将返回您随同该请求参数提供的 日期。否则将返回 LastUpdatedBefore 请求参数的默认值：您的请求时间减去两分钟。
     * 可选。将返回 LastUpdatedBefore 或 CreatedBefore。
     */
    private String lastUpdatedBefore;

    /**
     * 如果您使用 CreatedAfter 请求参数，将返回日期。如果您还使用 CreatedBefore 请求参数，将返回您随同该请求参数提供的日期。否则将返回 CreatedBefore 请求参数的默认值：您的请求时间减去两分钟。
     * 可选。将返回 LastUpdatedBefore 或 CreatedBefore。
     */
    private String CreatedBefore;

    /**
     * 订单列表。
     */
    private List<OrderDto> orders;

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public String getLastUpdatedBefore() {
        return lastUpdatedBefore;
    }

    public void setLastUpdatedBefore(String lastUpdatedBefore) {
        this.lastUpdatedBefore = lastUpdatedBefore;
    }

    public String getCreatedBefore() {
        return CreatedBefore;
    }

    public void setCreatedBefore(String createdBefore) {
        CreatedBefore = createdBefore;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }
}
