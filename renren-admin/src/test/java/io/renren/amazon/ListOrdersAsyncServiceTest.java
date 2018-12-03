package io.renren.amazon;

import io.renren.common.utils.DateUtils;
import io.renren.modules.amazon.service.ListOrdersAsyncService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.orders._2013_09_01.*;
import com.amazonservices.mws.orders._2013_09_01.model.*;

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
    final String sellerId = "A3OT34MAE5R969";

    // MWS验证令牌
    final String mwsAuthToken = "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A";

    @Autowired
    private ListOrdersAsyncService listOrdersAsyncService;

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
        amazonOrderId.add("404-8753970-2929168");
        amazonOrderId.add("302-8563289-3648321");
        request.setAmazonOrderId(amazonOrderId);
        requestList.add(request);

        // Make the calls.
        listOrdersAsyncService.invokeGetOrder(client, requestList);
    }

    @Test
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
}
