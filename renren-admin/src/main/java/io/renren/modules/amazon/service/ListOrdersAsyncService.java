package io.renren.modules.amazon.service;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersRequest;
import io.renren.modules.product.entity.OrderEntity;

import java.util.List;

public interface ListOrdersAsyncService {

    List<Object> invokeListOrders(MarketplaceWebServiceOrdersAsync client, List<ListOrdersRequest> requestList);

    List<Object> invokeListOrdersByNextToken(MarketplaceWebServiceOrdersAsync client, List<ListOrdersByNextTokenRequest> requestList) ;

    List<OrderEntity> analysis(List<Object> responseList);
}