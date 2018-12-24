package io.renren.modules.amazon.service.impl;

import java.util.*;
import java.util.concurrent.*;

import com.amazonservices.mws.orders._2013_09_01.*;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import io.renren.modules.amazon.config.MarketplaceWebServiceOrdersSampleConfig;
import io.renren.common.utils.DateUtils;
import io.renren.modules.amazon.dto.ListOrdersResponseDto;
import io.renren.modules.amazon.entity.AmazonMarketplaceEntity;
import io.renren.modules.amazon.service.AmazonMarketplaceService;
import io.renren.modules.amazon.service.ListOrdersAsyncService;
import io.renren.modules.amazon.util.XMLUtil;
import io.renren.modules.product.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@Service("listOrdersAsyncService")
public class ListOrdersAsyncServiceImpl implements ListOrdersAsyncService {

    @Autowired
    private AmazonMarketplaceService amazonMarketplaceService;

    @Override
    public void listOrdersRequest(String sellerId, String mwsAuthToken, Integer region, String lastUpdatedAfterStr) {

        Map<String, Object> map = new HashMap<>();
        map.put("region", region);
        List<AmazonMarketplaceEntity> amazonMarketplaceEntityList = amazonMarketplaceService.selectByMap(map);

        String serviceURL = amazonMarketplaceEntityList.get(0).getMwsPoint();

        // Get a client connection.
        MarketplaceWebServiceOrdersAsyncClient client = MarketplaceWebServiceOrdersSampleConfig.getAsyncClient(serviceURL);

        // Create a request list.
        List<ListOrdersRequest> requestList = new ArrayList<ListOrdersRequest>();
        ListOrdersRequest request = new ListOrdersRequest();

        request.setSellerId(sellerId);

        request.setMWSAuthToken(mwsAuthToken);

        // 指定某一格式为 ISO 8601 的日期，用以选择最后更新日期为该日期之后（或当天）的订单。更新即为对订单状态进行更改，包括新订单的创建。包括亚马逊和卖家所进行的更新。必须不迟于两分钟，且在请求提交时间之前。
        XMLGregorianCalendar lastUpdatedAfter = DateUtils.getTheDateNow45DaysShort();
        request.setLastUpdatedAfter(lastUpdatedAfter);

        // 店铺ID
        List<String> marketplaceId = new ArrayList<>();
        for (AmazonMarketplaceEntity am : amazonMarketplaceEntityList) {
            marketplaceId.add(am.getMarketplaceId());
        }
        request.setMarketplaceId(marketplaceId);
        requestList.add(request);

        Boolean isSuccess = false;
        // 写道这里
        List<ListOrdersResponseDto> listOrdersResponseDtos = new ArrayList<>();
        ListOrdersResponseDto listOrdersResponseDto = null;

        List<Object> responseList = invokeListOrders(client, requestList);
        for (Object tempResponse : requestList) {
            // Object 转换 ListOrdersResponse 还是 MarketplaceWebServiceOrdersException
            String className = tempResponse.getClass().getName();
            if ((MarketplaceWebServiceOrdersException.class.getName()).equals(className) == true) {
                System.out.println("responseList 类型是 MarketplaceWebServiceOrdersException。");
                isSuccess = false;
                break;
            } else if ((ListOrdersResponse.class.getName()).equals(className) == true) {
                System.out.println("responseList 类型是 ListOrdersResponse。");
                ListOrdersResponse response = (ListOrdersResponse) tempResponse;
                listOrdersResponseDto = XMLUtil.analysisListOrdersResponse(response.toXML());
                isSuccess = true;
            }
        }

        listOrdersResponseDtos.add(listOrdersResponseDto);
        if (listOrdersResponseDto.getNextToken() != null && isSuccess == true) {
            ListOrdersResponseDto listOrdersByNextTokenResponseDto;
            List<ListOrdersByNextTokenRequest> listOrdersByNextTokenRequests = new ArrayList<ListOrdersByNextTokenRequest>();
            ListOrdersByNextTokenRequest listOrdersByNextTokenRequest = new ListOrdersByNextTokenRequest();
            listOrdersByNextTokenRequest.setSellerId(sellerId);
            listOrdersByNextTokenRequest.setMWSAuthToken(mwsAuthToken);
            listOrdersByNextTokenRequest.setNextToken(listOrdersResponseDto.getNextToken());
            listOrdersByNextTokenRequests.add(listOrdersByNextTokenRequest);

            // 写道这里
            invokeListOrdersByNextToken(client, listOrdersByNextTokenRequests);
        }
    }

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
        List<Future<ListOrdersByNextTokenResponse>> futureList = new ArrayList<Future<ListOrdersByNextTokenResponse>>();
        for (ListOrdersByNextTokenRequest request : requestList) {
            Future<ListOrdersByNextTokenResponse> future = client.listOrdersByNextTokenAsync(request);
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
                System.out.println("RequestId: " + rhmd.getRequestId());
                System.out.println("Timestamp: " + rhmd.getTimestamp());
                String responseXml = response.toXML();
                System.out.println(responseXml);
                xresponse = response;
            } catch (ExecutionException ee) {
                Throwable cause = ee.getCause();
                if (cause instanceof MarketplaceWebServiceOrdersException) {
                    // TODO: 2018/11/21 报错怎么办？
                    // Exception properties are important for diagnostics.
                    MarketplaceWebServiceOrdersException ex =
                            (MarketplaceWebServiceOrdersException) cause;
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
    public List<Object> invokeGetOrder(MarketplaceWebServiceOrdersAsync client, List<GetOrderRequest> requestList) {
        // Call the service async.
        List<Future<GetOrderResponse>> futureList =
                new ArrayList<Future<GetOrderResponse>>();
        for (GetOrderRequest request : requestList) {
            Future<GetOrderResponse> future =
                    client.getOrderAsync(request);
            futureList.add(future);
        }
        List<Object> responseList = new ArrayList<Object>();
        for (Future<GetOrderResponse> future : futureList) {
            Object xresponse;
            try {
                GetOrderResponse response = future.get();
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
                    MarketplaceWebServiceOrdersException ex =
                            (MarketplaceWebServiceOrdersException) cause;
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
    public List<Object> invokeListOrderItems(MarketplaceWebServiceOrdersAsync client, List<ListOrderItemsRequest> requestList) {
        // Call the service async.
        List<Future<ListOrderItemsResponse>> futureList =
                new ArrayList<Future<ListOrderItemsResponse>>();
        for (ListOrderItemsRequest request : requestList) {
            Future<ListOrderItemsResponse> future =
                    client.listOrderItemsAsync(request);
            futureList.add(future);
        }
        List<Object> responseList = new ArrayList<Object>();
        for (Future<ListOrderItemsResponse> future : futureList) {
            Object xresponse;
            try {
                ListOrderItemsResponse response = future.get();
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
                    MarketplaceWebServiceOrdersException ex =
                            (MarketplaceWebServiceOrdersException) cause;
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
    public List<Object> invokeListOrderItemsByNextToken(MarketplaceWebServiceOrdersAsync client, List<ListOrderItemsByNextTokenRequest> requestList) {
        // Call the service async.
        List<Future<ListOrderItemsByNextTokenResponse>> futureList =
                new ArrayList<Future<ListOrderItemsByNextTokenResponse>>();
        for (ListOrderItemsByNextTokenRequest request : requestList) {
            Future<ListOrderItemsByNextTokenResponse> future =
                    client.listOrderItemsByNextTokenAsync(request);
            futureList.add(future);
        }
        List<Object> responseList = new ArrayList<Object>();
        for (Future<ListOrderItemsByNextTokenResponse> future : futureList) {
            Object xresponse;
            try {
                ListOrderItemsByNextTokenResponse response = future.get();
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
                    MarketplaceWebServiceOrdersException ex =
                            (MarketplaceWebServiceOrdersException) cause;
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
    public List<Object> invokeGetServiceStatus(MarketplaceWebServiceOrdersAsync client, List<GetServiceStatusRequest> requestList) {
        // Call the service async.
        List<Future<GetServiceStatusResponse>> futureList =
                new ArrayList<Future<GetServiceStatusResponse>>();
        for (GetServiceStatusRequest request : requestList) {
            Future<GetServiceStatusResponse> future =
                    client.getServiceStatusAsync(request);
            futureList.add(future);
        }
        List<Object> responseList = new ArrayList<Object>();
        for (Future<GetServiceStatusResponse> future : futureList) {
            Object xresponse;
            try {
                GetServiceStatusResponse response = future.get();
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
                    MarketplaceWebServiceOrdersException ex =
                            (MarketplaceWebServiceOrdersException) cause;
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
    public List<OrderEntity> analysisListOrders(List<Object> responseList) {
        // TODO: 2018/11/20 解析 responseList，调用 xml 方法
        for (int i = 0; i < responseList.size(); i++) {

        }
        return null;
    }
}
