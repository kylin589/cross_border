package io.renren.modules.product.service.impl;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderRequest;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ResponseHeaderMetadata;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.amazon.util.FileUtil;
import io.renren.modules.logistics.DTO.*;
import io.renren.modules.logistics.entity.*;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.logistics.service.LogisticsChannelService;
import io.renren.modules.logistics.service.NewOrderAbroadLogisticsService;
import io.renren.modules.logistics.service.SubmitLogisticsService;
import io.renren.modules.logistics.util.AbroadLogisticsUtil;
import io.renren.modules.logistics.util.NewAbroadLogisticsUtil;
import io.renren.modules.logistics.util.XmlUtils;
import io.renren.modules.order.entity.NewProductShipAddressEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.NewProductShipAddressService;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.product.dao.NewOrderDao;
import io.renren.modules.product.entity.*;

import io.renren.modules.product.service.*;
import io.renren.modules.product.vm.OrderItemModel;
import io.renren.modules.product.vm.OrderModel;
import io.renren.modules.sys.dto.FranchiseeStatisticsDto;
import io.renren.modules.sys.dto.PlatformStatisticsDto;
import io.renren.modules.sys.dto.UserStatisticsDto;
import io.renren.modules.sys.entity.ConsumeEntity;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.ConsumeService;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.vm.StatisticsVM;
import io.renren.modules.util.DateUtils;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

import com.baomidou.mybatisplus.service.impl.ServiceImpl;



@Service("newOrderService")
public class NewOrderServiceImpl extends ServiceImpl<NewOrderDao, NewOrderEntity> implements NewOrderService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
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
    NewProductShipAddressService newProductShipAddressService;
    @Autowired
    NewOrderService newOrderService;
    @Autowired
    NewOrderItemService newOrderItemService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private LogisticsChannelService logisticsChannelService;
    @Autowired
    private ProductShipAddressService productShipAddressService;
    @Autowired
    private AmazonGrantService amazonGrantService;
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;
    @Autowired
    private SubmitLogisticsService submitLogisticsService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private AmazonRateService amazonRateService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ConsumeService consumeService;
    @Autowired
    private NewOrderAbroadLogisticsService newOrderAbroadLogisticsService;
    @Value(("${file.path}"))
    private String fileStoragePath;
    @Override
    public Map<String, Object> queryMyPage(Map<String, Object> params, Long userId) {
        //店铺名称
        String shopName = (String) params.get("shopName");
        //状态标识
        String orderStatus = (String) params.get("orderStatus");
        //异常状态标识
        String abnormalStatus = (String) params.get("abnormalStatus");
        //订单id
        String orderIdStr = (String) params.get("orderId");
        Long orderId = 0L;
        if(StringUtils.isNotBlank(orderIdStr)){
            orderId = Long.parseLong(orderIdStr);
        }
        //亚马逊订单id
        String amazonOrderId = (String) params.get("amazonOrderId");
        //产品id
        String productIdStr = (String)params.get("productId");
        Long productId = 0L;
        if(StringUtils.isNotBlank(productIdStr)){
            productId = Long.parseLong(productIdStr);
        }
        //产品sku
        String productSku = (String) params.get("productSku");
        //asin码
        String productAsin = (String) params.get("productAsin");
        //国内物流单号
        String domesticWaybill = (String) params.get("domesticWaybill");
        //国外物流单号
        String abroadWaybill = (String) params.get("abroadWaybill");
        //开始时间
        String startDate = (String) params.get("startDate");
        //结束时间
        String endDate = (String) params.get("endDate");
        //查询条件
        EntityWrapper<NewOrderEntity> wrapper = new EntityWrapper<NewOrderEntity>();
        wrapper.like(StringUtils.isNotBlank(shopName), "shop_name", shopName)
                .eq(StringUtils.isNotBlank(orderStatus), "order_status", orderStatus)
                .eq(StringUtils.isNotBlank(abnormalStatus), "abnormal_status", abnormalStatus)
                .eq(StringUtils.isNotBlank(orderIdStr), "order_id", orderId)
                .like(StringUtils.isNotBlank(amazonOrderId), "amazon_order_id", amazonOrderId)
                .eq(StringUtils.isNotBlank(productIdStr), "product_id", productId)
                .like(StringUtils.isNotBlank(productSku), "product_sku", productSku)
                .like(StringUtils.isNotBlank(productAsin), "product_asin", productAsin)
                .eq(StringUtils.isNotBlank(domesticWaybill), "domestic_waybill", domesticWaybill)
                .eq(StringUtils.isNotBlank(abroadWaybill), "abroad_waybill", abroadWaybill)
                .ge(StringUtils.isNotBlank(startDate), "buy_date", startDate)
                .le(StringUtils.isNotBlank(endDate), "buy_date", endDate)
                .eq("user_id",userId)
                .orderBy("buy_date",false);

        Page<NewOrderEntity> page = this.selectPage(
                new Query<NewOrderEntity>(params).getPage(),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        Map<String, Object> condition = new HashMap<>(2);
        condition.put("userId",userId);
        OrderStatisticsEntity orderCounts = baseMapper.statisticsOrderCounts(condition);

        if(orderCounts == null){
            orderCounts = new OrderStatisticsEntity();
        }
        //订单数
        int orderCount = this.selectCount(new EntityWrapper<NewOrderEntity>().eq("user_id",userId));
        orderCounts.setOrderCount(orderCount);
        //核算订单数
        int completeCounts = this.selectCount(new EntityWrapper<NewOrderEntity>().eq("user_id",userId).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
        orderCounts.setOrderCounts(completeCounts);
        //总金额
        String orderMoney = baseMapper.salesVolumeStatistics(condition);
        if(StringUtils.isNotBlank(orderMoney)){
            orderCounts.setOrderMoney(new BigDecimal(orderMoney));
        }
        //退货数
        int returnCounts = this.selectCount(new EntityWrapper<NewOrderEntity>().eq("abnormal_status",ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
        orderCounts.setReturnCounts(returnCounts);
        Map<String, Object> map = new HashMap<>(2);
        map.put("page",pageUtils);
        map.put("orderCounts",orderCounts);
        return map;
    }

    @Override
    public Map<String, Object> queryAllPage(Map<String, Object> params, Long deptId) {
        //公司
        String qDeptId = (String) params.get("deptId");
        //用户
        String userId = (String) params.get("userId");
        //店铺名称
        String shopName = (String) params.get("shopName");
        //状态标识
        String orderStatus = (String) params.get("orderStatus");
        //异常状态标识
        String abnormalStatus = (String) params.get("abnormalStatus");
        //订单id
        String orderIdStr = (String) params.get("orderId");
        Long orderId = 0L;
        if(StringUtils.isNotBlank(orderIdStr)){
            orderId = Long.parseLong(orderIdStr);
        }
        //亚马逊订单id
        String amazonOrderId = (String) params.get("amazonOrderId");
        //产品id
        String productIdStr = (String)params.get("productId");
        Long productId = 0L;
        if(StringUtils.isNotBlank(productIdStr)){
            productId = Long.parseLong(productIdStr);
        }
        //产品sku
        String productSku = (String) params.get("productSku");
        //asin码
        String productAsin = (String) params.get("productAsin");
        //国内物流单号
        String domesticWaybill = (String) params.get("domesticWaybill");
        //国外物流单号
        String abroadWaybill = (String) params.get("abroadWaybill");
        //开始时间
        String startDate = (String) params.get("startDate");
        //结束时间
        String endDate = (String) params.get("endDate");
        //查询条件
        EntityWrapper<NewOrderEntity> wrapper = new EntityWrapper<NewOrderEntity>();
        if(deptId == 1L){
            wrapper.eq(StringUtils.isNotBlank(shopName), "shop_name", shopName)
                    .eq(StringUtils.isNotBlank(orderStatus), "order_status", orderStatus)
                    .eq(StringUtils.isNotBlank(abnormalStatus), "abnormal_status", abnormalStatus)
                    .eq(StringUtils.isNotBlank(orderIdStr), "order_id", orderId)
                    .like(StringUtils.isNotBlank(amazonOrderId), "amazon_order_id", amazonOrderId)
                    .eq(StringUtils.isNotBlank(productIdStr), "product_id", productId)
                    .like(StringUtils.isNotBlank(productSku), "product_sku", productSku)
                    .like(StringUtils.isNotBlank(productAsin), "product_asin", productAsin)
                    .like(StringUtils.isNotBlank(domesticWaybill), "domestic_waybill", domesticWaybill)
                    .like(StringUtils.isNotBlank(abroadWaybill), "abroad_waybill", abroadWaybill)
                    .ge(StringUtils.isNotBlank(startDate), "buy_date", startDate)
                    .le(StringUtils.isNotBlank(endDate), "buy_date", endDate)
                    .eq(StringUtils.isNotBlank(userId),"user_id",userId)
                    .eq(StringUtils.isNotBlank(qDeptId),"dept_id",qDeptId)
                    .orderBy("buy_date",false);
        }else{
            wrapper.eq(StringUtils.isNotBlank(shopName), "shop_name", shopName)
                    .eq(StringUtils.isNotBlank(orderStatus), "order_status", orderStatus)
                    .eq(StringUtils.isNotBlank(orderIdStr), "order_id", orderId)
                    .like(StringUtils.isNotBlank(amazonOrderId), "amazon_order_id", amazonOrderId)
                    .eq(StringUtils.isNotBlank(productIdStr), "product_id", productId)
                    .like(StringUtils.isNotBlank(productSku), "product_sku", productSku)
                    .like(StringUtils.isNotBlank(productAsin), "product_asin", productAsin)
                    .like(StringUtils.isNotBlank(domesticWaybill), "domestic_waybill", domesticWaybill)
                    .like(StringUtils.isNotBlank(abroadWaybill), "abroad_waybill", abroadWaybill)
                    .ge(StringUtils.isNotBlank(startDate), "buy_date", startDate)
                    .le(StringUtils.isNotBlank(endDate), "buy_date", endDate)
                    .eq(StringUtils.isNotBlank(userId),"user_id",userId)
                    .eq("dept_id",deptId)
                    .orderBy("buy_date",false);
        }


        Page<NewOrderEntity> page = this.selectPage(
                new Query<NewOrderEntity>(params).getPage(),
                wrapper
        );
        List<NewOrderEntity> orderEntityList = page.getRecords();
        for(NewOrderEntity NewOrderEntity : orderEntityList){
            SysUserEntity sysUserEntity = userService.selectById(NewOrderEntity.getUserId());
            SysDeptEntity sysDeptEntity = deptService.selectById(NewOrderEntity.getDeptId());
            if(sysDeptEntity != null){
                NewOrderEntity.setDeptName(sysDeptEntity.getName());
            }
            if(sysUserEntity != null){
                NewOrderEntity.setUserName(sysUserEntity.getDisplayName());
            }
        }
        PageUtils pageUtils = new PageUtils(page);
        OrderStatisticsEntity orderCounts = new OrderStatisticsEntity();
        Map<String, Object> condition = new HashMap<>(1);
        if(deptId == 1L){
            //总公司
            if(baseMapper.statisticsOrderCounts(condition) != null){
                orderCounts = baseMapper.statisticsOrderCounts(condition);
            }
            //订单数
            int orderCount = this.selectCount(new EntityWrapper<NewOrderEntity>());
            orderCounts.setOrderCount(orderCount);
            //核算订单数
            int completeCounts = this.selectCount(new EntityWrapper<NewOrderEntity>().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH));
            orderCounts.setOrderCounts(completeCounts);
            //总金额
            String orderMoney = baseMapper.salesVolumeStatistics(condition);
            if(StringUtils.isNotBlank(orderMoney)){
                orderCounts.setOrderMoney(new BigDecimal(orderMoney));
            }
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<NewOrderEntity>().eq("abnormal_status",ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
            orderCounts.setReturnCounts(returnCounts);
        }else{
            //加盟商
            condition.put("deptId",deptId);
            if(baseMapper.statisticsOrderCounts(condition) != null){
                orderCounts = baseMapper.statisticsOrderCounts(condition);
            }
            //订单数
            int orderCount = this.selectCount(new EntityWrapper<NewOrderEntity>().eq("dept_id",deptId));
            orderCounts.setOrderCount(orderCount);
            //核算订单数
            int completeCounts = this.selectCount(new EntityWrapper<NewOrderEntity>().eq("dept_id",deptId).eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH));
            orderCounts.setOrderCounts(completeCounts);
            //总金额
            String orderMoney = baseMapper.salesVolumeStatistics(condition);
            if(StringUtils.isNotBlank(orderMoney)){
                orderCounts.setOrderMoney(new BigDecimal(orderMoney));
            }
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<NewOrderEntity>().eq("dept_id",deptId).eq("abnormal_status",ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
            orderCounts.setReturnCounts(returnCounts);
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("page",pageUtils);
        map.put("orderCounts",orderCounts);
        return map;
    }

    @Override
    public PageUtils queryNewAllPage(Map<String, Object> params){
        //国内物流单号
        String domesticWaybill = (String) params.get("domesticWaybill");
        //国外物流单号
        String abroadWaybill = (String) params.get("abroadWaybill");
        //亚马逊单号
        String amazonOrderId = (String)params.get("amazonOrderId");
        //订单id
        String orderIdStr = (String) params.get("orderId");
        //状态标识
        String orderStatus = (String) params.get("orderStatus");
        //异常状态标识
        String abnormalStatus = (String) params.get("abnormalStatus");

        Long orderId = 0L;
        if(StringUtils.isNotBlank(orderIdStr)){
            orderId = Long.parseLong(orderIdStr);
        }
        //查询条件
        EntityWrapper<NewOrderEntity> wrapper = new EntityWrapper<NewOrderEntity>();
        wrapper.eq(StringUtils.isNotBlank(orderIdStr), "order_id", orderId)
                .like(StringUtils.isNotBlank(amazonOrderId), "amazon_order_id", amazonOrderId)
                .like(StringUtils.isNotBlank(domesticWaybill), "domestic_waybill", domesticWaybill)
                .like(StringUtils.isNotBlank(abroadWaybill), "abroad_waybill", abroadWaybill)
                .eq(StringUtils.isNotBlank(orderStatus), "order_status", orderStatus)
                .eq(StringUtils.isNotBlank(abnormalStatus), "abnormal_status", abnormalStatus);
        return new PageUtils(this.selectPage(
                new Query<NewOrderEntity>(params).getPage(),
                wrapper
        ));
    }
    @Override
    //修改状态
    public boolean updateState(Long orderId, String orderState) {
        String orderStatus = dataDictionaryService.selectOne(
                new EntityWrapper<DataDictionaryEntity>()
                        .eq("data_type","NEW_AMAZON_ORDER_STATE")
                        .eq("data_content",orderState)
        ).getDataNumber();

        int flag = baseMapper.updateState(orderId,orderState,orderStatus);
        if(flag != 0){
            return true;
        }
        return false;
    }
    @Override
    public Map<String,String> pushOrder(String amazonOrderId, int packageType, String channelCode, String chineseName, String englishName, int length, int width, int height, BigDecimal weight){
        NewOrderEntity neworderEntity = this.selectOne(new EntityWrapper<NewOrderEntity>().eq("amazon_order_id", amazonOrderId));
        ProductShipAddressEntity shipAddressEntity = productShipAddressService.selectOne(new EntityWrapper<ProductShipAddressEntity>().eq("amazon_order_id", amazonOrderId));
        //推送--订单基本信息
        OrderRequestData omsOrder = new OrderRequestData();
        omsOrder.setOrderNumber(amazonOrderId);
        omsOrder.setShippingMethodCode(channelCode);//测试用的
        omsOrder.setPackageNumber(neworderEntity.getOrderNumber());
        omsOrder.setWeight(weight);
        //推送--订单详情
        List<ApplicationInfos> omsOrderDetails = new ArrayList<>();
        List<NewOrderItemEntity> productOrderItemEntitys=newOrderItemService.selectList(new EntityWrapper<NewOrderItemEntity>().eq("amazon_order_id",neworderEntity.getAmazonOrderId()));
        for(NewOrderItemEntity productOrderItemEntity:productOrderItemEntitys){
            ApplicationInfos omsOrderDetail=new ApplicationInfos();
            omsOrderDetail.setApplicationName(productOrderItemEntity.getProductTitle());
            omsOrderDetail.setQty(1);
            BigDecimal UnitPrice=new BigDecimal(1);//测试用
            omsOrderDetail.setUnitPrice(UnitPrice);
            BigDecimal unitweight=new BigDecimal(1);//测试用
            omsOrderDetail.setUnitWeight(unitweight);
            NewOrderEntity newOrderEntity=this.selectOne(new EntityWrapper<NewOrderEntity>().eq("amazon_order_id",productOrderItemEntity.getAmazonOrderId()));
            omsOrderDetail.setProductUrl(productOrderItemEntity.getProductImageUrl()==null ? newOrderEntity.getProductImageUrl():productOrderItemEntity.getProductImageUrl());
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
        shippingInfo.setShippingPhone(shipAddressEntity.getShipTel()==null?NewAbroadLogisticsUtil.getTel():shipAddressEntity.getShipTel());
        shippingInfo.setShippingZip(shipAddressEntity.getShipZip());
        shippingInfo.setShippingPhone(shipAddressEntity.getShipTel());
        ApplicationInfos[] applicationInfos = new ApplicationInfos[omsOrderDetails.size()];
        omsOrder.setShippingInfo(shippingInfo);
        omsOrder.setApplicationInfos(omsOrderDetails.toArray(applicationInfos));
        SenderInfo senderInfo = new SenderInfo();//默认空值，不是必填参数
        omsOrder.setSenderInfo(senderInfo);
        JSONArray omsOrderJson = JSONArray.fromObject(omsOrder);
        Map<String,String> result = NewAbroadLogisticsUtil.pushOrder(omsOrderJson.toString());
        return result;
    }

    @Override
    public boolean updateAbnormalState(Long[] orderIds, String abnormalStatus, String abnormalState) {
        int flag = 0;
        //如果是normal，则设为null
        if("Normal".equals(abnormalStatus)){
            flag = baseMapper.updateAbnormalState(orderIds,null,null);
        }else{
            //如果是退货，则修改费用信息
            if(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_RETURN.equals(abnormalStatus)){
                List<NewOrderEntity> orderList = new ArrayList<>();
                for(int i = 0; i < orderIds.length; i++){
                    NewOrderEntity neworderEntity = this.selectById(orderIds[i]);
                    //订单当时税率
                    BigDecimal momentRate = neworderEntity.getMomentRate();
                    //订单金额
                    BigDecimal orderMoneyForeign = neworderEntity.getOrderMoney();
                    //获取Amazon佣金（外币）
                    BigDecimal amazonCommissionForeign = orderMoneyForeign.multiply(new BigDecimal(0.03)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    neworderEntity.setAmazonCommission(amazonCommissionForeign);
                    //到账金额
                    BigDecimal accountMoneyForeign = new BigDecimal(0.00).subtract(amazonCommissionForeign);
                    neworderEntity.setAccountMoney(accountMoneyForeign);
                    //国际运费
                    BigDecimal interFreight = neworderEntity.getInterFreight();
                    if(interFreight.compareTo(new BigDecimal(0.0)) == 0){
                        //没产生运费
                        //利润 = 到账金额(人民币)
                        BigDecimal orderProfit = accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP);
                        neworderEntity.setOrderProfit(orderProfit);
                        //利润率
                        BigDecimal profitRate = orderProfit.divide(neworderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
                        neworderEntity.setProfitRate(profitRate);
                    }else{
                        //产生运费
                        //平台佣金点数
                        BigDecimal companyPoint = deptService.selectById(neworderEntity.getDeptId()).getCompanyPoints();
                        //平台服务费 = （订单金额-Amazon佣金）* 汇率 * 平台佣金点数
                        BigDecimal a = orderMoneyForeign.subtract(amazonCommissionForeign);
                        BigDecimal b = a.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP);
                        BigDecimal platformCommissions = b.multiply(companyPoint).setScale(2,BigDecimal.ROUND_HALF_UP);
                        neworderEntity.setPlatformCommissions(platformCommissions);
                        //利润
                        //到账金额——>人民币
                        BigDecimal c = accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP);
                        BigDecimal orderProfit = c.subtract(interFreight).subtract(platformCommissions).setScale(2,BigDecimal.ROUND_HALF_UP);
                        neworderEntity.setOrderProfit(orderProfit);
                        //利润率
                        BigDecimal profitRate = orderProfit.divide(neworderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
                        neworderEntity.setProfitRate(profitRate);
                        //扣款
                        deduction(neworderEntity);
                    }
                    orderList.add(neworderEntity);
                }
                this.updateBatchById(orderList);
            }
            flag = baseMapper.updateAbnormalState(orderIds,abnormalState,abnormalStatus);
        }
        if(flag != 0){
            return true;
        }
        return false;
    }

    @Override
    public void internationalShipments(NewOrderEntity neworderEntity) {

    }

    @Override
    public void deduction(NewOrderEntity neworderEntity) {
        //扣款
        SysDeptEntity dept = deptService.selectById(neworderEntity.getDeptId());
        //原来余额
        BigDecimal oldBalance = dept.getBalance();
        BigDecimal nowBalance = oldBalance.subtract(neworderEntity.getInterFreight()).subtract(neworderEntity.getPlatformCommissions());
        dept.setBalance(nowBalance);
        deptService.updateById(dept);
        //生成运费记录
        ConsumeEntity consumeEntity1 = new ConsumeEntity();
        consumeEntity1.setAmazonOrderId(neworderEntity.getAmazonOrderId());
        consumeEntity1.setDeptId(neworderEntity.getDeptId());
        consumeEntity1.setDeptName(deptService.selectById(neworderEntity.getDeptId()).getName());
        consumeEntity1.setUserId(neworderEntity.getUserId());
        consumeEntity1.setUserName(userService.selectById(neworderEntity.getUserId()).getDisplayName());
        consumeEntity1.setType("物流费");
        consumeEntity1.setOrderId(neworderEntity.getOrderId());
        consumeEntity1.setMoney(neworderEntity.getInterFreight());
        consumeEntity1.setBeforeBalance(oldBalance);
        consumeEntity1.setAfterBalance(oldBalance.subtract(neworderEntity.getInterFreight()));
        consumeEntity1.setAbroadWaybill(neworderEntity.getAbroadWaybill());
        consumeEntity1.setCreateTime(new Date());
        consumeService.insert(consumeEntity1);
        //生成服务费记录
        ConsumeEntity consumeEntity2 = new ConsumeEntity();
        consumeEntity2.setAmazonOrderId(neworderEntity.getAmazonOrderId());
        consumeEntity2.setDeptId(neworderEntity.getDeptId());
        consumeEntity2.setDeptName(deptService.selectById(neworderEntity.getDeptId()).getName());
        consumeEntity2.setUserId(neworderEntity.getUserId());
        consumeEntity2.setUserName(userService.selectById(neworderEntity.getUserId()).getDisplayName());
        consumeEntity2.setType("服务费");
        consumeEntity2.setOrderId(neworderEntity.getOrderId());
        consumeEntity2.setMoney(neworderEntity.getPlatformCommissions());
        consumeEntity2.setBeforeBalance(oldBalance.subtract(neworderEntity.getInterFreight()));
        consumeEntity2.setAfterBalance(nowBalance);
        consumeEntity2.setCreateTime(new Date());
        consumeService.insert(consumeEntity2);
    }

    /*@Override
    public UserStatisticsDto oneLevelUserStatistics(StatisticsVM vm) {
        UserStatisticsDto dto = new UserStatisticsDto();
        Map<String, Object> params = new HashMap<String,Object>();
        String type = vm.getType();
        String startDate = null;
        String endDate = null;
        if("year".equals(type)){
            //查询年
            startDate = DateUtils.getYearFirst();
            endDate = DateUtils.getYearEnd();
        }else if("month".equals(type)){
            //查询月
            startDate = DateUtils.getMonthFirst();
            endDate = DateUtils.getMonthEnd();
        }else if("day".equals(type)){
            //查询日
            startDate = DateUtils.getDayFirst();
            endDate = DateUtils.getDayEnd();
        }else{
            //按时间查询
            startDate = DateUtils.startFormat.format(vm.getStartDate());
            endDate = DateUtils.endFormat.format(vm.getEndDate());
        }
        if(StringUtils.isNotBlank(startDate)){
            params.put("startDate", startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
            params.put("endDate", endDate);
        }
        if (StringUtils.isNotBlank(vm.getDeptId())){
            params.put("deptId",vm.getDeptId());
        }
        if(StringUtils.isNotBlank(vm.getUserId())){
            params.put("userIdId",vm.getUserId());
        }
        EntityWrapper orderWrapper = new EntityWrapper<NewOrderEntity>();
        orderWrapper.ge(StringUtils.isNotBlank(startDate), "buy_date", startDate)
                .le(StringUtils.isNotBlank(endDate), "buy_date", endDate)
                .eq(StringUtils.isNotBlank(vm.getDeptId()),"dept_id",vm.getDeptId())
                .eq(StringUtils.isNotBlank(vm.getUserId()),"user_id",vm.getUserId());
        EntityWrapper productWrapper = new EntityWrapper<ProductsEntity>();
        productWrapper.ge(StringUtils.isNotBlank(startDate), "create_time", startDate)
                .le(StringUtils.isNotBlank(endDate), "create_time", endDate)
                .eq(StringUtils.isNotBlank(vm.getDeptId()),"dept_id",vm.getDeptId())
                .eq(StringUtils.isNotBlank(vm.getUserId()),"create_user_id",vm.getUserId());
        if(baseMapper.userStatistics(params) != null){
            dto = baseMapper.userStatistics(params);
        }

        dto.setAddProductsCounts(productsService.selectCount(productWrapper));
        if(baseMapper.salesVolumeStatistics(params) != null){
            dto.setSalesVolume(new BigDecimal(baseMapper.salesVolumeStatistics(params)));
        }
        dto.setAddOrderCounts(this.selectCount(orderWrapper));
        dto.setReturnCounts(this.selectCount(orderWrapper));
        dto.setAbnormalCounts(this.selectCount(orderWrapper.isNotNull("abnormal_status")));
        if(dto.getSalesVolume().compareTo(new BigDecimal(0.00)) != 0){
            dto.setProfitRate(dto.getProfit().divide(dto.getSalesVolume(),2,BigDecimal.ROUND_HALF_UP));
        }
        return dto;
    }*/

    /*@Override
    public FranchiseeStatisticsDto oneLevelFranchiseeStatistics(StatisticsVM vm) {
        FranchiseeStatisticsDto dto = new FranchiseeStatisticsDto();
        Map<String, Object> params = new HashMap<String,Object>();
        String type = vm.getType();
        String startDate = null;
        String endDate = null;
        if("year".equals(type)){
            //查询年
            startDate = DateUtils.getYearFirst();
            endDate = DateUtils.getYearEnd();
        }else if("month".equals(type)){
            //查询月
            startDate = DateUtils.getMonthFirst();
            endDate = DateUtils.getMonthEnd();
        }else if("day".equals(type)){
            //查询日
            startDate = DateUtils.getDayFirst();
            endDate = DateUtils.getDayEnd();
        }else{
            //按时间查询
            startDate = DateUtils.startFormat.format(vm.getStartDate());
            endDate = DateUtils.endFormat.format(vm.getEndDate());
        }
        if(StringUtils.isNotBlank(startDate)){
            params.put("startDate", startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
            params.put("endDate", endDate);
        }
        if (StringUtils.isNotBlank(vm.getDeptId())){
            params.put("deptId",vm.getDeptId());
        }
        if(StringUtils.isNotBlank(vm.getUserId())){
            params.put("userIdId",vm.getUserId());
        }
        EntityWrapper orderWrapper = new EntityWrapper<NewOrderEntity>();
        orderWrapper.ge(StringUtils.isNotBlank(startDate), "buy_date", startDate)
                .le(StringUtils.isNotBlank(endDate), "buy_date", endDate)
                .eq(StringUtils.isNotBlank(vm.getDeptId()),"dept_id",vm.getDeptId())
                .eq(StringUtils.isNotBlank(vm.getUserId()),"user_id",vm.getUserId());
        if(baseMapper.franchiseeStatistics(params) != null){
            dto = baseMapper.franchiseeStatistics(params);
        };
        if(baseMapper.salesVolumeStatistics(params) != null){
            dto.setSalesVolume(new BigDecimal(baseMapper.salesVolumeStatistics(params)));
        }
        dto.setAddOrderCounts(this.selectCount(orderWrapper));
        dto.setAllCost(dto.getCost().add(dto.getOrderFreight().add(dto.getServicePrice())));
        if(dto.getSalesVolume().compareTo(new BigDecimal(0.00)) != 0){
            dto.setProfitRate(dto.getProfit().divide(dto.getSalesVolume(),2,BigDecimal.ROUND_HALF_UP));
        }
        return dto;
    }*/

    /*@Override
    public PlatformStatisticsDto platformStatistics(StatisticsVM vm) {
        Map<String, Object> params = new HashMap<String,Object>();
        String type = vm.getType();
        String startDate = null;
        String endDate = null;
        if("year".equals(type)){
            //查询年
            startDate = DateUtils.getYearFirst();
            endDate = DateUtils.getYearEnd();
        }else if("month".equals(type)){
            //查询月
            startDate = DateUtils.getMonthFirst();
            endDate = DateUtils.getMonthEnd();
        }else if("day".equals(type)){
            //查询日
            startDate = DateUtils.getDayFirst();
            endDate = DateUtils.getDayEnd();
        }else{
            //按时间查询
            startDate = DateUtils.startFormat.format(vm.getStartDate());
            endDate = DateUtils.endFormat.format(vm.getEndDate());
        }
        if(StringUtils.isNotBlank(startDate)){
            params.put("startDate", startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
            params.put("endDate", endDate);
        }
        PlatformStatisticsDto platformStatisticsDto = new PlatformStatisticsDto();
        BigDecimal choucheng = platformStatisticsDto.getChoucheng();
        if(baseMapper.chouchengStatistics(params) != null){
            choucheng = new BigDecimal(baseMapper.chouchengStatistics(params));
        }

        params.put("dept_id",1);
        BigDecimal userProfit = platformStatisticsDto.getUserProfit();
        if(baseMapper.hqUserProfit(params) != null){
            userProfit = new BigDecimal(baseMapper.hqUserProfit(params));
        }
        BigDecimal allProfit = choucheng.add(userProfit);
        platformStatisticsDto.setChoucheng(choucheng);
        platformStatisticsDto.setUserProfit(userProfit);
        platformStatisticsDto.setAllProfit(allProfit);
        return platformStatisticsDto;
    }*/

    @Override
    public void amazonUpdateLogistics(SendDataMoedl sendDataMoedl, Long orderId) {
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
            NewOrderAbroadLogisticsEntity newabroadLogisticsEntity = newOrderAbroadLogisticsService.selectOne(new EntityWrapper<NewOrderAbroadLogisticsEntity>().eq("order_id",orderId));
            String amazonOrderId=newOrderService.selectById(orderId).getAmazonOrderId();
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
                newabroadLogisticsEntity.setIsSynchronization(1);//表示同步成功
                newOrderAbroadLogisticsService.updateById(newabroadLogisticsEntity);
            }else{
                logger.error("同步失败,请重新上传订单...");
                newabroadLogisticsEntity.setIsSynchronization(0);//表示同步失败
                newOrderAbroadLogisticsService.updateById(newabroadLogisticsEntity);
            }

        }
    }

    @Override
    public Map<String,String> DeleteOrder(String type,String wayBillNumber) {
        String  jsonStr="{\"Type\":"+"\""+type+"\",\"OrderNumber\":"+wayBillNumber+"\"}";
        return NewAbroadLogisticsUtil.delOrder(jsonStr);

    }

    @Override
    public void printOrder(String orderNumber) {
        StringJoiner sj=new StringJoiner("","[\\\"","\\\"]");
        sj.add(orderNumber);
        NewAbroadLogisticsUtil.printOrder(sj.toString());

    }

    @Override
    public Map<String,String>  getShippingFeeDetail(String orderNumber) {
       return NewAbroadLogisticsUtil.getShippingFeeDetail("","",orderNumber);
    }

    @Override
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

    /**
     * 真实发货信息 ——封装物流信息
     * 后置：上传数据到亚马逊
     * @param
     * @param
     */
    private SendDataMoedl synchronizationZhenModel(NewOrderEntity neworderEntity, NewOrderAbroadLogisticsEntity newabroadLogisticsEntity){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String amazonOrderId = neworderEntity.getAmazonOrderId();
        String trackWaybill = newabroadLogisticsEntity.getTrackWaybill();
        //获取系统当前日期北京时间
        Date date = new Date();
        //北京时间减去8小时
        date= io.renren.common.utils.DateUtils.addDateHours(date,-8);
        //再把当前日期变成格林时间带T的.
        String shipDate=simpleDateFormat.format(date);
        Shipping u1 = new Shipping();
        u1.setMessageType("OrderFulfillment");
        Header header=new Header();
        header.setDocumentVersion("1.01");
        header.setMerchantIdentifier("MYID");//<MerchantIdentifier>此选项可以随便填写，，
        u1.setHeader(header);
        List<io.renren.modules.logistics.entity.Message> messages=new ArrayList<>();
        int count=1;
        List<NewOrderItemEntity> productOrderItemEntities=newOrderItemService.selectList(new EntityWrapper<NewOrderItemEntity>().eq("amazon_order_id",amazonOrderId));
        for (NewOrderItemEntity productOrderItemEntity:productOrderItemEntities) {
            io.renren.modules.logistics.entity.Message message=new Message();//如果要确认多个订单可以增加多个<message>
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
        AmazonGrantShopEntity shopEntity = amazonGrantShopService.selectById(neworderEntity.getShopId());
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
     * 获取单个订单的方法
     * @param client
     * @param requestList
     * @return
     */
    public  List<Object> invokeGetOrder(MarketplaceWebServiceOrdersAsync client, List<GetOrderRequest> requestList) {
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
