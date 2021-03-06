package io.renren.amazon;


import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.service.ListOrdersAsyncService;
import io.renren.modules.logistics.DTO.ApplicationInfos;
import io.renren.modules.logistics.DTO.OrderRequestData;
import io.renren.modules.logistics.DTO.SenderInfo;
import io.renren.modules.logistics.DTO.ShippingInfo;
import io.renren.modules.logistics.entity.*;
import io.renren.modules.logistics.entity.Message;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.logistics.service.LogisticsChannelService;
import io.renren.modules.logistics.service.SubmitLogisticsService;
import io.renren.modules.logistics.util.NewAbroadLogisticsUtil;
import io.renren.modules.logistics.util.XmlUtils;
import io.renren.modules.order.entity.NewProductShipAddressEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.NewProductShipAddressService;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.product.entity.NewOrderEntity;
import io.renren.modules.product.entity.NewOrderItemEntity;
import io.renren.modules.product.service.NewOrderItemService;
import io.renren.modules.product.service.NewOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ListOrdersAsyncServiceTest {

    // 访问密钥ID
    final String accessKey = "AKIAJPTOJEGMM7G4FJQA";

    // 访问密钥
    final String secretKey = "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A";

    final String appName = "mws_test";

    final String appVersion = "1.0";

    final String serviceURL = "https://mws.amazonservices.co.uk/Orders/2013-09-01";

    // 卖家ID
    final String sellerId = "A18MNV1HNZ2F2W";

    // MWS验证令牌
    final String mwsAuthToken = "amzn.mws.8ab41e16-8486-6400-e655-b77def0356f2";

    @Autowired
    private ListOrdersAsyncService listOrdersAsyncService;
    @Autowired
    private LogisticsChannelService logisticsChannelService;
    @Autowired
    private SubmitLogisticsService submitLogisticsService;
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;
    @Autowired
    private ProductShipAddressService productShipAddressService;
    @Autowired
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private NewOrderService newOrderService;
    @Autowired
    private NewOrderItemService newOrderItemService;
    @Autowired
    private NewProductShipAddressService newProductShipAddressService;

    @Test
    @Ignore
    public void invokeListFeedsTest() {
        //定义一个map集合存储
        Map<String, Object> map = new HashMap<>();
        map.put("region", 0);//存储区域id--0代表欧洲区域
        List<String> serviceURL = new ArrayList<>();
        List<String> marketplaceIds = new ArrayList<>();
        List<AmazonGrantShopEntity> shopList = amazonGrantShopService.selectByMap(map);
        for(AmazonGrantShopEntity shop: shopList){
            serviceURL.add(shop.getMwsPoint());
            marketplaceIds.add(shop.getMarketplaceId());//获取MarketplaceId值
        }
        Shipping u1 = new Shipping();
        u1.setMessageType("OrderFulfillment");
        Header header=new Header();
        header.setDocumentVersion("1.01");
        header.setMerchantIdentifier("MYID");
        u1.setHeader(header);
        List<Message> messages=new ArrayList<>();
        Message message1=new Message();
        message1.setMessageID("1");
        OrderFulfillment orderful=new OrderFulfillment();
        orderful.setAmazonOrderID("306-7829162-9096359");
        orderful.setFulfillmentDate("2019-03-01T02:01:34");
        FulfillmentData fd=new FulfillmentData();
        fd.setCarrierName("Yun Express");
        fd.setShippingMethod("Standard");
        fd.setShipperTrackingNumber("YT3076279392579333");
        Item item=new Item();
        item.setAmazonOrderItemCode("34270572071275");
        item.setQuantity("1");
        orderful.setItem(item);
        orderful.setFulfillmentData(fd);
        message1.setOrderFulfillment(orderful);
        Message message2=new Message();
        message2.setMessageID("2");
        OrderFulfillment orderful2=new OrderFulfillment();
        orderful2.setAmazonOrderID("306-7829162-9096359");
        orderful2.setFulfillmentDate("2019-03-01T02:01:34");
        FulfillmentData fd2=new FulfillmentData();
        fd2.setCarrierName("Yun Express");
        fd2.setShippingMethod("Standard");
        fd2.setShipperTrackingNumber("YT3076279392579333");
        Item item2=new Item();
        item2.setAmazonOrderItemCode("38670572071278");
        item2.setQuantity("1");
        orderful2.setItem(item2);
        orderful2.setFulfillmentData(fd2);
        message2.setOrderFulfillment(orderful2);
        messages.add(message1);
        messages.add(message2);
        u1.setMessages(messages);
        List<Shipping> List = new ArrayList<Shipping>();
        List.add(u1);
        /**
         * 根据List数组，生成XML数据
         */
        String resultXml = XmlUtils.getXmlFromList(List);

        //打印生成xml数据
        FileWriter outdata = null;
        String filePath="/usr/shipping/shipping.xml";
        String feedType="_POST_ORDER_FULFILLMENT_DATA_";
        try {
            outdata = new FileWriter(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter outfile=new PrintWriter(outdata);
        outfile.println(resultXml);// 输出String
        outfile.flush();// 输出缓冲区的数据
        outfile.close();
//         List<Object> responseList =listOrdersAsyncService.invokeListOrders(client,requestList);
        //进行数据上传(步骤一)
        // TODO: 2018/12/26 数据上传
        String feedSubmissionId=submitLogisticsService.submitFeed(serviceURL.get(0),sellerId,mwsAuthToken,feedType,filePath,accessKey,secretKey);
        //进行数据上传(步骤二)
////        List<String> feedSubmissionIds=submitLogisticsService.getFeedSubmissionList(serviceURL.get(0),sellerId,mwsAuthToken,feedSubmissionId,accessKey,secretKey);
//        System.out.println("=========================="+feedSubmissionIds.get(0)+"=============================");
//        if(feedSubmissionIds.size()>0 && feedSubmissionIds!=null){
//            //进行数据上传(步骤三)
//            submitLogisticsService.getFeedSubmissionResult(serviceURL.get(0),sellerId,mwsAuthToken,feedSubmissionIds.get(0),accessKey,secretKey);
//        }
    }

    @Test
    @Ignore
    public void invokeListOrdersTest() {

        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL("https://mws-eu.amazonservices.com");

        MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                appName, appVersion, config, null);

        // Create a request list.
        List<ListOrdersRequest> requestList = new ArrayList<ListOrdersRequest>();
        ListOrdersRequest request = new ListOrdersRequest();

        request.setSellerId(sellerId);

        request.setMWSAuthToken(mwsAuthToken);
        //
        XMLGregorianCalendar lastUpdatedAfter = DateUtils.xmlToDate("2017-10-1 22:34:56");
        request.setLastUpdatedAfter(lastUpdatedAfter);
        XMLGregorianCalendar lastUpdatedBefore = DateUtils.xmlToDate("2018-11-16 22:34:56");
        request.setLastUpdatedBefore(lastUpdatedBefore);
        // 店铺ID
        List<String> marketplaceId = new ArrayList<String>();
        marketplaceId.add("A1PA6795UKMFR9");
        marketplaceId.add("A1RKKUPIHCS9HS");
        marketplaceId.add("A13V1IB3VIYZZH");
        marketplaceId.add("A1F83G8C2ARO7P");
        marketplaceId.add("A21TJRUUN4KGV");
        marketplaceId.add("APJ6JRA9NG5V4");
        marketplaceId.add("A33AVAJ2PDY3EV");
        request.setMarketplaceId(marketplaceId);
        requestList.add(request);

        listOrdersAsyncService.invokeListOrders(client, requestList);

    }

    @Test
    @Ignore
    public void listOrderByNextTokenAsyncTest() {
        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL(serviceURL);

        MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                appName, appVersion, config, null);

        // Create a request list.
        List<ListOrdersByNextTokenRequest> requestList = new ArrayList<ListOrdersByNextTokenRequest>();
        ListOrdersByNextTokenRequest request = new ListOrdersByNextTokenRequest();
        // String sellerId = "A3OT34MAE5R969";
        request.setSellerId(sellerId);
        // String mwsAuthToken = "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A";
        request.setMWSAuthToken(mwsAuthToken);
        String nextToken = "fSKnKj1qo99/YYuZQbKv1QafEPREmauvizt1MIhPYZYOpmdYy2BlWdVncdvmRFv9Bm4WEItVacpTg2dsYWmzKd8ytOVgN7d/KyNtf5fepe3iG+vCyuJg6hanQHYOdTc+S+0zfa1p/rlwwUVaIJ5xpkFIaEVlNiMgr390vvVWxn1fnxQ7X6uzaF7TMh5o5U17y+9VsJ0xnRjNwKEvbXwVRAEJIskodtiAKbo82QdwNfQn+AwiN87vnp+lUkZL6+7mkUcxfLpcvmXCcTjnCe7d9QC0s33bXHPMJUiAnEg/VQSLdbAGTjFVaOrSyOslVIrxsSNO+ZJB0flNr+lvPPgkmY/BBCgkNvGZJVHL0xZ68mFOlEANlTTF+AiWZjtq5O8Vu3UzQHFKvEKFmWkEh3p1SY57CVI/H9ImehdapzRS0eeSdpZRB7wxkv++oSr7md1azurCEvRg0oZVIqDORMMh+vgSGNlBymhlKyHkmpcASUKY9TB3jldv+X/y5ZICxITwxDxzcxlN3cZKBK7iEwGf1glpi+km+v61ZQ+8nmCw/wtMIENUTqf/iM/UC0E9WR84T6B1SgezW1etTshJEF7OfCHpqJO4CEcxYUuw4IE3xTA=";
        request.setNextToken(nextToken);
        requestList.add(request);

        // Make the calls.
        listOrdersAsyncService.invokeListOrdersByNextToken(client, requestList);
    }

    @Test
    @Ignore
    public void getOrderAsyncTest() {

        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL(serviceURL);

        MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                appName, appVersion, config, null);

        // Create a request list.
        List<GetOrderRequest> requestList = new ArrayList<GetOrderRequest>();
        GetOrderRequest request = new GetOrderRequest();
        // String sellerId = "A3OT34MAE5R969";
        request.setSellerId(sellerId);
        // String mwsAuthToken = "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A";
        request.setMWSAuthToken(mwsAuthToken);
        List<String> amazonOrderId = new ArrayList<String>();
        // 最多50个
        amazonOrderId.add("306-7829162-9096359");
        request.setAmazonOrderId(amazonOrderId);
        requestList.add(request);

        // Make the calls.
        List<Object> reponselist=listOrdersAsyncService.invokeGetOrder(client, requestList);
        Boolean isSuccess = false;
        GetOrderResponse getOrderResponse = null;
        for (Object tempResponse : reponselist) {
            // Object 转换 ListOrdersResponse 还是 MarketplaceWebServiceOrdersException
            String className = tempResponse.getClass().getName();
            if ((GetOrderResponse.class.getName()).equals(className) == true) {
                System.out.println("responseList 类型是 GetOrderResponse。");
                GetOrderResponse response = (GetOrderResponse) tempResponse;
                System.out.println(response.toXML());
                String s=response.toXML();
                if(s.contains("<OrderStatus>")){
                   s= s.substring(s.indexOf("<OrderStatus>"),s.indexOf("</OrderStatus>")).replace("<OrderStatus>","");
                    System.out.println(s);
                }
//                getOrderResponse = analysisGetOrderResponse(response.toXML());
                isSuccess = true;
            } else {
                System.out.println("responseList 类型是 MarketplaceWebServiceOrdersException。");
                isSuccess = false;
                continue;
            }
        }
    }

    @Test
    @Ignore
    public void listOrderItemsAsyncTest() {
        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL(serviceURL);

        MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                appName, appVersion, config, null);

        // Create a request list.
        List<ListOrderItemsRequest> requestList = new ArrayList<ListOrderItemsRequest>();
        ListOrderItemsRequest request = new ListOrderItemsRequest();
        // String sellerId = "example";
        request.setSellerId(sellerId);
        // String mwsAuthToken = "example";
        request.setMWSAuthToken(mwsAuthToken);
        // 单个订单id，AmazonOrderId
        String amazonOrderId = "028-9193666-8454735";
        request.setAmazonOrderId(amazonOrderId);
        requestList.add(request);

        // Make the calls.
        listOrdersAsyncService.invokeListOrderItems(client, requestList);
    }

    @Test
    @Ignore
    public void listOrderItemsByNextTokenAsyncTest() {

        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL(serviceURL);

        MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                appName, appVersion, config, null);

        // Create a request list.
        List<ListOrderItemsByNextTokenRequest> requestList = new ArrayList<ListOrderItemsByNextTokenRequest>();
        ListOrderItemsByNextTokenRequest request = new ListOrderItemsByNextTokenRequest();
        // String sellerId = "A3OT34MAE5R969";
        request.setSellerId(sellerId);
        // String mwsAuthToken = "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A";
        request.setMWSAuthToken(mwsAuthToken);
        String nextToken = "";
        request.setNextToken(nextToken);
        requestList.add(request);

        // Make the calls.
        listOrdersAsyncService.invokeListOrderItemsByNextToken(client, requestList);

    }

    @Test
    @Ignore
    public void getServiceStatusAsyncTest() {
        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL(serviceURL);

        MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                appName, appVersion, config, null);

        // Create a request list.
        List<GetServiceStatusRequest> requestList = new ArrayList<GetServiceStatusRequest>();
        GetServiceStatusRequest request = new GetServiceStatusRequest();
        // String sellerId = "example";
        request.setSellerId(sellerId);
        // String mwsAuthToken = "example";
        request.setMWSAuthToken(mwsAuthToken);
        requestList.add(request);

        // Make the calls.
        listOrdersAsyncService.invokeGetServiceStatus(client, requestList);

    }
    @Test
    public void push(){
        NewAbroadLogisticsUtil.pushOrder();
    }

    @Test
    @Ignore
    public  void pushOrder() {
        String amazonOrderId="303-7832993-5749169";
        NewOrderEntity neworderEntity =newOrderService.selectOne(new EntityWrapper<NewOrderEntity>().eq("amazon_order_id",amazonOrderId));
        NewProductShipAddressEntity newProductShipAddressEntity=newProductShipAddressService.selectOne(new EntityWrapper<NewProductShipAddressEntity>().eq("amazon_order_id",amazonOrderId));

        ProductShipAddressEntity shipAddressEntity = productShipAddressService.selectOne(new EntityWrapper<ProductShipAddressEntity>().eq("order_id",newProductShipAddressEntity.getOrderId()));

        //推送--订单基本信息
        OrderRequestData omsOrder = new OrderRequestData();
        omsOrder.setOrderNumber(amazonOrderId);
        List<DomesticLogisticsEntity> domesticLogisticsEntitys = domesticLogisticsService.selectList(new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",neworderEntity.getOrderId()));
        StringBuffer deanname = new StringBuffer("");
        StringBuffer supplyexpressno = new StringBuffer("");
        for(int i = 0; i < domesticLogisticsEntitys.size(); i++){
            if(StringUtils.isNotBlank(domesticLogisticsEntitys.get(i).getLogisticsCompany())){
                deanname.append(domesticLogisticsEntitys.get(i).getLogisticsCompany());
                deanname.append(",");
                supplyexpressno.append(domesticLogisticsEntitys.get(i).getWaybill());
                supplyexpressno.append(",");
            }
        }
        if(StringUtils.isNotBlank(deanname.toString())){
            omsOrder.setShippingMethodCode(deanname.substring(0,deanname.length()-1));//测试用的
        }else{
            omsOrder.setShippingMethodCode("GBZXR");
        }
        omsOrder.setPackageNumber(neworderEntity.getOrderNumber());
        BigDecimal weight= new BigDecimal(1);
        omsOrder.setWeight(weight);
        omsOrder.setTrackingNumber(supplyexpressno.toString());
//        //设置时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS+08:00");
//        omsOrder.setOrder_date(sdf.format(orderEntity.getBuyDate()));
//        omsOrder.setOrder_memo(shipAddressEntity.getShipCountry());
        //推送--订单详情

        List<ApplicationInfos> omsOrderDetails = new ArrayList<>();
        List<NewOrderItemEntity> productOrderItemEntitys=newOrderItemService.selectList(new EntityWrapper<NewOrderItemEntity>().eq("amazon_order_id",neworderEntity.getAmazonOrderId()));
        for(NewOrderItemEntity productOrderItemEntity:productOrderItemEntitys){
            ApplicationInfos omsOrderDetail=new ApplicationInfos();
            omsOrderDetail.setApplicationName(productOrderItemEntity.getProductTitle());
            omsOrderDetail.setQty(productOrderItemEntity.getOrderItemNumber());
            omsOrderDetail.setUnitPrice(productOrderItemEntity.getProductPrice());
            BigDecimal unitweight=new BigDecimal(1);//测试用
            omsOrderDetail.setUnitWeight(unitweight);
            omsOrderDetail.setProductUrl(productOrderItemEntity.getProductImageUrl());
            omsOrderDetail.setSku(productOrderItemEntity.getProductSku());
            omsOrderDetails.add(omsOrderDetail);

        }

           //推送—收货人信息
            ShippingInfo shippingInfo=new ShippingInfo();
            shippingInfo.setCountryCode(shipAddressEntity.getShipCountry());
            shippingInfo.setShippingFirstName(shipAddressEntity.getShipName());
            shippingInfo.setShippingAddress(shipAddressEntity.getShipAddressDetail());
            shippingInfo.setShippingCity(shipAddressEntity.getShipCity());
            shippingInfo.setShippingState(shipAddressEntity.getShipRegion());
            shippingInfo.setShippingZip(shipAddressEntity.getShipZip());
            shippingInfo.setShippingPhone(shipAddressEntity.getShipTel());
           ApplicationInfos[] applicationInfos=new ApplicationInfos[omsOrderDetails.size()];
           omsOrder.setShippingInfo(shippingInfo);
           omsOrder.setApplicationInfos(omsOrderDetails.toArray(applicationInfos));
           SenderInfo senderInfo=new SenderInfo();//默认空值，不是必填参数
           omsOrder.setSenderInfo(senderInfo);
           JSONArray omsOrderJson = JSONArray.fromObject(omsOrder);
//            NewAbroadLogisticsUtil.pushOrder(omsOrderJson.toString());
    }

    public  List<String>  getShippingMethodCode(int type) {
        List<String> result =new  ArrayList<>();
        List<LogisticsChannelEntity> channelilist = logisticsChannelService.selectList(new EntityWrapper<LogisticsChannelEntity>().eq("package_type", type));
         for (LogisticsChannelEntity logisticsChannelEntity:channelilist){
             String key=logisticsChannelEntity.getChannelName();
             String value=logisticsChannelEntity.getChannelCode();
             String str=key+":"+value;
             result.add(str);
         }
        return result;
    }
    @Test
    public void test(){
        List<String> list= getShippingMethodCode(0);
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }


    }




}
