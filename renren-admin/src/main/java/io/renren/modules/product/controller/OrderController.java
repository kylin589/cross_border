package io.renren.modules.product.controller;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderRequest;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ResponseHeaderMetadata;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.R;
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
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import io.renren.modules.product.vm.OrderItemModel;
import io.renren.modules.product.vm.OrderModel;
import io.renren.modules.product.vm.OrderVM;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.NoticeService;
import io.renren.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private ProductOrderItemService productOrderItemService;
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
    @Autowired
    private AmazonRateService amazonRateService;
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
        //更新订单物流等信息
//        new RefreshOrderThread(orderId).start();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setAmazonOrderId(orderEntity.getAmazonOrderId());
        orderDTO.setBuyDate(orderEntity.getBuyDate());
        orderDTO.setOrderStatus(orderStatus);
        orderDTO.setOrderState(orderEntity.getOrderState());

        orderDTO.setAbnormalStatus(abnormalStatus);
        orderDTO.setAbnormalState(orderEntity.getAbnormalState());
        orderDTO.setMomentRate(momentRate);
        String AmazonOrderId=orderEntity.getAmazonOrderId();
        List<ProductOrderItemEntity> productOrderItemEntitys=productOrderItemService.selectList(new EntityWrapper<ProductOrderItemEntity>().eq("amazon_order_id",AmazonOrderId));
        orderDTO.setShopName(orderEntity.getShopName());
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

/*        List<DomesticLogisticsEntity> domesticLogisticsList = domesticLogisticsService.selectList(
                new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",orderId)
        );
        orderDTO.setDomesticLogisticsList(domesticLogisticsList);*/
        //国际物流
        AbroadLogisticsEntity abroadLogistics = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
        if(abroadLogistics == null){
            orderDTO.setAbroadLogistics(new AbroadLogisticsEntity());
        }else{
            orderDTO.setAbroadLogistics(abroadLogistics);
        }
        for (ProductOrderItemEntity productOrderItemEntity:productOrderItemEntitys) {
            ProductsEntity productsEntity = productsService.selectById(productOrderItemEntity.getProductId());
            if(StringUtils.isBlank(productOrderItemEntity.getProductTitle()) && productsEntity != null){
                productOrderItemEntity.setProductTitle(productsEntity.getProductTitle());
            }
           //设置amazon产品链接 amazonProductUrl
            AmazonMarketplaceEntity amazonMarketplaceEntity = amazonMarketplaceService.selectOne(new EntityWrapper<AmazonMarketplaceEntity>().eq("country_code",orderEntity.getCountryCode()));
            String amazonProductUrl = amazonMarketplaceEntity.getAmazonSite() + "/gp/product/" + productOrderItemEntity.getProductAsin();
            productOrderItemEntity.setAmazonProductUrl(amazonProductUrl);
            DomesticLogisticsEntity domesticLogistics = domesticLogisticsService.selectOne(new EntityWrapper<DomesticLogisticsEntity>().eq("item_id",productOrderItemEntity.getItemId()));
            productOrderItemEntity.setDomesticLogistics(domesticLogistics);
        }
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
        orderDTO.setOrderProductList(productOrderItemEntitys);
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
     * 给物流那边同步修改后的物流单号
     */
    @RequestMapping("/synchronizeWaybill")
    public R synchronizeWaybill(@RequestParam Long orderId){
        OrderEntity orderEntity = orderService.selectById(orderId);
        List<DomesticLogisticsEntity> list = domesticLogisticsService.selectList(new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",orderId));
        StringBuffer supplyexpressno = new StringBuffer("");
        for(int i = 0; i < list.size(); i++){
            if(StringUtils.isNotBlank(list.get(i).getWaybill())){
                supplyexpressno.append(list.get(i).getWaybill());
                supplyexpressno.append(",");
            }
        }
        if(StringUtils.isNotBlank(supplyexpressno.toString())){
            AbroadLogisticsUtil.updateOrderWaybill(orderEntity.getAmazonOrderId(),supplyexpressno.substring(0,supplyexpressno.length()-1));
            RemarkEntity remark = new RemarkEntity();
            remark.setOrderId(orderEntity.getOrderId());
            remark.setUserName(getUser().getDisplayName());
            remark.setUserId(getUserId());
            remark.setRemark("同步物流单号");
            remark.setType("log");
            remark.setUpdateTime(new Date());
            return R.ok();
        }
        return R.error("没有可同步的物流单号");
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
        if("物流仓库未签收".equals(orderVM.getOrderState())){
            //获取可用余额
            SysDeptEntity dept = deptService.selectById(getDeptId());
            if(dept.getAvailableBalance().compareTo(new BigDecimal(500.00)) != 1){
                return R.error("余额不足，请联系公司管理员及时充值后再次尝试");
            }
            //推送订单
            Map<String,String> result = orderService.pushOrder(orderVM.getOrderId());
            if("false".equals(result.get("code"))){
                return R.error("订单推送失败,错误原因：" + result.get("msg"));
            }
        }
        boolean flag = orderService.updateState(orderVM.getOrderId(),orderVM.getOrderState());
        if(flag){
            //添加操作日志
            RemarkEntity remark = new RemarkEntity();
            remark.setOrderId(orderVM.getOrderId());
            remark.setType("log");
            if("国内物流已采购".equals(orderVM.getOrderState())){
                remark.setRemark("订单国内物流已采购");
            }else if("物流仓库未签收".equals(orderVM.getOrderState())){
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
/*        SysDeptEntity dept = deptService.selectById(getDeptId());
        if(dept.getAvailableBalance().compareTo(new BigDecimal(50.00)) != 1){
            return R.error("余额不足，请联系公司管理员及时充值后再次尝试");
        }*/
        Long orderId = orderVM.getOrderId();
        AbroadLogisticsEntity abroadLogistics = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));

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
        if(abroadLogistics != null){
            abroadLogistics.setAbroadWaybill(abroadWaybill);
            abroadLogistics.setIsSynchronization(2);//表示正在同步中
            abroadLogistics.setUpdateTime(new Date());
            abroadLogistics.setShipTime(new Date());
            abroadLogisticsService.updateById(abroadLogistics);
        }else{
            //生成国际物流对象
            abroadLogistics = new AbroadLogisticsEntity();
            abroadLogistics.setOrderId(orderId);
            abroadLogistics.setAbroadWaybill(abroadWaybill);
            abroadLogistics.setIsSynchronization(2);//表示正在同步中
            abroadLogistics.setCreateTime(new Date());
            abroadLogistics.setUpdateTime(new Date());
            System.out.println(order.getBuyDate());
            abroadLogistics.setShipTime(new Date());
            abroadLogisticsService.insert(abroadLogistics);
        }
        orderService.updateById(order);
        //准备订单国际物流上传信息模型
        SendDataMoedl sendDataMoedl = synchronizationXuModel(order,abroadLogistics);
        // 将运单号同步到亚马逊平台
        orderService.amazonUpdateLogistics(sendDataMoedl,orderId);
        return R.ok().put("abroadLogistics",abroadLogistics);
    }
    /**
     * 同步国际运单号
     * orderVM: orderId,
     */
    @RequestMapping("/synchronization")
    public R synchronization(@RequestParam Long orderId){
        OrderEntity order = orderService.selectById(orderId);
        AbroadLogisticsEntity abroadLogistics = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
        if(abroadLogistics == null){
            abroadLogistics = new AbroadLogisticsEntity();
            Random random = new Random();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("YT3");
            for (int i = 0; i < 15; i++) {
                stringBuffer.append(random.nextInt(10));
            }
            //生成国际物流单号
            String abroadWaybill = stringBuffer.toString();
            abroadLogistics.setOrderId(orderId);
            abroadLogistics.setAbroadWaybill(abroadWaybill);
            abroadLogistics.setIsSynchronization(2);//表示正在同步中
            abroadLogistics.setCreateTime(new Date());
            abroadLogistics.setUpdateTime(new Date());
            System.out.println(order.getBuyDate());
            abroadLogistics.setShipTime(new Date());
            abroadLogisticsService.insert(abroadLogistics);
            //设置国际物流单号
            order.setAbroadWaybill(abroadWaybill);
            orderService.updateById(order);
        }else{
            abroadLogistics.setIsSynchronization(2);//表示正在同步中
            abroadLogistics.setUpdateTime(new Date());
            abroadLogistics.setShipTime(new Date());
            abroadLogisticsService.updateById(abroadLogistics);
        }


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
        orderService.amazonUpdateLogistics(sendDataMoedl,orderId);
        return R.ok("正在同步，请稍后查看");
    }

    /**
     * 虚发货——封装物流信息
     * 后置：上传数据到亚马逊
     * @param orderEntity
     * @param abroadLogisticsEntity
     */
    private SendDataMoedl synchronizationXuModel(OrderEntity orderEntity, AbroadLogisticsEntity abroadLogisticsEntity){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        String amazonOrderId = orderEntity.getAmazonOrderId();
        String abroadWaybill = abroadLogisticsEntity.getAbroadWaybill();
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
            fd.setShipperTrackingNumber(abroadWaybill);
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
//                new RefreshAmazonStateThread(orderEntity,orderModel).start();
                //调用启动线程池的方法
                RefreshAmazonState(orderEntity,orderModel);
            }
        }else{
            //不为取消订单时执行
            if(!ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(status) && !ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH.equals(status) && !ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN.equals(abStatus)){
                //国际物流更新
//                new RefreshOrderThread(orderId).start();
                RefreshOrder(orderId);
            }
        }
        return R.ok();
    }

    @Async("taskExecutor")
    public void RefreshAmazonState(OrderEntity orderEntity,OrderModel orderModel){
        String modelStatus = orderModel.getOrderStatus();
        //更新订单
        //获取状态判断是否为取消
            /*if (ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(modelStatus)) {
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
            }*/
        //更新订单
        List<OrderItemModel> orderItemModels=orderModel.getOrderItemModels();
        if(orderItemModels!=null && orderItemModels.size()>0) {
            for (OrderItemModel orderItemModel : orderItemModels) {
                //判断该商品是否存在
                ProductOrderItemEntity productOrderItemEntity = productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id", orderItemModel.getOrderItemId()));
                //存在更新
                ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku", orderItemModel.getProductSku()));
                if (StringUtils.isNotBlank(orderItemModel.getProductImageUrl())) {
                    productOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                } else if (productsEntity != null) {
                    productOrderItemEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                }
                //更新订单商品
//                            orderEntity.setProductTitle(orderModel.getTitlename());
//                            orderEntity.setCountryCode(orderModel.getCountry());
                productOrderItemEntity.setProductTitle(orderItemModel.getProductTitle());
                productOrderItemEntity.setProductSku(orderItemModel.getProductSku());
                productOrderItemEntity.setProductAsin(orderItemModel.getProductAsin());
                productOrderItemEntity.setProductPrice(orderItemModel.getProductPrice());
                productOrderItemEntity.setUpdatetime(new Date());
                productOrderItemService.updateById(productOrderItemEntity);
            }
        }
            /*String orderItemId=orderModel.getOrderItemId();
            ProductOrderItemEntity productOrderItemEntity=productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id",orderItemId));

            ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku",productOrderItemEntity.getProductSku()));
            if(StringUtils.isNotBlank(productOrderItemEntity.getProductImageUrl())){
                orderEntity.setProductImageUrl(productOrderItemEntity.getProductImageUrl());
            }else if(productsEntity != null){
                orderEntity.setProductImageUrl(productsEntity.getMainImageUrl());
            }

            orderEntity.setProductTitle(orderModel.getTitlename());
*/
        orderEntity.setCountryCode(orderModel.getCountry());
        //设置汇率
        BigDecimal rate = new BigDecimal(0.00);
        String rateCode = orderModel.getCurrencyCode();
        if(StringUtils.isNotBlank(rateCode)){
            rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code",rateCode)).getRate();
        }
        BigDecimal orderMoney = orderModel.getOrderMoney();
        if(orderMoney.compareTo(new BigDecimal("0.00")) != 0){
            orderEntity.setOrderMoney(orderMoney);
            orderEntity.setOrderMoneyCny(orderMoney.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //获取Amazon佣金（外币）
            BigDecimal amazonCommission = orderMoney.multiply(new BigDecimal(0.15).setScale(2,BigDecimal.ROUND_HALF_UP));
            orderEntity.setAmazonCommission(amazonCommission);
            orderEntity.setAmazonCommissionCny(amazonCommission.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //到账金额
            BigDecimal accountMoney = orderMoney.subtract(amazonCommission);
            orderEntity.setAccountMoney(accountMoney);
            orderEntity.setAccountMoneyCny(accountMoney.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        //获取状态判断是否为取消
        if(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(modelStatus)){
            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED);
            orderEntity.setOrderState("取消");
        }else{
            String orderStatus = orderEntity.getOrderStatus();
            //获取当前订单状态判断是否为待付款、已付款、虚发货
            List amazonStateList = Arrays.asList(ConstantDictionary.OrderStateCode.AMAZON_ORDER_STATE);
            if(amazonStateList.contains(orderStatus)){
                //获取返回状态判断是否为待付款、已付款、虚发货
                if(amazonStateList.contains(modelStatus)){
                    //判断两个状态不想等时更改状态
                    if(!modelStatus.equals(orderStatus)){
                        orderEntity.setOrderStatus(modelStatus);
                        String orderState = dataDictionaryService.selectOne(
                                new EntityWrapper<DataDictionaryEntity>()
                                        .eq("data_type","AMAZON_ORDER_STATE")
                                        .eq("data_number",modelStatus)
                        ).getDataContent();

                        orderEntity.setOrderState(orderState);
                        orderService.updateById(orderEntity);
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
        }
    }

    @Async("taskExecutor")
    public void RefreshOrder(Long orderId){
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
     * 上传国际物流信息到amazon
     * @param sendDataMoedl
     */
    public void amazonUpdateLogistics(SendDataMoedl sendDataMoedl, Long orderId){
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
//        FileUtil.generateFilePath(fileStoragePath,"shipping",orderId);
        String filePath = FileUtil.generateFilePath(fileStoragePath,"shipping", orderId);
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
                logger.error("同步失败,请重新上传订单...");
                abroadLogisticsEntity.setIsSynchronization(0);//表示同步失败
                abroadLogisticsService.updateById(abroadLogisticsEntity);
            }

        }
    }

    /**
     * 更新亚马逊订单的方法
     */
   /* class AmazonUpdateLogisticsThread extends Thread{
        private SendDataMoedl sendDataMoedl;
        private Long orderId;

        public AmazonUpdateLogisticsThread(SendDataMoedl sendDataMoedl,Long orderId){
            this.sendDataMoedl=sendDataMoedl;
            this.orderId=orderId;
        }
        @Override
        public void run(){
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

            *//**
             * 根据List数组，生成XML数据
             *//*
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
            String feedSubmissionId = submitLogisticsService.submitFeed(serviceURL.get(0),sellerId,mwsAuthToken,feedType,filePath,accessKey,secretKey);
            //进行数据上传(步骤二)
            List<String> feedSubmissionIds=submitLogisticsService.getFeedSubmissionList(serviceURL.get(0),sellerId,mwsAuthToken,feedSubmissionId,accessKey,secretKey);
            System.out.println("=========================="+feedSubmissionIds.get(0)+"=============================");
            if(feedSubmissionIds.size()>0 && feedSubmissionIds!=null){
                //进行数据上传(步骤三)
                submitLogisticsService.getFeedSubmissionResult(serviceURL.get(0),sellerId,mwsAuthToken,feedSubmissionIds.get(0),accessKey,secretKey);
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
                    logger.error("同步失败,请重新上传订单...");
                    abroadLogisticsEntity.setIsSynchronization(0);//表示同步失败
                    abroadLogisticsService.updateById(abroadLogisticsEntity);
                }

            }
        }
    }*/
    /**
     * 刷新订单亚马逊状态线程
     * 手动刷新订单时调用
     * 状态为亚马逊状态时
     */
    /*class RefreshAmazonStateThread extends Thread {

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
            *//*if (ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(modelStatus)) {
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
            }*//*
            //更新订单
            List<OrderItemModel> orderItemModels=orderModel.getOrderItemModels();
            if(orderItemModels!=null && orderItemModels.size()>0) {
                for (OrderItemModel orderItemModel : orderItemModels) {
                    //判断该商品是否存在
                    ProductOrderItemEntity productOrderItemEntity = productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id", orderItemModel.getOrderItemId()));
                    //存在更新
                    ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku", orderItemModel.getProductSku()));
                    if (StringUtils.isNotBlank(orderItemModel.getProductImageUrl())) {
                        productOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                    } else if (productsEntity != null) {
                        productOrderItemEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                    }
                    //更新订单商品
//                            orderEntity.setProductTitle(orderModel.getTitlename());
//                            orderEntity.setCountryCode(orderModel.getCountry());
                    productOrderItemEntity.setProductTitle(orderItemModel.getProductTitle());
                    productOrderItemEntity.setProductSku(orderItemModel.getProductSku());
                    productOrderItemEntity.setProductAsin(orderItemModel.getProductAsin());
                    productOrderItemEntity.setProductPrice(orderItemModel.getProductPrice());
                    productOrderItemEntity.setUpdatetime(new Date());
                    productOrderItemService.updateById(productOrderItemEntity);
                }
            }
            *//*String orderItemId=orderModel.getOrderItemId();
            ProductOrderItemEntity productOrderItemEntity=productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id",orderItemId));

            ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku",productOrderItemEntity.getProductSku()));
            if(StringUtils.isNotBlank(productOrderItemEntity.getProductImageUrl())){
                orderEntity.setProductImageUrl(productOrderItemEntity.getProductImageUrl());
            }else if(productsEntity != null){
                orderEntity.setProductImageUrl(productsEntity.getMainImageUrl());
            }

            orderEntity.setProductTitle(orderModel.getTitlename());
*//*
            orderEntity.setCountryCode(orderModel.getCountry());
            //设置汇率
            BigDecimal rate = new BigDecimal(0.00);
            String rateCode = orderModel.getCurrencyCode();
            if(StringUtils.isNotBlank(rateCode)){
                rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code",rateCode)).getRate();
            }
            BigDecimal orderMoney = orderModel.getOrderMoney();
            if(orderMoney.compareTo(new BigDecimal("0.00")) != 0){
                orderEntity.setOrderMoney(orderMoney);
                orderEntity.setOrderMoneyCny(orderMoney.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
                //获取Amazon佣金（外币）
                BigDecimal amazonCommission = orderMoney.multiply(new BigDecimal(0.15).setScale(2,BigDecimal.ROUND_HALF_UP));
                orderEntity.setAmazonCommission(amazonCommission);
                orderEntity.setAmazonCommissionCny(amazonCommission.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
                //到账金额
                BigDecimal accountMoney = orderMoney.subtract(amazonCommission);
                orderEntity.setAccountMoney(accountMoney);
                orderEntity.setAccountMoneyCny(accountMoney.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
            }
            //获取状态判断是否为取消
            if(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(modelStatus)){
                orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED);
                orderEntity.setOrderState("取消");
            }else{
                String orderStatus = orderEntity.getOrderStatus();
                //获取当前订单状态判断是否为待付款、已付款、虚发货
                List amazonStateList = Arrays.asList(ConstantDictionary.OrderStateCode.AMAZON_ORDER_STATE);
                if(amazonStateList.contains(orderStatus)){
                    //获取返回状态判断是否为待付款、已付款、虚发货
                    if(amazonStateList.contains(modelStatus)){
                        //判断两个状态不想等时更改状态
                        if(!modelStatus.equals(orderStatus)){
                            orderEntity.setOrderStatus(modelStatus);
                            String orderState = dataDictionaryService.selectOne(
                                    new EntityWrapper<DataDictionaryEntity>()
                                            .eq("data_type","AMAZON_ORDER_STATE")
                                            .eq("data_number",modelStatus)
                            ).getDataContent();

                            orderEntity.setOrderState(orderState);
                            orderService.updateById(orderEntity);
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
            }
        }
    }*/

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




    /**
     * 刷新订单国际物流线程
     * 手动刷新订单时且状态不为亚马逊状态时调用
     */
  /*  class RefreshOrderThread extends Thread   {
        private Long orderId;
        public RefreshOrderThread(Long orderId) {
            this.orderId = orderId;
        }

        @Override
        public void run() {
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
                        orderService.amazonUpdateLogistics(sendDataMoedl,orderId);
                    }
                }
            }
        }
    }*/
}
