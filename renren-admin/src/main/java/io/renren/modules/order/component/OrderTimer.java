package io.renren.modules.order.component;


import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import com.amazonservices.mws.products.MarketplaceWebServiceProducts;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.ASINListType;
import com.amazonservices.mws.products.model.GetMatchingProductRequest;
import com.amazonservices.mws.products.model.GetMatchingProductResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.modules.amazon.dto.ListOrderItemsByNextTokenResponseDto;
import io.renren.modules.amazon.dto.ListOrdersResponseDto;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.util.XMLUtil;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.VariantsInfoEntity;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.service.VariantsInfoService;
import io.renren.modules.product.vm.OrderModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static io.renren.modules.amazon.util.XMLUtil.analysisListOrderItemsByNextTokenResponse;
import static io.renren.modules.amazon.util.XMLUtil.analysisListOrdersByNextTokenResponse;
import static io.renren.modules.amazon.util.XMLUtil.analysisListOrdersResponse;


@Component("OrderTimer")
public class OrderTimer {
    // 欧洲
    @Value(("${mws-config.eu-access-key}"))
    private String euAccessKey;

    @Value(("${mws-config.eu-secret-key}"))
    private String euSecretKey;

    // 日本
    @Value(("${mws-config.jp-access-key}"))
    private String jpAccessKey;

    @Value(("${mws-config.jp-secret-key}"))
    private String jpSecretKey;

    // 北美
    @Value(("${mws-config.na-access-key}"))
    private String naAccessKey;

    @Value(("${mws-config.na-secret-key}"))
    private String naSecretKey;

    // 澳大利亚
    @Value(("${mws-config.au-access-key}"))
    private String auAccessKey;

    @Value(("${mws-config.au-secret-key}"))
    private String auSecretKey;

    @Value(("${mws-config.app-name}"))
    private String appName;

    @Value(("${mws-config.app-version}"))
    private String appVersion;

    @Autowired
    private AmazonGrantShopService amazonGrantShopService;

    @Autowired
    private AmazonGrantService amazonGrantService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private VariantsInfoService variantsInfoService;

    @Autowired
    private ProductsService productsService;

    /**
     * 获得店铺授权列表信息
     */
    public void getShopGrantlist(){
        List<AmazonGrantEntity> grantList = amazonGrantService.selectList(null);
        Map map=new HashMap();
        List<AmazonGrantShopEntity> shoplist=null;
        for (AmazonGrantEntity grant : grantList) {
            Long grantId = grant.getGrantId();
            String sellerId = grant.getMerchantId();//获得商家id
            String mwsAuthToken = grant.getGrantToken();//获得授权Token

            shoplist=amazonGrantShopService.selectList(new EntityWrapper<AmazonGrantShopEntity>().eq("region",grant.getRegion()).eq("grant_id",grant.getGrantId()));
            for(AmazonGrantShopEntity shop:shoplist){
                String shopName = shop.getShopName();
                map.put("region",shop.getRegion());
                map.put("sellerId",sellerId);
                map.put("mwsAuthToken",mwsAuthToken);
                map.put("shopname",shop.getShopName());
                map.put("countryCode",shop.getCountryCode());
                map.put("serviceURL",shop.getMwsPoint());
                map.put("marketplaceId",shop.getMarketplaceId());
                map.put("shopId",shop.getGrantShopId());
                map.put("userId",shop.getUserId());
                map.put("deptId",shop.getDeptId());
                getSingleShopOrderList(map);//获得单个店铺列表
            }
        }
    }

    /***
     * 获得单个店铺订单和订单详情
     */
    @Async("taskExecutor")
    public void getSingleShopOrderList(Map map) {
        System.out.println("111111111111111111111111");
        List<String> marketplaceId = new ArrayList<>();
        String sellerId = (String) map.get("sellerId");
        String mwsAuthToken = (String) map.get("mwsAuthToken");
        String shopName = (String) map.get("shopname");
        Long shopId = (Long)map.get("shopId");
        Long userId = (Long)map.get("userId");
        Long deptId = (Long)map.get("deptId");
        int region= (int) map.get("region");
        String accessKey=null;
        String secretKey=null;
        if(region==0){//北美
            accessKey=naAccessKey;
            secretKey=naSecretKey;
        }else if(region==1){//欧洲
            accessKey=euAccessKey;
            secretKey=euSecretKey;
        }else if(region==2){//日本
            accessKey=jpAccessKey;
            secretKey=jpSecretKey;
        }else if(region==3){//澳大利亚
            accessKey=auAccessKey;
            secretKey=auSecretKey;
        }
        String serviceURL = (String) map.get("serviceURL");
        marketplaceId.add((String) map.get("marketplaceId"));
        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL(serviceURL);
        MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                "my_test", "1.0", config, null);
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
            if ((ListOrdersResponse.class.getName()).equals(className) == true) {
                System.out.println("responseList 类型是 ListOrdersResponse。");
                ListOrdersResponse response = (ListOrdersResponse) tempResponse;
                listOrdersResponseDto = analysisListOrdersResponse(response.toXML());
                isSuccess = true;
            } else {
                System.out.println("responseList 类型是 MarketplaceWebServiceOrdersException。");
                isSuccess = false;
                continue ;
            }
        }
        listOrdersResponseDtos.add(listOrdersResponseDto);//封装解析出来的
        if (listOrdersResponseDto != null && StringUtils.isNotBlank(listOrdersResponseDto.getNextToken()) && isSuccess == true) {
            ListOrdersResponseDto listOrdersByNextTokenResponseDto = null;
            List<ListOrdersByNextTokenRequest> listOrdersByNextTokenRequests = new ArrayList<ListOrdersByNextTokenRequest>();
            ListOrdersByNextTokenRequest listOrdersByNextTokenRequest = new ListOrdersByNextTokenRequest();
            listOrdersByNextTokenRequest.setSellerId(sellerId);
            listOrdersByNextTokenRequest.setMWSAuthToken(mwsAuthToken);
            listOrdersByNextTokenRequest.setNextToken(listOrdersResponseDto.getNextToken());
            listOrdersByNextTokenRequests.add(listOrdersByNextTokenRequest);
            List<Object> responseList2 = invokeListOrdersByNextToken(client, listOrdersByNextTokenRequests);
            Boolean isSuccess2 = false;
            for (Object tempResponse : responseList2) {
                // Object 转换 listOrdersByNextTokenResponseDto 还是 MarketplaceWebServiceOrdersException
                String className = tempResponse.getClass().getName();
                if ((ListOrdersByNextTokenResponse.class.getName()).equals(className) == true) {
                    System.out.println("responseList2 类型是 ListOrdersByNextTokenResponse。");
                    ListOrdersByNextTokenResponse response = (ListOrdersByNextTokenResponse) tempResponse;
                    //解析订单下一页的响应列表
                    listOrdersByNextTokenResponseDto = analysisListOrdersByNextTokenResponse(response.toXML());
                    isSuccess2 = true;
                } else {
                    System.out.println("responseList2 类型是 MarketplaceWebServiceOrdersException。");
                    isSuccess2 = false;
                    continue;
                }
            }
            listOrdersResponseDtos.add(listOrdersByNextTokenResponseDto);
        }
        if(listOrdersResponseDtos.size()>0 && listOrdersResponseDtos!=null){
            //循环输出
            for (int i = 0; i < listOrdersResponseDtos.size(); i++) {
                //循环输出订单
                if (listOrdersResponseDtos.get(i)!= null && listOrdersResponseDtos.get(i).getOrders() != null){
                    for (int j = 0; j < listOrdersResponseDtos.get(i).getOrders().size(); j++) {
                        List<ListOrderItemsRequest> ListOrderItemsRequestRequests = new ArrayList<ListOrderItemsRequest>();
                        ListOrderItemsRequest ListOrderItemsRequest = new ListOrderItemsRequest();
                        String AmazonOrderId = listOrdersResponseDtos.get(i).getOrders().get(j).getAmazonOrderId();
                        System.out.println("订单号:" + AmazonOrderId + "=================");
                        ListOrderItemsRequest.setAmazonOrderId(AmazonOrderId);
                        ListOrderItemsRequest.setSellerId(sellerId);
                        ListOrderItemsRequest.setMWSAuthToken(mwsAuthToken);
                        ListOrderItemsRequestRequests.add(ListOrderItemsRequest);
                        List<Object> responseList3 = invokeListOrderItems(client, ListOrderItemsRequestRequests);
                        Boolean isSuccess3 = false;
                        List<ListOrderItemsByNextTokenResponseDto> orderItemResponseDtos = new ArrayList<>();
                        ListOrderItemsByNextTokenResponseDto orderItemResponseDto = null;
                        for (Object tempResponse : responseList3) {
                            // Object 转换 listOrdersByNextTokenResponseDto 还是 MarketplaceWebServiceOrdersException
                            String className = tempResponse.getClass().getName();
                            if ((ListOrderItemsResponse.class.getName()).equals(className) == true) {
                                System.out.println("responseList3 类型是 ListOrderItemsByNextTokenResponse。");
                                ListOrderItemsResponse response = (ListOrderItemsResponse) tempResponse;
                                orderItemResponseDto = analysisListOrderItemsByNextTokenResponse(response.toXML());
                                isSuccess3=true;
                            } else {
                                System.out.println("responseList3 类型是 MarketplaceWebServiceOrdersException。");
                                isSuccess3=false;
                                continue;
                            }
                        }
                        orderItemResponseDtos.add(orderItemResponseDto);
                        for (int k = 0; k < orderItemResponseDtos.size(); k++) {
                            List<OrderModel> orderModelList = new ArrayList<OrderModel>();
                            if(orderItemResponseDtos.get(k) != null && orderItemResponseDtos.get(k).getOrderItems() != null && orderItemResponseDtos.get(k).getOrderItems().size() >0){
                                for (int m = 0; m < orderItemResponseDtos.get(k).getOrderItems().size(); m++) {
                                    System.out.println("店铺id===================" + shopId);
                                    String titlename = orderItemResponseDtos.get(k).getOrderItems().get(m).getTitle();
                                    System.out.println("商品名称:"+titlename+"==================");
                                    String product_asin = orderItemResponseDtos.get(k).getOrderItems().get(m).getASIN();
                                    System.out.println("商品asin码:"+product_asin+"==================");
                                    String orderItemId = orderItemResponseDtos.get(k).getOrderItems().get(m).getOrderItemId();
                                    String product_sku = orderItemResponseDtos.get(k).getOrderItems().get(m).getSellerSKU();
                                    System.out.println("商品sku:"+product_sku+"==================");
                                    int ordernumber = orderItemResponseDtos.get(k).getOrderItems().get(m).getQuantityOrdered();
                                    //根据商品sku获取图片连接的url的方法
                                    String img_url= this.getImageUrl(product_sku,product_asin,sellerId,mwsAuthToken,serviceURL,marketplaceId);
                                    System.out.println("订单配送数量："+ordernumber+"============");
                                    ProductShipAddressEntity addressEntity = new ProductShipAddressEntity();
                                    String shipname = listOrdersResponseDtos.get(i).getOrders().get(j).getName();
                                    System.out.println("收件人姓名:"+shipname+"==========");
                                    String shipaddress1 = listOrdersResponseDtos.get(i).getOrders().get(j).getAddressLine1();
                                    String shipaddress2 = listOrdersResponseDtos.get(i).getOrders().get(j).getAddressLine2();

                                    System.out.println("收件地址1："+shipaddress1+"===============");
                                    System.out.println("收件地址2："+shipaddress2+"===============");

                                    String shipcity = listOrdersResponseDtos.get(i).getOrders().get(j).getCity();
                                    System.out.println("收件人城市:"+shipcity+"====================");
                                    String shipCounty = listOrdersResponseDtos.get(i).getOrders().get(j).getCounty();
                                    System.out.println("收件人县:"+shipCounty+"====================");
                                    String shipCountry = listOrdersResponseDtos.get(i).getOrders().get(j).getCountryCode();
                                    System.out.println("收件人国家:"+shipCountry+"====================");
                                    String shipdistrict = listOrdersResponseDtos.get(i).getOrders().get(j).getDistrict();
                                    System.out.println("收件人区:"+shipdistrict+"====================");
                                    String shipregion = listOrdersResponseDtos.get(i).getOrders().get(j).getStateOrRegion();
                                    System.out.println("收件人区域:"+shipregion+"====================");
                                    String shiptel = listOrdersResponseDtos.get(i).getOrders().get(j).getPhone();
                                    System.out.println("收件人电话:"+shiptel+"====================");
                                    String shipzip = listOrdersResponseDtos.get(i).getOrders().get(j).getPostalCode();
                                    System.out.println("收件人邮编:"+shipzip+"====================");
                                    //获得订单商品sku
                                    //进行数据库表查询根据AmazonOrderId，有就更新，没有就插入
                                    OrderModel orderModel = new OrderModel();

                                    orderModel.setAmazonOrderId(AmazonOrderId);
                                    String buytime=listOrdersResponseDtos.get(i).getOrders().get(j).getPurchaseDate();
                                    orderModel.setCountry((String) map.get("countryCode"));
                                    buytime = buytime.replace("Z", " UTC");// UTC是本地时间
                                    SimpleDateFormat format = new SimpleDateFormat(
                                            "yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                                    Date d=null;
                                    try{
                                        d=format.parse(buytime);
                                    }catch (ParseException e){
                                        e.getStackTrace();
                                    }
                                    if(d != null){
                                        Timestamp timeStamep = new Timestamp(d.getTime());
                                        System.out.println("购买日期:"+timeStamep+"=================================");
                                        orderModel.setBuyDate(timeStamep);
                                    }
                                    orderModel.setOrderStatus(listOrdersResponseDtos.get(i).getOrders().get(j).getOrderStatus());
                                    if (titlename != null) {
                                        orderModel.setTitlename(titlename);
                                    } else {
                                        orderModel.setTitlename("");
                                    }
                                    if (product_asin != null) {
                                        orderModel.setProductAsin(product_asin);
                                    } else {
                                        orderModel.setProductAsin("");
                                    }
                                    if (orderItemId != null) {
                                        orderModel.setOrderItemId(orderItemId);
                                    } else {
                                        orderModel.setOrderItemId("");
                                    }
                                    if (product_sku != null) {
                                        orderModel.setProductSku(product_sku);
                                    } else {
                                        orderModel.setProductSku("");
                                    }
                                    if(img_url!=null){
                                        orderModel.setProductImageUrl(img_url);
                                    }else{
                                        orderModel.setProductImageUrl("");
                                    }
                                    if(orderItemResponseDtos.get(k).getOrderItems().get(m).getItemPrice()!=null){
                                        String orderMoney = listOrdersResponseDtos.get(i).getOrders().get(j).getAmount();
                                        System.out.println("订单金额："+orderMoney+"============");
                                        if (orderMoney != null) {
                                            BigDecimal OrderMoney = new BigDecimal(orderMoney);
                                            orderModel.setOrderMoney(OrderMoney);//订单总费用
                                        } else {
                                            orderModel.setOrderMoney(new BigDecimal(0));//订单总费用
                                        }
                                        String CurrencyCode=orderItemResponseDtos.get(k).getOrderItems().get(m).getItemPrice().getCurrencyCode();
                                        System.out.println("货币代码："+CurrencyCode+"============");
                                        if(CurrencyCode!=null){
                                            orderModel.setCurrencyCode(CurrencyCode);
                                        }else{
                                            orderModel.setCurrencyCode("");
                                        }
                                    }
                                    orderModel.setOrderNumber(ordernumber);//订单总量
                                    if (shopName != null) {
                                        orderModel.setShopName(shopName);
                                    } else {
                                        orderModel.setShopName("");
                                    }

                                    orderModel.setShopId(shopId);
                                    orderModel.setUserId(userId);
                                    orderModel.setDeptId(deptId);
                                    if (shipname != null) {
                                        addressEntity.setShipName(shipname);
                                    } else {
                                        addressEntity.setShipName("");
                                    }
                                    if (shipaddress1 != null) {
                                        addressEntity.setShipAddressLine1(shipaddress1);
                                    } else if(shipaddress2 != null){
                                        addressEntity.setShipAddressLine1(shipaddress2);
                                    }else{
                                        addressEntity.setShipAddressLine1("");
                                    }
                                    if (shipcity != null) {
                                        addressEntity.setShipCity(shipcity);

                                    } else {
                                        addressEntity.setShipCity("");

                                    }
                                    /**
                                     * 国家
                                     */
                                    if (shipCountry != null) {
                                        addressEntity.setShipCountry(shipCountry);
                                    } else {
                                        addressEntity.setShipCountry("");
                                    }
                                    /**
                                     * 县
                                     */
                                    if(shipCounty !=null){
                                        addressEntity.setShipCounty(shipCounty);
                                    }else{
                                        addressEntity.setShipCounty("");
                                    }
                                    if (shipdistrict != null) {
                                        addressEntity.setShipDistrict(shipdistrict);

                                    } else {
                                        addressEntity.setShipDistrict("");

                                    }
                                    if (shipregion != null) {
                                        addressEntity.setShipRegion(shipregion);
                                    } else {
                                        addressEntity.setShipRegion("");
                                    }
                                    if (shiptel != null) {
                                        addressEntity.setShipTel(shiptel);
                                    } else {
                                        addressEntity.setShipTel("");
                                    }
                                    if (shipzip != null) {
                                        addressEntity.setShipZip(shipzip);
                                    } else {
                                        addressEntity.setShipZip("");
                                    }
                                    System.out.println("======================"+orderModel+"=====================");
                                    orderModel.setProductShipAddressEntity(addressEntity);
                                    orderModelList.add(orderModel);
                                    System.out.println("============="+orderModelList.size()+"========================");
                                }
                                if(orderModelList.size() > 0){
//                                    orderService.updateOrder(orderModelList);
                                    System.out.println("执行更新");
                                    new SaveOrUpdateOrderThread(orderModelList).start();
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * 根据商品sku来获取商品的image_url值
     * @param product_sku
     */
    public String getImageUrl(String product_sku,String product_asin,String sellerld,String token,String sericeUrl,List marketplaceId){
        String img_url =null;
        //根据sku去新库的变体表中获取变体信息
        VariantsInfoEntity skuInfo=variantsInfoService.selectOne(new EntityWrapper<VariantsInfoEntity>().eq("variant_sku",product_sku) );
        if(skuInfo!=null) {
          img_url = skuInfo.getImageUrl().split(",")[0];//获取图片url
        }
        //如果获取不到，则到产品实体类中查询获取
        if(!StringUtils.isNotBlank(img_url)){
            ProductsEntity productsEntity= productsService.selectOne(new EntityWrapper<ProductsEntity>().eq("main_image_url",product_sku));
            if(productsEntity!=null){
                img_url=productsEntity.getMainImageUrl();
            }
            if(!StringUtils.isNotBlank(img_url)){
                //如果新库获取不到，就到旧库里获取，调用商品获取的接口
                return getProductinfoTest(sellerld,token,product_asin,marketplaceId);
            }
        }
        return null;
    }


    public String getProductinfoTest(String sellerld,String token,String product_asin,List marketplacedId) {
        MarketplaceWebServiceProductsConfig config = new MarketplaceWebServiceProductsConfig();
        config.setServiceURL("https://mws-eu.amazonservices.com");
        MarketplaceWebServiceProductsAsyncClient client = new MarketplaceWebServiceProductsAsyncClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                "mws_test", "1.0", config, null);
        // Create a request.
        GetMatchingProductRequest request = new GetMatchingProductRequest();

        request.setSellerId(sellerld);
        request.setMWSAuthToken(token);
        request.setMarketplaceId(marketplacedId.get(0).toString());
        ASINListType asinList = new ASINListType();
        List<String> asins = new ArrayList<>();
        asins.add(product_asin);
        asinList.setASIN(asins);
        request.setASINList(asinList);
        return invokeGetMatchingProduct(client, request);

    }

    public static String invokeGetMatchingProduct(MarketplaceWebServiceProducts client, GetMatchingProductRequest request) {
        com.amazonservices.mws.products.model.ResponseHeaderMetadata rhmd;
        try {
            GetMatchingProductResponse response = client.getMatchingProduct(request);
            rhmd = response.getResponseHeaderMetadata();
            System.out.println("Response:");
            System.out.println("RequestId: " + rhmd.getRequestId());
            System.out.println("Timestamp: " + rhmd.getTimestamp());

            String responseXml = response.toXML();
            System.out.println("商品xml：：：：：：：：：：：：：：：：：：：：：：/n" + responseXml );
            int a = responseXml.indexOf("<ns2:URL>");
            int b = responseXml.indexOf("</ns2:URL>");
            //进行截取图片url
            if(a != b){
                String imageURL = responseXml.substring(a + 9, b).replace("SL75", "SL500");
                return imageURL;
            }
        } catch (MarketplaceWebServiceProductsException var5) {
            System.out.println("Service Exception:");
            rhmd = var5.getResponseHeaderMetadata();
            if (rhmd != null) {
                System.out.println("RequestId: " + rhmd.getRequestId());
                System.out.println("Timestamp: " + rhmd.getTimestamp());
            }

            System.out.println("Message: " + var5.getMessage());
            System.out.println("StatusCode: " + var5.getStatusCode());
            System.out.println("ErrorCode: " + var5.getErrorCode());
            System.out.println("ErrorType: " + var5.getErrorType());
        }
        return null;
    }

    /**
     * 功能描述：发送订单请求，返回订单列表的响应数据
     *
     *
     */

    /*public void listOrdersRequest() {

        Map<String, Object> map = new HashMap<>();
        map.put("region", 0);
        List<AmazonGrantEntity> grantList=amazonGrantService.selectByMap(map);
          for(AmazonGrantEntity grant:grantList){


            String sellerId=grant.getMerchantId();//获得商家id
            String mwsAuthToken=grant.getGrantToken();//获得授权Token
            List<AmazonGrantShopEntity> shopList=amazonGrantShopService.selectByMap(map);
            // 店铺ID
            List<String> marketplaceId = new ArrayList<>();
            //循环获取店铺授权
              aaa: for(AmazonGrantShopEntity shop: shopList){
                String serviceURL = shop.getMwsPoint();
                marketplaceId.add(shop.getMarketplaceId());//获取MarketplaceId值
                String shopName=shop.getShopName();//获得店铺名称
                // Get a client connection.
*//*
                MarketplaceWebServiceOrdersAsyncClient client = MarketplaceWebServiceOrdersSampleConfig.getAsyncClient(serviceURL);
*//*
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
                    if ((ListOrdersResponse.class.getName()).equals(className) == true) {
                        System.out.println("responseList 类型是 ListOrdersResponse。");
                        ListOrdersResponse response = (ListOrdersResponse) tempResponse;
                        listOrdersResponseDto = XMLUtil.analysisListOrdersResponse(response.toXML());
                        isSuccess = true;
                    } else {
                        isSuccess = false;
                        continue aaa;
                    }
                }
                listOrdersResponseDtos.add(listOrdersResponseDto);//封装解析出来的
                if (listOrdersResponseDto.getNextToken() != null && isSuccess == true) {
                    //只有有NextToken标识，就一直获取
                    //判断下一页的订单数据
                    ListOrdersResponseDto listOrdersByNextTokenResponseDto=null;
                    List<ListOrdersByNextTokenRequest> listOrdersByNextTokenRequests = new ArrayList<ListOrdersByNextTokenRequest>();
                    ListOrdersByNextTokenRequest listOrdersByNextTokenRequest = new ListOrdersByNextTokenRequest();
                    listOrdersByNextTokenRequest.setSellerId(sellerId);
                    listOrdersByNextTokenRequest.setMWSAuthToken(mwsAuthToken);
                    listOrdersByNextTokenRequest.setNextToken(listOrdersResponseDto.getNextToken());
                    listOrdersByNextTokenRequests.add(listOrdersByNextTokenRequest);
                    *//**
                     * 获得订单下一页的响应列表
                     *//*
                    List<Object> responseList2 =invokeListOrdersByNextToken(client, listOrdersByNextTokenRequests);
                    Boolean isSuccess2 = false;
                    for (Object tempResponse : responseList2) {
                        // Object 转换 listOrdersByNextTokenResponseDto 还是 MarketplaceWebServiceOrdersException
                        String className = tempResponse.getClass().getName();
                        if ((ListOrdersByNextTokenResponse.class.getName()).equals(className) == true) {
                            System.out.println("responseList2 类型是 ListOrdersByNextTokenResponse。");
                            ListOrdersByNextTokenResponse response = (ListOrdersByNextTokenResponse) tempResponse;
                            //解析订单下一页的响应列表
                            listOrdersByNextTokenResponseDto = XMLUtil.analysisListOrdersByNextTokenResponse(response.toXML());
                            isSuccess2=true;
                        }else{
                            isSuccess2=false;
                            continue;
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
                        List<OrderModel> orderModelList = new ArrayList<OrderModel>();
                        for(int k=0;k<orderItemResponseDtos.size();k++){
                            for(int m=0;m<orderItemResponseDtos.get(k).getOrderItems().size();m++){
                                String product_asin=orderItemResponseDtos.get(k).getOrderItems().get(m).getASIN();
                                String product_sku=orderItemResponseDtos.get(k).getOrderItems().get(m).getSellerSKU();
                                int ordernumber=orderItemResponseDtos.get(k).getOrderItems().get(m).getQuantityOrdered();
                                String orderItemId=orderItemResponseDtos.get(k).getOrderItems().get(m).getOrderItemId();//获得订单商品编号
                                ProductShipAddressEntity addressEntity=new ProductShipAddressEntity();
                                String shipname=listOrdersResponseDtos.get(i).getOrders().get(j).getName();
                                String shipaddress=listOrdersResponseDtos.get(i).getOrders().get(j).getAddressLine1();
                                String shipaddress2=listOrdersResponseDtos.get(i).getOrders().get(j).getAddressLine2();
                                String shipcity=listOrdersResponseDtos.get(i).getOrders().get(j).getCity();
                                String shipCounty=listOrdersResponseDtos.get(i).getOrders().get(j).getCounty();//县
                                String shipCountry=listOrdersResponseDtos.get(i).getOrders().get(j).getCountryCode();//国家
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
                                if(orderItemId!=null){
                                    orderModel.setOrderItemId(orderItemId);
                                }else{
                                    orderModel.setOrderItemId("");
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
                                if(shipCounty!=null){
                                    addressEntity.setShipCounty(shipCounty);
                                }else{
                                    addressEntity.setShipCounty("");
                                }
                                if(shipCountry!=null){
                                    addressEntity.setShipCountry(shipCountry);
                                }else{
                                    addressEntity.setShipCountry("");
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

                        }
                        //开启一个线程去对接业务逻辑并保存到数据库
                        if(orderModelList.size() > 0){
                            System.out.println("执行更新");
                            new SaveOrUpdateOrderThread(orderModelList).start();
                        }


                    }


                }

            }
        }
    }*/







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

    public static List<Object> invokeGetOrder(MarketplaceWebServiceOrdersAsync client, List<GetOrderRequest> requestList) {
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
    /**
     * 更新订单线程
     * 定时获取到订单后执行
     */
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