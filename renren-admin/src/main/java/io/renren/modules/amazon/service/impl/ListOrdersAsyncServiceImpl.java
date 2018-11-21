package io.renren.modules.amazon.service.impl;

import java.util.*;
import java.util.concurrent.*;

import com.amazonservices.mws.orders._2013_09_01.*;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import io.renren.modules.amazon.service.ListOrdersAsyncService;
import io.renren.modules.product.entity.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("listOrdersAsyncService")
public class ListOrdersAsyncServiceImpl implements ListOrdersAsyncService {

    @Override
    public List<Object> invokeListOrders(MarketplaceWebServiceOrdersAsync client, List<ListOrdersRequest> requestList) {
        // Call the service async.
        List<Future<ListOrdersResponse>> futureList = new ArrayList<Future<ListOrdersResponse>>();
        for (ListOrdersRequest request : requestList) {
            Future<ListOrdersResponse> future = client.listOrdersAsync(request);
            futureList.add(future);
        }
        List<Object> responseList = new ArrayList<Object>();
        for (Future<ListOrdersResponse> future : futureList) {
            Object xresponse;
            try {
                ListOrdersResponse response = future.get();
                ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
                // We recommend logging every the request id and timestamp of every call.
                System.out.println("Response:");
                System.out.println("RequestId: " + rhmd.getRequestId());
                System.out.println("Timestamp: " + rhmd.getTimestamp());
                String responseXml = response.toXML();
                System.out.println(responseXml);
                xresponse = response;
            } catch (ExecutionException ee) {
                Throwable cause = ee.getCause();
                if (cause instanceof MarketplaceWebServiceOrdersException) {
                    // Exception properties are important for diagnostics.
                    MarketplaceWebServiceOrdersException ex = (MarketplaceWebServiceOrdersException) cause;
                    ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
                    System.out.println("Service Exception:");
                    System.out.println("RequestId: " + rhmd.getRequestId());
                    System.out.println("Timestamp: " + rhmd.getTimestamp());
                    System.out.println("Message: " + ex.getMessage());
                    System.out.println("StatusCode: " + ex.getStatusCode());
                    System.out.println("ErrorCode: " + ex.getErrorCode());
                    System.out.println("ErrorType: " + ex.getErrorType());
                    xresponse = ex;
                } else {
                    xresponse = cause;
                }
            } catch (Exception e) {
                xresponse = e;
            }
            responseList.add(xresponse);
        }
        return responseList;
    }

    @Override
    public List<Object> invokeListOrdersByNextToken(MarketplaceWebServiceOrdersAsync client, List<ListOrdersByNextTokenRequest> requestList) {
        // Call the service async.
        List<Future<ListOrdersByNextTokenResponse>> futureList =
                new ArrayList<Future<ListOrdersByNextTokenResponse>>();
        for (ListOrdersByNextTokenRequest request : requestList) {
            Future<ListOrdersByNextTokenResponse> future =
                    client.listOrdersByNextTokenAsync(request);
            futureList.add(future);
        }
        List<Object> responseList = new ArrayList<Object>();
        for (Future<ListOrdersByNextTokenResponse> future : futureList) {
            Object xresponse;
            try {
                ListOrdersByNextTokenResponse response = future.get();
                ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
                // We recommend logging every the request id and timestamp of every call.
                System.out.println("Response:");
                System.out.println("RequestId: "+rhmd.getRequestId());
                System.out.println("Timestamp: "+rhmd.getTimestamp());
                String responseXml = response.toXML();
                System.out.println(responseXml);
                xresponse = response;
            } catch (ExecutionException ee) {
                Throwable cause = ee.getCause();
                if (cause instanceof MarketplaceWebServiceOrdersException) {
                    // Exception properties are important for diagnostics.
                    MarketplaceWebServiceOrdersException ex =
                            (MarketplaceWebServiceOrdersException)cause;
                    ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
                    System.out.println("Service Exception:");
                    System.out.println("RequestId: "+rhmd.getRequestId());
                    System.out.println("Timestamp: "+rhmd.getTimestamp());
                    System.out.println("Message: "+ex.getMessage());
                    System.out.println("StatusCode: "+ex.getStatusCode());
                    System.out.println("ErrorCode: "+ex.getErrorCode());
                    System.out.println("ErrorType: "+ex.getErrorType());
                    xresponse = ex;
                } else {
                    xresponse = cause;
                }
            } catch (Exception e) {
                xresponse = e;
            }
            responseList.add(xresponse);
        }
        return responseList;
    }

    @Override
    public List<OrderEntity> analysis(List<Object> responseList) {
        // TODO: 2018/11/20 解析 responseList，调用 xml 方法
        for (int i = 0; i< responseList.size(); i++){

        }
        return null;
    }
}
