package io.renren.modules.amazon.service;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import io.renren.modules.product.entity.OrderEntity;

import java.util.List;

public interface ListOrdersAsyncService {

    void listOrdersRequest(String sellerId, String mwsAuthToken, Integer region, String lastUpdatedAfterStr);

    /**
     * 请求订单列表,返回您在指定时间段内所创建或更新的订单。
     *
     * @param client      客户端
     * @param requestList 请求参数
     * @return 响应对象列表，包括订单列表xml
     * @auther zjr
     * @date 2018-11-21 9:54
     */
    List<Object> invokeListOrders(MarketplaceWebServiceOrdersAsync client, List<ListOrdersRequest> requestList);

    /**
     * 请求订单列表的下一页,使用 NextToken 参数返回下一页订单。
     *
     * @param client      客户端
     * @param requestList 请求参数
     * @return 响应对象列表, 包括订单列表xml
     * @auther zjr
     * @date 2018-11-21 9:54
     */
    List<Object> invokeListOrdersByNextToken(MarketplaceWebServiceOrdersAsync client, List<ListOrdersByNextTokenRequest> requestList);

    /**
     * 请求订单详情，根据您指定的 AmazonOrderId 值返回订单。
     *
     * @param client      客户端
     * @param requestList 请求参数
     * @return 响应对象列表, 包括订单详情xml
     * @auther zjr
     * @date 2018-11-21 9:54
     */
    List<Object> invokeGetOrder(MarketplaceWebServiceOrdersAsync client, List<GetOrderRequest> requestList);

    /**
     * 根据您指定的 AmazonOrderId 返回订单商品。
     *
     * @param client      客户端
     * @param requestList 请求参数
     * @return 响应对象列表, 包括订单中商品详情xml
     * @auther zjr
     * @date 2018-11-21 9:54
     */
    List<Object> invokeListOrderItems(MarketplaceWebServiceOrdersAsync client, List<ListOrderItemsRequest> requestList);

    /**
     * 使用 NextToken 参数返回下一页订单商品。
     *
     * @param client      客户端
     * @param requestList 请求参数
     * @return 响应对象列表, 包括订单中商品详情xml
     * @auther zjr
     * @date 2018-11-21 9:54
     */
    List<Object> invokeListOrderItemsByNextToken(MarketplaceWebServiceOrdersAsync client, List<ListOrderItemsByNextTokenRequest> requestList);

    /**
     * 返回 “订单 API”部分 的运行状态。
     *
     * @param client      客户端
     * @param requestList 请求参数
     * @return 响应对象列表, 包括运行状态详情xml
     * @auther zjr
     * @date 2018-11-21 9:54
     */
    List<Object> invokeGetServiceStatus(MarketplaceWebServiceOrdersAsync client, List<GetServiceStatusRequest> requestList);

    List<OrderEntity> analysisListOrders(List<Object> responseList);
}