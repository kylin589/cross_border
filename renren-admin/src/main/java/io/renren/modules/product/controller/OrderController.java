package io.renren.modules.product.controller;

import com.aliyun.oss.model.Grant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.entity.AmazonMarketplaceEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.service.AmazonMarketplaceService;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.amazon.util.FileUtil;
import io.renren.modules.logistics.DTO.ReceiveOofayData;
import io.renren.modules.logistics.entity.*;
import io.renren.modules.logistics.service.AbroadLogisticsService;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.logistics.service.SubmitLogisticsService;
import io.renren.modules.logistics.util.AbroadLogisticsUtil;
import io.renren.modules.logistics.util.XmlUtils;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.entity.RemarkEntity;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.order.service.RemarkService;
import io.renren.modules.product.dto.OrderDTO;
import io.renren.modules.product.entity.DataDictionaryEntity;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.DataDictionaryService;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.vm.OrderModel;
import io.renren.modules.product.vm.OrderVM;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.NoticeEntity;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.NoticeService;
import io.renren.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



/**
 * 
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-19 14:59:13
 */
@RestController
@RequestMapping("product/order")
public class OrderController extends AbstractController{
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductShipAddressService productShipAddressService;
    @Autowired
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private AbroadLogisticsService abroadLogisticsService;
    @Autowired
    private RemarkService remarkService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private AmazonMarketplaceService amazonMarketplaceService;
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;
    @Autowired
    private SubmitLogisticsService submitLogisticsService;
    @Autowired
    private AmazonGrantService amazonGrantService;
    @Value(("${file.path}"))
    private String fileStoragePath;
    /**
     * 我的订单
     */
    @RequestMapping("/getMyList")
//    @RequiresPermissions("product:order:mylist")
    public R getMyList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = orderService.queryMyPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }
    /**
     * 我的订单(旧)
     */
    @RequestMapping("/getOldMyList")
//    @RequiresPermissions("product:order:mylist")
    public R getOldMyList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = orderService.queryOldMyPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }
    /**
     * 所有订单
     */
    @RequestMapping("/getAllList")
//    @RequiresPermissions("product:order:alllist")
    public R getAllList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = orderService.queryAllPage(params, getDeptId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }
    /**
     * 所有订单(旧)
     */
    @RequestMapping("/getOldAllList")
//    @RequiresPermissions("product:order:alllist")
    public R getOldAllList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = orderService.queryOldAllPage(params, getDeptId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }
    /**
     * 订单详情
     */
    @RequestMapping("/getOrderInfo")
    public R getOrderInfo(@RequestParam(value = "orderId") Long orderId){
        OrderEntity orderEntity = orderService.selectById(orderId);
        BigDecimal momentRate = orderEntity.getMomentRate();
        String orderStatus = orderEntity.getOrderStatus();
        String abnormalStatus = orderEntity.getAbnormalStatus();
        new RefreshOrderThread(orderId).start();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setAmazonOrderId(orderEntity.getAmazonOrderId());
        orderDTO.setBuyDate(orderEntity.getBuyDate());
        orderDTO.setOrderStatus(orderStatus);
        orderDTO.setOrderState(orderEntity.getOrderState());

        orderDTO.setAbnormalStatus(abnormalStatus);
        orderDTO.setAbnormalState(orderEntity.getAbnormalState());

        orderDTO.setShopName(orderEntity.getShopName());
        orderDTO.setProductId(orderEntity.getProductId());
        orderDTO.setProductSku(orderEntity.getProductSku());
        ProductsEntity productsEntity = productsService.selectById(orderEntity.getProductId());
        if(orderEntity.getProductTitle() != null){
            orderDTO.setProductTitle(orderEntity.getProductTitle());
        }else if(productsEntity != null){
            orderDTO.setProductTitle(productsEntity.getProductTitle());
        }

        orderDTO.setProductAsin(orderEntity.getProductAsin());
        orderDTO.setOrderNumber(orderEntity.getOrderNumber());
        orderDTO.setPurchasePrice(orderEntity.getPurchasePrice());
        ProductShipAddressEntity shipAddress = productShipAddressService.selectOne(
                new EntityWrapper<ProductShipAddressEntity>().eq("order_id",orderId)
        );
        if(shipAddress == null){
            orderDTO.setShipAddress(new ProductShipAddressEntity());
        }else{
            orderDTO.setShipAddress(shipAddress);
        }

        List<DomesticLogisticsEntity> domesticLogisticsList = domesticLogisticsService.selectList(
                new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",orderId)
        );
        orderDTO.setDomesticLogisticsList(domesticLogisticsList);
        //国际物流
        AbroadLogisticsEntity abroadLogistics = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
        if(abroadLogistics == null){
            orderDTO.setAbroadLogistics(new AbroadLogisticsEntity());
        }else{
            orderDTO.setAbroadLogistics(abroadLogistics);
        }

        //设置amazon产品链接
//        amazonProductUrl
        AmazonMarketplaceEntity amazonMarketplaceEntity = amazonMarketplaceService.selectOne(new EntityWrapper<AmazonMarketplaceEntity>().eq("country_code",orderEntity.getCountryCode()));
        String amazonProductUrl = amazonMarketplaceEntity.getMwsPoint() + "/gp/product/" + orderEntity.getProductAsin();
        orderDTO.setAmazonProductUrl(amazonProductUrl);
        orderDTO.setProductImageUrl(orderEntity.getProductImageUrl());
        orderDTO.setMomentRate(momentRate);
        //判断订单异常状态——不属于退货
        if(!ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN.equals(abnormalStatus)){
            //国际已发货、已完成订单
            if(ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED.equals(orderStatus) || ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH.equals(orderStatus)){
                //获取订单金额（外币）
                BigDecimal orderMoneyForeign = orderEntity.getOrderMoney();
                //设置订单金额（外币）
                orderDTO.setOrderMoneyForeign(orderMoneyForeign);
                //设置订单金额（人民币）
                orderDTO.setOrderMoney(orderEntity.getOrderMoneyCny());
                //设置Amazon佣金（外币）
                orderDTO.setAmazonCommissionForeign(orderEntity.getAmazonCommission());
                //设置Amazon佣金（人民币）
                orderDTO.setAmazonCommission(orderEntity.getAmazonCommissionCny());
                //设置到账金额（外币）
                orderDTO.setAccountMoneyForeign(orderEntity.getAccountMoney());
                //设置到账金额（人民币）
                orderDTO.setAccountMoney(orderEntity.getAccountMoneyCny());
                //采购价
                BigDecimal purchasePrice = orderEntity.getPurchasePrice();
                orderDTO.setPurchasePrice(purchasePrice);
                //国际运费
                BigDecimal interFreight = orderEntity.getInterFreight();
                orderDTO.setInterFreight(interFreight);
                //平台佣金
                BigDecimal platformCommissions = orderEntity.getPlatformCommissions();
                orderDTO.setPlatformCommissions(platformCommissions);
                //利润
                BigDecimal orderProfit = orderEntity.getOrderProfit();
                orderDTO.setOrderProfit(orderProfit);
                //利润率
                BigDecimal profitRate = orderEntity.getProfitRate();
                orderDTO.setProfitRate(profitRate);
            }else {
                //属于取消订单不处理
                if(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(orderStatus)){

                }else{
                    //不属于取消订单
                    //获取订单金额（外币）
                    BigDecimal orderMoneyForeign = orderEntity.getOrderMoney();
                    //设置订单金额（外币）
                    orderDTO.setOrderMoneyForeign(orderMoneyForeign);
                    //设置订单金额（人民币）
                    orderDTO.setOrderMoney(orderEntity.getOrderMoneyCny());
                    //设置Amazon佣金（外币）
                    orderDTO.setAmazonCommissionForeign(orderEntity.getAmazonCommission());
                    //设置Amazon佣金（人民币）
                    orderDTO.setAmazonCommission(orderEntity.getAmazonCommissionCny());
                    //设置到账金额（外币）
                    orderDTO.setAccountMoneyForeign(orderEntity.getAccountMoney());
                    //设置到账金额（人民币）
                    orderDTO.setAccountMoney(orderEntity.getAccountMoneyCny());
                }
            }
        }else{
            //退货
            //获取订单金额（外币）
            BigDecimal orderMoneyForeign = orderEntity.getOrderMoney();
            orderDTO.setOrderMoneyForeign(orderMoneyForeign);
            orderDTO.setOrderMoney(orderMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //获取Amazon佣金（外币）
            BigDecimal amazonCommissionForeign = orderEntity.getAmazonCommission();
            orderDTO.setAmazonCommissionForeign(amazonCommissionForeign);
            orderDTO.setAmazonCommission(amazonCommissionForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //到账金额
            BigDecimal accountMoneyForeign = orderEntity.getAccountMoney();
            orderDTO.setAccountMoneyForeign(accountMoneyForeign);
            orderDTO.setAccountMoney(accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //平台佣金
            BigDecimal platformCommissions = orderEntity.getPlatformCommissions();
            orderDTO.setPlatformCommissions(platformCommissions);
            //国际运费
            BigDecimal interFreight = orderEntity.getInterFreight();
            orderDTO.setInterFreight(interFreight);
            //退货费用
            BigDecimal returnCost = orderEntity.getReturnCost();
            orderDTO.setReturnCost(returnCost);
            //利润
            BigDecimal orderProfit = orderEntity.getOrderProfit();
            orderDTO.setOrderProfit(orderProfit);
            //利润率
            BigDecimal profitRate = orderEntity.getProfitRate();
            orderDTO.setProfitRate(profitRate);
        }
        List<RemarkEntity> remarkList = remarkService.selectList(new EntityWrapper<RemarkEntity>().eq("type","remark").eq("order_id",orderId));
        System.out.println("remarkList:" + remarkList.size());
        List<RemarkEntity> logList = remarkService.selectList(new EntityWrapper<RemarkEntity>().eq("type","log").eq("order_id",orderId));
        orderDTO.setRemarkList(remarkList);
        orderDTO.setLogList(logList);
        return R.ok().put("orderDTO",orderDTO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:order:save")
    public R save(@RequestBody OrderEntity order){
        orderService.insert(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:order:update")
    public R update(@RequestBody OrderEntity order){
        //ValidatorUtils.validateEntity((order);
        //全部更新
        orderService.updateAllColumnById(order);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:order:delete")
    public R delete(@RequestBody Long[] orderIds){
        orderService.deleteBatchIds(Arrays.asList(orderIds));

        return R.ok();
    }

    /**
     * 删除国内物流
     */
    @RequestMapping("/deleteLogistic")
    public R deleteLogistic(@RequestParam Long domesticLogisticsId){
        DomesticLogisticsEntity logisticsEntity = domesticLogisticsService.selectById(domesticLogisticsId);
        OrderEntity orderEntity = orderService.selectById(logisticsEntity.getOrderId());
        domesticLogisticsService.deleteById(domesticLogisticsId);
        List<DomesticLogisticsEntity> list = domesticLogisticsService.selectList(new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",orderEntity.getOrderId()));
        BigDecimal purchasePrice = new BigDecimal(0.0);
        if(list != null && list.size() > 0){
            for(DomesticLogisticsEntity domestic : list){
                purchasePrice = purchasePrice.add(domestic.getPrice());
            }
        }
        orderEntity.setPurchasePrice(purchasePrice);
        //已有利润
        if(orderEntity.getProfitRate().compareTo(new BigDecimal(0.0)) != 0){
            //利润率
            //新利润
            BigDecimal orderProfit = orderEntity.getOrderProfit().add(logisticsEntity.getPrice());
            orderEntity.setOrderProfit(orderProfit);
            BigDecimal profitRate = orderProfit.divide(orderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
            orderEntity.setProfitRate(profitRate);
            orderService.updateById(orderEntity);
        }
        orderService.updateById(orderEntity);
        RemarkEntity remark = new RemarkEntity();
        remark.setOrderId(orderEntity.getOrderId());
        remark.setUserName(getUser().getDisplayName());
        remark.setUserId(getUserId());
        remark.setRemark("删除采购信息");
        remark.setType("log");
        remark.setUpdateTime(new Date());
        return R.ok();
    }
    /**
     * 修改订单状态
     */
    @RequestMapping("/updateState")
    public R updateState(@RequestBody OrderVM orderVM){
        boolean flag = orderService.updateState(orderVM.getOrderId(),orderVM.getOrderState());
        if(flag){
            //添加操作日志
            RemarkEntity remark = new RemarkEntity();
            remark.setOrderId(orderVM.getOrderId());
            remark.setType("log");
            if("已采购".equals(orderVM.getOrderState())){
                remark.setRemark("订单已采购");
            }else if("待签收".equals(orderVM.getOrderState())){
                //推送订单
                Map<String,String> result = orderService.pushOrder(orderVM.getOrderId());
                if("false".equals(result.get("code"))){
                    return R.error("订单推送失败,错误原因：" + result.get("msg"));
                }
                remark.setRemark("订单已发货");
            }else if("完成".equals(orderVM.getOrderState())){
                remark.setRemark("订单已完成");
            }
            remark.setUserId(getUserId());
            remark.setUserName(getUser().getDisplayName());
            remark.setUpdateTime(new Date());
            remarkService.insert(remark);
            return R.ok();
        }
        return R.error();
    }
    /**
     * 修改订单异常状态
     */
    @RequestMapping("/updateAbnormalState")
    public R updateAbnormalState(@RequestBody OrderVM orderVM){
        boolean flag = orderService.updateAbnormalState(orderVM.getOrderIds(), orderVM.getAbnormalStatus(), orderVM.getAbnormalState());
        if(flag){
            return R.ok();
        }
        return R.error();
    }
    /**
     * 生成国际运单号
     * orderVM: orderId,
     */
    @RequestMapping("/createAbroadWaybill")
    public R createAbroadWaybill(@RequestBody OrderVM orderVM){
        //获取可用余额
        SysDeptEntity dept = deptService.selectById(getDeptId());
        if(dept.getAvailableBalance().compareTo(new BigDecimal(50.00)) != 1){
            return R.error("余额不足，请联系公司管理员及时充值后再次尝试");
        }

        Long orderId = orderVM.getOrderId();
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("YT3");
        for (int i = 0; i < 15; i++) {
            stringBuffer.append(random.nextInt(10));
        }
        //生成国际物流单号
        String abroadWaybill = stringBuffer.toString();
        OrderEntity order = orderService.selectById(orderId);
        //设置状态为虚发货
        order.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_SHIPPED);
        order.setOrderState("虚发货");
        //设置国际物流单号
        order.setAbroadWaybill(abroadWaybill);
        //生成国际物流对象
        AbroadLogisticsEntity abroadLogistics = new AbroadLogisticsEntity();
        abroadLogistics = new AbroadLogisticsEntity();
        abroadLogistics.setOrderId(orderId);
        abroadLogistics.setAbroadWaybill(abroadWaybill);
        abroadLogistics.setIsSynchronization(0);
        abroadLogistics.setCreateTime(new Date());
        abroadLogistics.setUpdateTime(new Date());
        abroadLogistics.setShipTime(DateUtils.addDateHours(order.getBuyDate(),8));
        abroadLogisticsService.insert(abroadLogistics);
        orderService.updateById(order);
        //准备订单国际物流上传信息模型
        SendDataMoedl sendDataMoedl = synchronizationXuModel(order,abroadLogistics);
        // 将运单号同步到亚马逊平台
        amazonUpdateLogistics(sendDataMoedl,orderId);
        return R.ok().put("abroadLogistics",abroadLogistics);
    }
    /**
     * 同步国际运单号
     * orderVM: orderId,
     */
    @RequestMapping("/synchronization")
    public R synchronization(@RequestBody OrderVM orderVM){
        Long orderId = orderVM.getOrderId();
        OrderEntity order = orderService.selectById(orderId);
        order.getAmazonOrderId();
        AbroadLogisticsEntity abroadLogistics = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
        String orderStatus = order.getOrderStatus();
        SendDataMoedl sendDataMoedl;
        if(ConstantDictionary.OrderStateCode.ORDER_STATE_SHIPPED.equals(orderStatus)){
            //准备订单国际物流上传信息模型——虚发货
            sendDataMoedl = synchronizationXuModel(order,abroadLogistics);
        }else{
            //准备订单国际物流上传信息模型——实际发货
            sendDataMoedl = synchronizationZhenModel(order,abroadLogistics);
        }
        // 将运单号同步到亚马逊平台
        amazonUpdateLogistics(sendDataMoedl,orderId);
        boolean flag = false;
        if(flag){
            abroadLogistics.setIsSynchronization(1);
            abroadLogistics.setUpdateTime(new Date());
            abroadLogisticsService.updateById(abroadLogistics);
            return R.ok();
        }else{
            return R.error("同步失败");
        }
    }

    /**
     * 虚发货——封装物流信息
     * 后置：上传数据到亚马逊
     * @param orderEntity
     * @param abroadLogisticsEntity
     */
    private SendDataMoedl synchronizationXuModel(OrderEntity orderEntity, AbroadLogisticsEntity abroadLogisticsEntity){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String orderItemId = orderEntity.getOrderItemId();
        String amazonOrderId = orderEntity.getAmazonOrderId();
        String abroadWaybill = abroadLogisticsEntity.getAbroadWaybill();
        Date date = abroadLogisticsEntity.getShipTime();
        Shipping u1 = new Shipping();
        u1.setMessageType("OrderFulfillment");
        Header header=new Header();
        header.setDocumentVersion("1.01");
        header.setMerchantIdentifier("MYID");//<MerchantIdentifier>此选项可以随便填写，，
        Message message=new Message();//如果要确认多个订单可以增加多个<message>
        message.setMessageID("1");
        OrderFulfillment orderful=new OrderFulfillment();
        orderful.setAmazonOrderID(amazonOrderId);
        orderful.setFulfillmentDate(simpleDateFormat.format(date));
        FulfillmentData fd=new FulfillmentData();
        fd.setCarrierName("Yun Express");
        fd.setShippingMethod("Standard");//<ShippingMethod>根据自己的需求可以有可以没有
        fd.setShipperTrackingNumber(abroadWaybill);
        Item item=new Item();
        item.setAmazonOrderItemCode(orderItemId);
        item.setQuantity(orderEntity.getOrderNumber().toString());
        AmazonGrantShopEntity shopEntity = amazonGrantShopService.selectById(orderEntity.getShopId());
        List<String> serviceURL = new ArrayList<>();
        List<String> marketplaceIds = new ArrayList<>();
        serviceURL.add(shopEntity.getMwsPoint());
        marketplaceIds.add(shopEntity.getMarketplaceId());//获取MarketplaceId值
        orderful.setFulfillmentData(fd);
        orderful.setItem(item);
        message.setOrderFulfillment(orderful);
        u1.setHeader(header);
        u1.setMessage(message);
        List<Shipping> list = new ArrayList<Shipping>();
        list.add(u1);
        AmazonGrantEntity amazonGrantEntity = amazonGrantService.selectById(shopEntity.getGrantId());
        String sellerId = amazonGrantEntity.getMerchantId();
        String mwsAuthToken = amazonGrantEntity.getGrantToken();
        SendDataMoedl sendDataMoedl = new SendDataMoedl(list,serviceURL,marketplaceIds,sellerId,mwsAuthToken);
        return sendDataMoedl;
    }
    /**
     * 真实发货信息 ——封装物流信息
     * 后置：上传数据到亚马逊
     * @param orderEntity
     * @param abroadLogisticsEntity
     */
    private SendDataMoedl synchronizationZhenModel(OrderEntity orderEntity, AbroadLogisticsEntity abroadLogisticsEntity){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String orderItemId = orderEntity.getOrderItemId();
        String amazonOrderId = orderEntity.getAmazonOrderId();
        String abroadWaybill = abroadLogisticsEntity.getAbroadWaybill();
        Date date = abroadLogisticsEntity.getShipTime();
        Shipping u1 = new Shipping();
        u1.setMessageType("OrderFulfillment");
        Header header=new Header();
        header.setDocumentVersion("1.01");
        header.setMerchantIdentifier("MYID");//<MerchantIdentifier>此选项可以随便填写，，
        Message message=new Message();//如果要确认多个订单可以增加多个<message>
        message.setMessageID("1");
        OrderFulfillment orderful=new OrderFulfillment();
        orderful.setAmazonOrderID(amazonOrderId);
        orderful.setFulfillmentDate(simpleDateFormat.format(date));
        FulfillmentData fd=new FulfillmentData();
        fd.setCarrierName(abroadLogisticsEntity.getDestTransportCompany());
        fd.setShippingMethod(abroadLogisticsEntity.getDestChannel());//<ShippingMethod>根据自己的需求可以有可以没有
        fd.setShipperTrackingNumber(abroadWaybill);
        Item item=new Item();
        item.setAmazonOrderItemCode(orderItemId);
        item.setQuantity(orderEntity.getOrderNumber().toString());
        AmazonGrantShopEntity shopEntity = amazonGrantShopService.selectById(orderEntity.getShopId());
        List<String> serviceURL = new ArrayList<>();
        List<String> marketplaceIds = new ArrayList<>();
        serviceURL.add(shopEntity.getMwsPoint());
        marketplaceIds.add(shopEntity.getMarketplaceId());//获取MarketplaceId值
        orderful.setFulfillmentData(fd);
        orderful.setItem(item);
        message.setOrderFulfillment(orderful);
        u1.setHeader(header);
        u1.setMessage(message);
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
        /**
         * 根据List数组，生成XML数据
         */
        String resultXml = XmlUtils.getXmlFromList(list);

        //打印生成xml数据
        FileWriter outdata = null;
        FileUtil.generateFilePath(fileStoragePath,"shipping");
        String filePath = FileUtil.generateFilePath(fileStoragePath,"shipping");
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
        String feedSubmissionId = submitLogisticsService.submitFeed(serviceURL.get(0),sellerId,mwsAuthToken,feedType,filePath);
        //进行数据上传(步骤二)
        List<String> feedSubmissionIds=submitLogisticsService.getFeedSubmissionList(serviceURL.get(0),sellerId,mwsAuthToken,feedSubmissionId);
        System.out.println("=========================="+feedSubmissionIds.get(0)+"=============================");
        if(feedSubmissionIds.size()>0 && feedSubmissionIds!=null){
            //进行数据上传(步骤三)
            submitLogisticsService.getFeedSubmissionResult(serviceURL.get(0),sellerId,mwsAuthToken,feedSubmissionIds.get(0));
            //同步成功后把物流状态改为同步
            AbroadLogisticsEntity abroadLogisticsEntity = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
            abroadLogisticsEntity.setIsSynchronization(1);
            abroadLogisticsService.updateById(abroadLogisticsEntity);
        }
    }
    /**
     * 手动更新订单状态
     * orderVM: orderId,
     */
    @RequestMapping("/manualUpdateOrder")
    public R manualUpdateOrder(@RequestBody OrderVM orderVM){
        Long orderId = orderVM.getOrderId();

        OrderEntity orderEntity = orderService.selectById(orderId);
        String status = orderEntity.getOrderStatus();
        String abStatus = orderEntity.getAbnormalStatus();
        String amazonOrderId = orderEntity.getAmazonOrderId();
        if(ConstantDictionary.OrderStateCode.ORDER_STATE_PENDING.equals(status) || ConstantDictionary.OrderStateCode.ORDER_STATE_UNSHIPPED.equals(status)){
            OrderModel orderModel = orderService.updateOrderAmazonStatus(amazonOrderId,orderEntity);
            if(orderModel != null){
                //amazon状态更新
                new RefreshAmazonStateThread(orderEntity,orderModel).start();
            }
        }else{
            //不为取消订单时执行
            if(!ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(status) && !ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH.equals(status) && !ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN.equals(abStatus)){
                //国际物流更新
                new RefreshOrderThread(orderId).start();
            }
        }
        return R.ok();
    }
    /**
     * 刷新订单亚马逊状态线程
     * 手动刷新订单时调用
     * 状态为亚马逊状态时
     */
    class RefreshAmazonStateThread extends Thread {

        private OrderEntity orderEntity;

        private OrderModel orderModel;

        public RefreshAmazonStateThread(OrderEntity orderEntity, OrderModel orderModel) {
            this.orderEntity = orderEntity;
            this.orderModel = orderModel;
        }

        @Override
        public void run() {
            String modelStatus = orderModel.getOrderStatus();
            //更新订单
            //获取状态判断是否为取消
            if (ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(modelStatus)) {
                orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED);
                orderEntity.setOrderState("取消");
            } else {
                //获取当前订单状态判断是否为待付款、已付款、虚发货
                if (Arrays.asList(ConstantDictionary.OrderStateCode.AMAZON_ORDER_STATE).contains(orderEntity.getOrderState())) {
                    //获取返回状态判断是否为待付款、已付款、虚发货
                    if (Arrays.asList(ConstantDictionary.OrderStateCode.AMAZON_ORDER_STATE).contains(modelStatus)) {
                        //判断两个状态不想等时更改状态
                        if (!modelStatus.equals(orderEntity.getOrderState())) {
                            orderEntity.setOrderStatus(modelStatus);
                            String orderState = dataDictionaryService.selectOne(
                                    new EntityWrapper<DataDictionaryEntity>()
                                            .eq("data_type", "AMAZON_ORDER_STATE")
                                            .eq("data_number", modelStatus)
                            ).getDataContent();
                            orderEntity.setOrderState(orderState);
                            orderService.updateById(orderEntity);
                        }
                    }
                }
                //新增/修改收货人信息
                ProductShipAddressEntity productShipAddressEntity = orderModel.getProductShipAddressEntity();
                if(productShipAddressEntity != null){//判断返回值是否有收件人信息
                    ProductShipAddressEntity shipAddress = productShipAddressService.selectOne(
                            new EntityWrapper<ProductShipAddressEntity>().eq("order_id",orderEntity.getOrderId())
                    );
                    if(shipAddress == null){
                        productShipAddressEntity.setOrderId(orderEntity.getOrderId());
                        productShipAddressService.insert(productShipAddressEntity);
                    }else{
                        productShipAddressEntity.setOrderId(shipAddress.getOrderId());
                        productShipAddressEntity.setShipAddressId(shipAddress.getShipAddressId());
                        productShipAddressService.updateById(productShipAddressEntity);
                    }
                }
            }
        }
    }
    /**
     * 刷新订单国际物流线程
     * 手动刷新订单时且状态不为亚马逊状态时调用
     */
    class RefreshOrderThread extends Thread   {
        private Long orderId;
        public RefreshOrderThread(Long orderId) {
            this.orderId = orderId;
        }

        @Override
        public void run() {
            OrderEntity orderEntity = orderService.selectById(orderId);
            String amazonOrderId = orderEntity.getAmazonOrderId();
            String abnormalStatus = orderEntity.getAbnormalStatus();
            String orderStatus = orderEntity.getOrderStatus();
            BigDecimal momentRate = orderEntity.getMomentRate();
            //不属于退货
            if(!ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN.equals(abnormalStatus)){
                //如果订单状态在待签收和入库时，更新订单的国际物流信息
                if(ConstantDictionary.OrderStateCode.ORDER_STATE_WAITINGRECEIPT.equals(orderStatus) || ConstantDictionary.OrderStateCode.ORDER_STATE_WAREHOUSING.equals(orderStatus)){
                    Map<String,Object> map = AbroadLogisticsUtil.getOrderDetail(amazonOrderId);
                    int status = 0;
                    if("true".equals(map.get("code"))){
                        orderEntity.setUpdateTime(new Date());
                        ReceiveOofayData receiveOofayData = (ReceiveOofayData)map.get("receiveOofayData");
                        //国际物流对象
                        AbroadLogisticsEntity abroadLogisticsEntity = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
                        //设置国际物流渠道
                        if(StringUtils.isNotBlank(receiveOofayData.getDestChannel())){
                            abroadLogisticsEntity.setDestChannel(receiveOofayData.getDestChannel());
                        }
                        //设置国际物流公司
                        if(StringUtils.isNotBlank(receiveOofayData.getDestTransportCompany())){
                            abroadLogisticsEntity.setDestTransportCompany(receiveOofayData.getDestTransportCompany());
                        }
                        //设置国际物流跟踪号(虚发货时已生成物流跟踪号，所以肯定不为空)
                        if(StringUtils.isNotBlank(receiveOofayData.getTrackWaybill()) && !abroadLogisticsEntity.getTrackWaybill().equals(receiveOofayData.getTrackWaybill())){
                            abroadLogisticsEntity.setTrackWaybill(receiveOofayData.getTrackWaybill());
                            abroadLogisticsEntity.setIsSynchronization(0);
                            NoticeEntity noticeEntity = new NoticeEntity();
                            noticeEntity.setCreateTime(new Date());
                            noticeEntity.setNoticeContent("订单编号：" + orderId + "的物流跟踪号发生变化，请尽快同步。");
                            noticeEntity.setUserId(orderEntity.getUserId());
                            noticeEntity.setDeptId(orderEntity.getDeptId());
                            noticeService.insert(noticeEntity);
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
                        if(StringUtils.isNotBlank(receiveOofayData.getActualWeight())){
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
                        if(StringUtils.isNotBlank(receiveOofayData.getInterFreight()) && orderEntity.getInterFreight().compareTo(new BigDecimal(0.00)) == 0){
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
                            orderService.updateById(orderEntity);
                        }
                        //状态转变为入库
                        if(receiveOofayData.isWarehousing && ConstantDictionary.OrderStateCode.ORDER_STATE_WAITINGRECEIPT.equals(orderStatus)){
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_WAREHOUSING);
                            orderEntity.setOrderState("入库");
                            abroadLogisticsEntity.setState("入库");
                        }else{
                            if(StringUtils.isNotBlank(receiveOofayData.getStatusStr())){
                                status = Integer.parseInt(receiveOofayData.getStatusStr());
                                if(status == 2){
                                    //国际已发货,并扣款
                                    orderService.internationalShipments(orderEntity);
                                    abroadLogisticsEntity.setState("出库");
                                }
                            }
                        }
                        abroadLogisticsEntity.setUpdateTime(new Date());
                        abroadLogisticsService.updateById(abroadLogisticsEntity);
                        orderService.updateById(orderEntity);
                    }
                }
            }
        }
    }
}
