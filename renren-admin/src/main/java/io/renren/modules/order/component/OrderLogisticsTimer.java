package io.renren.modules.order.component;


import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderRequest;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ResponseHeaderMetadata;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.amazon.util.FileUtil;
import io.renren.modules.logistics.DTO.ReceiveOofayData;
import io.renren.modules.logistics.entity.*;
import io.renren.modules.logistics.service.AbroadLogisticsService;
import io.renren.modules.logistics.service.SubmitLogisticsService;
import io.renren.modules.logistics.util.AbroadLogisticsUtil;
import io.renren.modules.logistics.util.XmlUtils;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.entity.ProductOrderItemEntity;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.product.service.ProductOrderItemService;
import io.renren.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Component("OrderLogisticsTimer")
public class OrderLogisticsTimer {
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
    private OrderService orderService;
    @Autowired
    private ProductOrderItemService ProductOrderItemService;
    @Autowired
    private AbroadLogisticsService abroadLogisticsService;

    @Autowired
    private SysDeptService deptService;

    @Autowired
    private AmazonGrantShopService amazonGrantShopService;

    @Autowired
    private AmazonGrantService amazonGrantService;

    @Autowired
    private SubmitLogisticsService submitLogisticsService;

    @Autowired
    private ProductOrderItemService productOrderItemService;

    @Value(("${file.path}"))
    private String fileStoragePath;

    /**
     * 更新订单物流信息
     */
    public void getOrderLogistics(){
        Date startDate = DateUtils.getTheDateNow1MonthsShort();
        List<OrderEntity> orderEntityList = orderService.selectList(
                new EntityWrapper<OrderEntity>()
                        .ge("buy_date",startDate).andNew()
                        .eq("order_status", ConstantDictionary.OrderStateCode.ORDER_STATE_WAITINGRECEIPT)
                        .or().eq("order_status", ConstantDictionary.OrderStateCode.ORDER_STATE_WAREHOUSING)
                        .or().eq("order_status", ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED)
        );
        if(orderEntityList != null && orderEntityList.size() >0){
//            new RefreshOrderThread(5548L).start();
            for(OrderEntity orderEntity : orderEntityList){
                refreshOrder(orderEntity.getOrderId());

            }
        }

    }
    @Async("taskExecutor")
    public void refreshOrder(Long orderId){
        //订单对象
        OrderEntity orderEntity = orderService.selectById(orderId);
        //国际物流对象
        AbroadLogisticsEntity abroadLogisticsEntity = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
        if(abroadLogisticsEntity == null){
            abroadLogisticsEntity = new AbroadLogisticsEntity();
        }
        String amazonOrderId = orderEntity.getAmazonOrderId();
        String abnormalStatus = orderEntity.getAbnormalStatus();
        String orderStatus = orderEntity.getOrderStatus();
//            BigDecimal momentRate = orderEntity.getMomentRate();
        //不属于退货
        if(!ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN.equals(abnormalStatus) || orderEntity.getInterFreight().compareTo(new BigDecimal(0.00)) == 0){
            Map<String,Object> map = AbroadLogisticsUtil.getOrderDetail(amazonOrderId);
            if("true".equals(map.get("code"))){
                orderEntity.setUpdateTime(new Date());
                ReceiveOofayData receiveOofayData = (ReceiveOofayData)map.get("receiveOofayData");
                //如果订单状态在物流仓库未签收和仓库已入库时，更新订单的国际物流信息
                if(ConstantDictionary.OrderStateCode.ORDER_STATE_WAITINGRECEIPT.equals(orderStatus) || ConstantDictionary.OrderStateCode.ORDER_STATE_WAREHOUSING.equals(orderStatus)){
                    int status = 0;
                    //状态转变为仓库已入库
                    if(receiveOofayData.isWarehousing && ConstantDictionary.OrderStateCode.ORDER_STATE_WAITINGRECEIPT.equals(orderStatus)){
                        orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_WAREHOUSING);
                        orderEntity.setOrderState("仓库已入库");
                        abroadLogisticsEntity.setState("仓库已入库");
                    }else{
                        if(StringUtils.isNotBlank(receiveOofayData.getStatusStr())){
                            status = Integer.parseInt(receiveOofayData.getStatusStr());
                            if(status == 3){
                                abroadLogisticsEntity.setState("出库");
                                //国际已发货
                                orderService.internationalShipments(orderEntity);
                            }
                        }
                    }
                    abroadLogisticsEntity.setUpdateTime(new Date());
                }
                //设置国际物流公司
                if(StringUtils.isNotBlank(receiveOofayData.getDestTransportCompany())){
                    System.out.println("物流公司：" + receiveOofayData.getDestTransportCompany());
                    abroadLogisticsEntity.setDestTransportCompany(receiveOofayData.getDestTransportCompany());
                    //设置国际物流渠道
                    if(StringUtils.isNotBlank(receiveOofayData.getDestChannel())){
                        abroadLogisticsEntity.setDestChannel(receiveOofayData.getDestChannel());
                    }else{
                        abroadLogisticsEntity.setDestChannel("Standard");
                    }
                }
                //设置国内跟踪号
                if(StringUtils.isNotBlank(receiveOofayData.getDomesticTrackWaybill())){
                    abroadLogisticsEntity.setDomesticTrackWaybill(receiveOofayData.getDomesticTrackWaybill());
                }
                //设置发货时间
                if(StringUtils.isNotBlank(receiveOofayData.getShipTime())){
                    DateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date shipTime = null;
                    try {
                        shipTime = df.parse(receiveOofayData.getShipTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    abroadLogisticsEntity.setShipTime(shipTime);
                }
                //设置实际重量
                if(StringUtils.isNotBlank(receiveOofayData.getActualWeight()) && !"0.0".equals(receiveOofayData.getActualWeight())){
                    abroadLogisticsEntity.setActualWeight(receiveOofayData.getActualWeight());
                }
                //设置目的地查询网址
                if(StringUtils.isNotBlank(receiveOofayData.getDestQueryUrl())){
                    abroadLogisticsEntity.setDestQueryUrl(receiveOofayData.getDestQueryUrl());
                }
                //设置服务查询网址
                if(StringUtils.isNotBlank(receiveOofayData.getServiceQueryUrl())){
                    abroadLogisticsEntity.setServiceQueryUrl(receiveOofayData.getServiceQueryUrl());
                }
                //设置联系电话
                if(StringUtils.isNotBlank(receiveOofayData.getMobile())){
                    abroadLogisticsEntity.setMobile(receiveOofayData.getMobile());
                }
                //有运费
                if(StringUtils.isNotBlank(receiveOofayData.getInterFreight()) && new BigDecimal(receiveOofayData.getInterFreight()).compareTo(new BigDecimal(0.00)) == 1 && orderEntity.getInterFreight().compareTo(new BigDecimal(0.00)) == 0){
                    //计算国际运费、平台佣金、利润
                    //国际运费
                    BigDecimal interFreight = new BigDecimal(receiveOofayData.getInterFreight());
                    abroadLogisticsEntity.setInterFreight(interFreight);
                    orderEntity.setInterFreight(interFreight);
                    //到账金额(人民币)
                    BigDecimal accountMoney = orderEntity.getAccountMoneyCny();
                    //平台佣金
                    BigDecimal companyPoint = deptService.selectById(orderEntity.getDeptId()).getCompanyPoints();
                    BigDecimal platformCommissions = accountMoney.multiply(companyPoint).setScale(2,BigDecimal.ROUND_HALF_UP);
                    orderEntity.setPlatformCommissions(platformCommissions);
                    //利润 到账-国际运费-采购价-平台佣金
                    BigDecimal orderProfit = accountMoney.subtract(orderEntity.getPurchasePrice()).subtract(interFreight).subtract(platformCommissions).setScale(2,BigDecimal.ROUND_HALF_UP);
                    orderEntity.setOrderProfit(orderProfit);
                    //利润率
                    BigDecimal profitRate = orderProfit.divide(orderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
                    orderEntity.setProfitRate(profitRate);
                    orderService.deduction(orderEntity);
                }
                //判断状态为国际已发货或已完成
                else if(ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED.equals(orderStatus) || ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH.equals(orderStatus)){
                    //判断订单数据中国际运费是否为空
                    if(orderEntity.getInterFreight().compareTo(new BigDecimal(0.00)) == 0){
                        orderEntity.setUpdateTime(new Date());
                        //有运费

                        if(StringUtils.isNotBlank(receiveOofayData.getInterFreight()) && new BigDecimal(receiveOofayData.getInterFreight()).compareTo(new BigDecimal(0.00)) == 1){
                            //计算国际运费、平台佣金、利润
                            //国际运费
                            BigDecimal interFreight = new BigDecimal(receiveOofayData.getInterFreight());
                            abroadLogisticsEntity.setInterFreight(interFreight);
                            orderEntity.setInterFreight(interFreight);
                            //到账金额(人民币)
                            BigDecimal accountMoney = orderEntity.getAccountMoneyCny();
                            //平台佣金
                            BigDecimal companyPoint = deptService.selectById(orderEntity.getDeptId()).getCompanyPoints();
                            BigDecimal platformCommissions = accountMoney.multiply(companyPoint).setScale(2,BigDecimal.ROUND_HALF_UP);
                            orderEntity.setPlatformCommissions(platformCommissions);
                            //利润 到账-国际运费-采购价-平台佣金
                            BigDecimal orderProfit = accountMoney.subtract(orderEntity.getPurchasePrice()).subtract(interFreight).subtract(platformCommissions).setScale(2,BigDecimal.ROUND_HALF_UP);
                            orderEntity.setOrderProfit(orderProfit);
                            //利润率
                            BigDecimal profitRate = orderProfit.divide(orderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
                            orderEntity.setProfitRate(profitRate);
                            orderService.deduction(orderEntity);
                        }
                    }

                    //设置转单号
                    if(StringUtils.isBlank(abroadLogisticsEntity.getTrackWaybill())){
                        Map<String,String> trackWaybillMap = AbroadLogisticsUtil.getTrackWaybill(amazonOrderId);
                        if("true".equals(trackWaybillMap.get("code"))){
                            String trackWaybill = trackWaybillMap.get("trackWaybill");
                            if(StringUtils.isNotBlank(trackWaybill)){
                                abroadLogisticsEntity.setTrackWaybill(trackWaybill);
                                abroadLogisticsEntity.setIsSynchronization(0);
                            }
                        }
                    }
                }
                if(abroadLogisticsEntity.getOrderId() == null){
                    abroadLogisticsEntity.setOrderId(orderId);
                    abroadLogisticsService.insert(abroadLogisticsEntity);
                }else{
                    abroadLogisticsService.updateById(abroadLogisticsEntity);
                }
                orderService.insertOrUpdate(orderEntity);
                //同步转单号
                if(StringUtils.isNotBlank(abroadLogisticsEntity.getTrackWaybill()) && abroadLogisticsEntity.getIsSynchronization() == 0){
                    SendDataMoedl sendDataMoedl = synchronizationZhenModel(orderEntity,abroadLogisticsEntity);
                    amazonUpdateLogistics(sendDataMoedl,orderId);
                }
            }
        }
    }


    /**
     * 真实发货信息 ——封装物流信息
     * 后置：上传数据到亚马逊
     * @param orderEntity
     * @param abroadLogisticsEntity
     */
    private SendDataMoedl synchronizationZhenModel(OrderEntity orderEntity, AbroadLogisticsEntity abroadLogisticsEntity){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String amazonOrderId = orderEntity.getAmazonOrderId();
        String trackWaybill = abroadLogisticsEntity.getTrackWaybill();
        //获取系统当前日期北京时间
        Date date = new Date();
        //北京时间减去8小时
        date=DateUtils.addDateHours(date,-8);
        //再把当前日期变成格林时间带T的.
        String shipDate=simpleDateFormat.format(date);
        Shipping u1 = new Shipping();
        u1.setMessageType("OrderFulfillment");
        Header header=new Header();
        header.setDocumentVersion("1.01");
        header.setMerchantIdentifier("MYID");//<MerchantIdentifier>此选项可以随便填写，，
        u1.setHeader(header);
        List<Message> messages=new ArrayList<>();
        int count=1;
        List<ProductOrderItemEntity> productOrderItemEntities=productOrderItemService.selectList(new EntityWrapper<ProductOrderItemEntity>().eq("amazon_order_id",amazonOrderId));
        for (ProductOrderItemEntity productOrderItemEntity:productOrderItemEntities) {
            Message message=new Message();//如果要确认多个订单可以增加多个<message>
            message.setMessageID(String.valueOf(count++));
            OrderFulfillment orderful=new OrderFulfillment();
            orderful.setAmazonOrderID(amazonOrderId);
            orderful.setFulfillmentDate(shipDate);
            FulfillmentData fd=new FulfillmentData();
            fd.setCarrierName("Yun Express");
            fd.setShippingMethod("Standard");//<ShippingMethod>根据自己的需求可以有可以没有
            fd.setShipperTrackingNumber(trackWaybill);
            Item item=new Item();
            String orderItemId= productOrderItemEntity.getOrderItemId();
            item.setAmazonOrderItemCode(orderItemId);
            item.setQuantity(String.valueOf(productOrderItemEntity.getOrderItemNumber()));
            orderful.setFulfillmentData(fd);
            orderful.setItem(item);
            message.setOrderFulfillment(orderful);
            messages.add(message);
        }
        AmazonGrantShopEntity shopEntity = amazonGrantShopService.selectById(orderEntity.getShopId());
        List<String> serviceURL = new ArrayList<>();
        List<String> marketplaceIds = new ArrayList<>();
        serviceURL.add(shopEntity.getMwsPoint());
        marketplaceIds.add(shopEntity.getMarketplaceId());//获取MarketplaceId值
        u1.setMessages(messages);
        List<Shipping> list = new ArrayList<Shipping>();
        list.add(u1);
        AmazonGrantEntity amazonGrantEntity = amazonGrantService.selectById(shopEntity.getGrantId());
        String sellerId = amazonGrantEntity.getMerchantId();
        String mwsAuthToken = amazonGrantEntity.getGrantToken();
        SendDataMoedl sendDataMoedl = new SendDataMoedl(list,serviceURL,marketplaceIds,sellerId,mwsAuthToken);
        return sendDataMoedl;
    }
    /**
     * 上传国际物流信息到amazon
     * @param sendDataMoedl
     */
    @Async("taskExecutor")
    public void amazonUpdateLogistics(SendDataMoedl sendDataMoedl,Long orderId){
        List<Shipping> list = sendDataMoedl.getList();
        List<String> serviceURL = sendDataMoedl.getServiceURL();
        List<String> marketplaceIds = sendDataMoedl.getMarketplaceIds();
        String sellerId = sendDataMoedl.getSellerId();
        String mwsAuthToken = sendDataMoedl.getMwsAuthToken();
        //获得授权表里的region字段的值，判断其逻辑
        AmazonGrantEntity amazonGrantEntity=amazonGrantService.selectOne(new EntityWrapper<AmazonGrantEntity>().eq("merchant_id",sellerId).eq("grant_token",mwsAuthToken));
        String accessKey=null;
        String secretKey=null;
        if(amazonGrantEntity!=null){
            int region= amazonGrantEntity.getRegion();
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
        }

        /**
         * 根据List数组，生成XML数据
         */
        String resultXml = XmlUtils.getXmlFromList(list);
        //打印生成xml数据
        FileWriter outdata = null;
        String filePath = FileUtil.generateFilePath(fileStoragePath,"shipping",orderId);
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
        String feedSubmissionId = submitLogisticsService.submitFeed(serviceURL.get(0),sellerId,mwsAuthToken,feedType,filePath,accessKey,secretKey);
        //进行数据上传(步骤二)
        List<String> feedSubmissionIds=submitLogisticsService.getFeedSubmissionList(serviceURL.get(0),sellerId,mwsAuthToken,feedSubmissionId,accessKey,secretKey);
        System.out.println("=========================="+feedSubmissionIds.get(0)+"=============================");
        if(feedSubmissionIds.size()>0 && feedSubmissionIds!=null){
            //进行数据上传(步骤三)
            submitLogisticsService.getFeedSubmissionResult(serviceURL.get(0),sellerId,mwsAuthToken,feedSubmissionIds.get(0),accessKey,secretKey);
            //同步成功后把物流状态改为同步
            AbroadLogisticsEntity abroadLogisticsEntity = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
            String amazonOrderId=orderService.selectById(orderId).getAmazonOrderId();
            //从后代调用接口获取亚马逊后台的订单状态
            String orderStatus="";
            MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
            config.setServiceURL(serviceURL.get(0));
            MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                    "my_test", "1.0", config, null);
            List<GetOrderRequest> requestList = new ArrayList<GetOrderRequest>();
            GetOrderRequest request = new GetOrderRequest();
            request.setSellerId(sellerId);
            request.setMWSAuthToken(mwsAuthToken);
            List<String> amazonOrderIds = new ArrayList<String>();
            amazonOrderIds.add(amazonOrderId);
            request.setAmazonOrderId(amazonOrderIds);
            requestList.add(request);
            List<Object> responseList=invokeGetOrder(client,requestList);
            Boolean isSuccess = false;
            GetOrderResponse getOrderResponse = null;
            for (Object tempResponse : responseList) {
                // Object 转换 ListOrdersResponse 还是 MarketplaceWebServiceOrdersException
                String className = tempResponse.getClass().getName();
                if ((GetOrderResponse.class.getName()).equals(className) == true) {
                    System.out.println("responseList 类型是 GetOrderResponse。");
                    GetOrderResponse response = (GetOrderResponse) tempResponse;
                    System.out.println(response.toXML());
                    orderStatus=response.toXML();
                    if(orderStatus.contains("<OrderStatus>")){
                        orderStatus= orderStatus.substring(orderStatus.indexOf("<OrderStatus>"),orderStatus.indexOf("</OrderStatus>")).replace("<OrderStatus>","");
                    }
                    isSuccess = true;
                } else {
                    System.out.println("responseList 类型是 MarketplaceWebServiceOrderException。");
                    isSuccess = false;
                    continue;
                }
            }

            //判读亚马逊后台订单的状态
            if("Shipped".equals(orderStatus)){
                abroadLogisticsEntity.setIsSynchronization(1);//表示同步成功
                abroadLogisticsService.updateById(abroadLogisticsEntity);
            }else{
//                logger.error("同步失败,请重新上传订单...");
                abroadLogisticsEntity.setIsSynchronization(0);//表示同步失败
                abroadLogisticsService.updateById(abroadLogisticsEntity);
            }

        }
    }
    /**
     * 获取单个订单的方法
     * @param client
     * @param requestList
     * @return
     */
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
}