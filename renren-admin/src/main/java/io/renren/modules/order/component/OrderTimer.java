package io.renren.modules.order.component;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import io.renren.common.utils.DateUtils;
import io.renren.modules.amazon.dto.ListOrderItemsByNextTokenResponseDto;
import io.renren.modules.amazon.dto.ListOrdersResponseDto;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.util.XMLUtil;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.product.vm.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;



@Component("OrderTimer")
public class OrderTimer {
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;

    @Autowired
    private AmazonGrantService amazonGrantService;

    @Autowired
    private OrderService orderService;

    /**
     * 功能描述：发送订单请求，返回订单列表的响应数据
     *
     *
     */

    public void listOrdersRequest() {

        Map<String, Object> map = new HashMap<>();
        map.put("region", 0);
        List<AmazonGrantEntity> grantList=amazonGrantService.selectByMap(map);
        aaa:  for(AmazonGrantEntity grant:grantList){


            String sellerId=grant.getMerchantId();//获得商家id
            String mwsAuthToken=grant.getGrantToken();//获得授权Token
            List<AmazonGrantShopEntity> shopList=amazonGrantShopService.selectByMap(map);
            // 店铺ID
            List<String> marketplaceId = new ArrayList<>();
            //循环获取店铺授权
            for(AmazonGrantShopEntity shop: shopList){
                String serviceURL = shop.getMwsPoint();
                marketplaceId.add(shop.getMarketplaceId());//获取MarketplaceId值
                String shopName=shop.getShopName();//获得店铺名称
                // Get a client connection.
/*
                MarketplaceWebServiceOrdersAsyncClient client = MarketplaceWebServiceOrdersSampleConfig.getAsyncClient(serviceURL);
*/
                MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
                config.setServiceURL(serviceURL);
                // Set other client connection configurations here.
                MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                        "my_test", "1.0", config, null);
                // Create a request list.
                List<ListOrdersRequest> requestList = new ArrayList<ListOrdersRequest>();
                ListOrdersRequest request = new ListOrdersRequest();
                request.setSellerId(sellerId);//卖家id
                request.setMWSAuthToken(mwsAuthToken);//授权令牌

                // 指定某一格式为 ISO 8601 的日期，用以选择最后更新日期为该日期之后（或当天）的订单。更新即为对订单状态进行更改，包括新订单的创建。包括亚马逊和卖家所进行的更新。必须不迟于两分钟，且在请求提交时间之前。
                XMLGregorianCalendar lastUpdatedAfter = DateUtils.getTheDateNow10DaysShort();
                request.setLastUpdatedAfter(lastUpdatedAfter);//最近更新日期



                request.setMarketplaceId(marketplaceId);//国家市场编码
                requestList.add(request);

                List<Object> responseList = invokeListOrders(client, requestList);//获得订单列表的
                Boolean isSuccess = false;
                List<ListOrdersResponseDto> listOrdersResponseDtos = new ArrayList<>();
                ListOrdersResponseDto listOrdersResponseDto = null;
                for (Object tempResponse : responseList) {
                    // Object 转换 ListOrdersResponse 还是 MarketplaceWebServiceOrdersException
                    String className = tempResponse.getClass().getName();
                    if ((MarketplaceWebServiceOrdersException.class.getName()).equals(className) == true) {
                        System.out.println("responseList 类型是 MarketplaceWebServiceOrdersException。");
                        isSuccess = false;
                        continue aaa;
                    } else if ((ListOrdersResponse.class.getName()).equals(className) == true) {
                        System.out.println("responseList 类型是 ListOrdersResponse。");
                        ListOrdersResponse response = (ListOrdersResponse) tempResponse;
                        listOrdersResponseDto = XMLUtil.analysisListOrdersResponse(response.toXML());
                        isSuccess = true;
                    }
                }
                listOrdersResponseDtos.add(listOrdersResponseDto);//封装解析出来的
                while (listOrdersResponseDto.getNextToken() != null && isSuccess == true) {
                    //只有有NextToken标识，就一直获取
                    //判断下一页的订单数据
                    ListOrdersResponseDto listOrdersByNextTokenResponseDto=null;
                    List<ListOrdersByNextTokenRequest> listOrdersByNextTokenRequests = new ArrayList<ListOrdersByNextTokenRequest>();
                    ListOrdersByNextTokenRequest listOrdersByNextTokenRequest = new ListOrdersByNextTokenRequest();
                    listOrdersByNextTokenRequest.setSellerId(sellerId);
                    listOrdersByNextTokenRequest.setMWSAuthToken(mwsAuthToken);
                    listOrdersByNextTokenRequest.setNextToken(listOrdersResponseDto.getNextToken());
                    listOrdersByNextTokenRequests.add(listOrdersByNextTokenRequest);
                    /**
                     * 获得订单下一页的响应列表
                     */
                    List<Object> responseList2 =invokeListOrdersByNextToken(client, listOrdersByNextTokenRequests);
                    for (Object tempResponse : responseList2) {
                        // Object 转换 listOrdersByNextTokenResponseDto 还是 MarketplaceWebServiceOrdersException
                        String className = tempResponse.getClass().getName();
                        if ((ListOrdersByNextTokenResponse.class.getName()).equals(className) == true) {
                            System.out.println("responseList2 类型是 ListOrdersByNextTokenResponse。");
                            ListOrdersByNextTokenResponse response = (ListOrdersByNextTokenResponse) tempResponse;
                            //解析订单下一页的响应列表
                            listOrdersByNextTokenResponseDto = XMLUtil.analysisListOrdersByNextTokenResponse(response.toXML());
                        }
                    }
                    listOrdersResponseDtos.add(listOrdersByNextTokenResponseDto);
                }
                //循环输出
                for (int i=0;i<listOrdersResponseDtos.size();i++){
                    //循环输出订单
                    for(int j=0;j<listOrdersResponseDtos.get(i).getOrders().size();j++){
                        List<ListOrderItemsRequest> ListOrderItemsRequestRequests = new ArrayList<ListOrderItemsRequest>();
                        ListOrderItemsRequest ListOrderItemsRequest = new ListOrderItemsRequest();
                        String AmazonOrderId=listOrdersResponseDtos.get(i).getOrders().get(j).getAmazonOrderId();
                        ListOrderItemsRequest.setAmazonOrderId(AmazonOrderId);
                        ListOrderItemsRequest.setSellerId(sellerId);
                        ListOrderItemsRequest.setMWSAuthToken(mwsAuthToken);
                        ListOrderItemsRequestRequests.add(ListOrderItemsRequest);
                        List<Object> responseList3=  invokeListOrderItems(client,ListOrderItemsRequestRequests);
                        List<ListOrderItemsByNextTokenResponseDto> orderItemResponseDtos = new ArrayList<>();
                        ListOrderItemsByNextTokenResponseDto orderItemResponseDto=null;
                        for (Object tempResponse : responseList3) {
                            // Object 转换 listOrdersByNextTokenResponseDto 还是 MarketplaceWebServiceOrdersException
                            String className = tempResponse.getClass().getName();
                            if ((ListOrderItemsResponse.class.getName()).equals(className) == true) {
                                System.out.println("responseList3 类型是 ListOrderItemsByNextTokenResponse。");
                                ListOrderItemsResponse  response= (ListOrderItemsResponse) tempResponse;
                                orderItemResponseDto= XMLUtil.analysisListOrderItemsByNextTokenResponseFanWei(response.toXML());

                            }
                        }
                        orderItemResponseDtos.add(orderItemResponseDto);
                        for(int k=0;k<orderItemResponseDtos.size();k++){
                            List<OrderModel> orderModelList = new ArrayList<OrderModel>();
                            for(int m=0;m<orderItemResponseDtos.get(k).getOrderItems().size();m++){
                                String product_asin=orderItemResponseDtos.get(k).getOrderItems().get(m).getASIN();
                                String product_sku=orderItemResponseDtos.get(k).getOrderItems().get(m).getSellerSKU();
                                int ordernumber=orderItemResponseDtos.get(k).getOrderItems().get(m).getQuantityOrdered();
                                ProductShipAddressEntity addressEntity=new ProductShipAddressEntity();
                                String shipname=listOrdersResponseDtos.get(i).getOrders().get(j).getName();
                                String shipaddress=listOrdersResponseDtos.get(i).getOrders().get(j).getAddressLine1();
                                String shipaddress2=listOrdersResponseDtos.get(i).getOrders().get(j).getAddressLine2();
                                String shipcity=listOrdersResponseDtos.get(i).getOrders().get(j).getCity();
                                String shipCountry=listOrdersResponseDtos.get(i).getOrders().get(j).getCounty();
                                String shipdistrict=listOrdersResponseDtos.get(i).getOrders().get(j).getDistrict();
                                String shipregion=listOrdersResponseDtos.get(i).getOrders().get(j).getStateOrRegion();
                                String shiptel=listOrdersResponseDtos.get(i).getOrders().get(j).getPhone();
                                String shipzip=listOrdersResponseDtos.get(i).getOrders().get(j).getPostalCode();
                                //获得订单商品sku
                                //进行数据库表查询根据AmazonOrderId，有就更新，没有就插入
                                OrderModel orderModel=new OrderModel();
                                orderModel.setAmazonOrderId(AmazonOrderId);
                                orderModel.setUserId(shop.getUserId());
                                orderModel.setDeptId(shop.getDeptId());
                                String buytime=listOrdersResponseDtos.get(i).getOrders().get(j).getPurchaseDate();
                                buytime = buytime.replace("Z", " UTC");// UTC是本地时间
                                SimpleDateFormat format = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                                Date d=null;
                                try{
                                    d=format.parse(buytime);
                                }catch (ParseException e){
                                    e.getStackTrace();
                                }
                                Timestamp timeStamep = new Timestamp(d.getTime());
                                orderModel.setBuyDate(timeStamep);
                                orderModel.setOrderStatus(listOrdersResponseDtos.get(i).getOrders().get(j).getOrderStatus());
                                if(product_asin!=null){
                                    orderModel.setProductAsin(product_asin);
                                }else{
                                    orderModel.setProductAsin("");
                                }
                                if(product_sku!=null){
                                    orderModel.setProductSku(product_sku);
                                }else{
                                    orderModel.setProductSku("");
                                }

                                if(orderItemResponseDtos.get(k).getOrderItems().get(m).getItemPrice()!=null){
                                    String orderMoney = orderItemResponseDtos.get(k).getOrderItems().get(m).getItemPrice().getAmount();
                                    System.out.println("订单金额："+orderMoney+"============");
                                    String CurrencyCode=orderItemResponseDtos.get(k).getOrderItems().get(m).getItemPrice().getCurrencyCode();
                                    System.out.println("货币代码："+CurrencyCode+"============");
                                    if (orderMoney != null) {
                                        BigDecimal OrderMoney = new BigDecimal(orderMoney);
                                        orderModel.setOrderMoney(OrderMoney);//订单总费用
                                    } else {
                                        orderModel.setOrderMoney(new BigDecimal(0));//订单总费用
                                    }
                                    if(CurrencyCode!=null){
                                        orderModel.setCurrencyCode(CurrencyCode);
                                    }else{
                                        orderModel.setCurrencyCode("");
                                    }

                                }

                                orderModel.setOrderNumber(ordernumber);//订单总量
                                if(shopName!=null){
                                    orderModel.setShopName(shopName);
                                }else{
                                    orderModel.setShopName("");
                                }

                                if(shipname!=null){
                                    addressEntity.setShipName(shipname);
                                }else{
                                    addressEntity.setShipName("");
                                }
                                if(shipaddress!=null){
                                    addressEntity.setShipAddressLine1(shipaddress);
                                }else if(shipaddress2!=null){
                                    addressEntity.setShipAddressLine1(shipaddress2);
                                }else{
                                    addressEntity.setShipAddressLine1("");
                                }
                                if(shipcity!=null){
                                    addressEntity.setShipCity(shipcity);

                                }else{
                                    addressEntity.setShipCity("");

                                }
                                if(shipCountry!=null){
                                    addressEntity.setShipCounty(shipCountry);
                                }else{
                                    addressEntity.setShipCounty("");
                                }
                                if(shipdistrict!=null){
                                    addressEntity.setShipDistrict(shipdistrict);

                                }else{
                                    addressEntity.setShipDistrict("");

                                }
                                if(shipregion!=null){
                                    addressEntity.setShipRegion(shipregion);
                                }else{
                                    addressEntity.setShipRegion("");
                                }
                                if(shiptel!=null){
                                    addressEntity.setShipTel(shiptel);
                                }else{
                                    addressEntity.setShipTel("");
                                }
                                if(shipzip!=null){
                                    addressEntity.setShipZip(shipzip);
                                }else{
                                    addressEntity.setShipZip("");
                                }
                                orderModel.setProductShipAddressEntity(addressEntity);
                                orderModelList.add(orderModel);
                            }
                            //开启一个线程去对接业务逻辑并保存到数据库
                            if(orderModelList.size() > 0){
                                new SaveOrUpdateOrderThread(orderModelList).start();
                            }
                        }



                    }


                }

            }
        }
    }







    /**
     * 输出xml数据
     * @param client      客户端
     * @param requestList 请求参数
     * @return
     */

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
                System.out.println(e.getStackTrace()+"###############################");
                xresponse = e;
            }
            responseList.add(xresponse);
        }
        return responseList;
    }

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

    class SaveOrUpdateOrderThread extends Thread   {
        private List<OrderModel> list;
        public SaveOrUpdateOrderThread(List<OrderModel> list) {
            this.list = list;
        }

        @Override
        public void run() {
            orderService.updateOrder(list);
        }
    }
}
