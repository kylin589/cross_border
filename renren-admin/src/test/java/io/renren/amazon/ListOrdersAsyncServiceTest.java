package io.renren.amazon;

import com.amazonservices.mws.orders._2013_09_01.samples.MarketplaceWebServiceOrdersSampleConfig;
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
    public void invokeListOrdersTest() {

        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL(serviceURL);

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
    public void ListOrderItemsByNextTokenAsyncTest(){
        // Get a client connection.
        MarketplaceWebServiceOrdersAsyncClient client = MarketplaceWebServiceOrdersSampleConfig.getAsyncClient();

        // Create a request list.
        List<ListOrdersByNextTokenRequest> requestList = new ArrayList<ListOrdersByNextTokenRequest>();
        ListOrdersByNextTokenRequest request = new ListOrdersByNextTokenRequest();
        String sellerId = "A3OT34MAE5R969";
        request.setSellerId(sellerId);
        String mwsAuthToken = "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A";
        request.setMWSAuthToken(mwsAuthToken);


        String nextToken = "example";
        request.setNextToken(nextToken);
        requestList.add(request);

        // Make the calls.
        listOrdersAsyncService.invokeListOrdersByNextToken(client, requestList);

    }
}
