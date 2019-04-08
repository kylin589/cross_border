package io.renren.modules.product.service.impl;


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
import io.renren.modules.logistics.DTO.ApplicationInfos;
import io.renren.modules.logistics.DTO.OrderRequestData;
import io.renren.modules.logistics.DTO.SenderInfo;
import io.renren.modules.logistics.DTO.ShippingInfo;
import io.renren.modules.logistics.entity.*;
import io.renren.modules.logistics.service.*;
import io.renren.modules.logistics.util.NewAbroadLogisticsSFCUtil;
import io.renren.modules.logistics.util.NewAbroadLogisticsUtil;
import io.renren.modules.logistics.util.shiprate.AddOrderRequest;
import io.renren.modules.logistics.util.shiprate.AddOrderRequestInfoArray;
import io.renren.modules.logistics.util.shiprate.GoodsDetailsArray;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.NewProductShipAddressService;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.product.dao.NewOrderDao;
import io.renren.modules.product.entity.*;

import io.renren.modules.product.service.*;

import io.renren.modules.sys.entity.ConsumeEntity;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.ConsumeService;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysUserService;

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

import java.text.SimpleDateFormat;
import java.util.*;


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
    @Autowired
    private NewOrderItemRelationshipService newOrderItemRelationshipService;
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
        return pageUtils;
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
    public Map<String,String> pushOrder(String orderNumber,String amazonOrderId, int packageType, String channelCode, String channelName, String englishName, int length, int width, int height, BigDecimal weight){
        NewOrderEntity neworderEntity = this.selectOne(new EntityWrapper<NewOrderEntity>().eq("amazon_order_id", amazonOrderId));
        ProductShipAddressEntity shipAddressEntity = productShipAddressService.selectOne(new EntityWrapper<ProductShipAddressEntity>().eq("amazon_order_id", amazonOrderId));
        //推送--订单基本信息
        OrderRequestData omsOrder = new OrderRequestData();
        omsOrder.setOrderNumber(orderNumber);//推送订单号
        omsOrder.setShippingMethodCode(channelCode);//测试用的
        omsOrder.setPackageNumber(neworderEntity.getOrderNumber());//订单表中的订单数量
        weight=weight.setScale(0,BigDecimal.ROUND_UP);
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
        Map<String,String> map =new HashMap<>();
        String message="电话号码必填，不能为空";
        map.put("msg",message);
        map.put("code","false");
        if(shipAddressEntity.getShipTel()!=null){
        shippingInfo.setShippingPhone(shipAddressEntity.getShipTel());
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
        }else{
            return map;
        }

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
    public void deductionFW(NewOrderEntity neworderEntity) {
        List<ConsumeEntity> consumeList = consumeService.selectList(new EntityWrapper<ConsumeEntity>().eq("order_id",neworderEntity.getOrderId()).eq("type","服务费"));
        if(consumeList == null || consumeList.size() == 0){
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
            consumeEntity2.setCreateTime(new Date());
            consumeService.insert(consumeEntity2);
        }
    }

    @Override
    public void deductionYF(NewOrderAbroadLogisticsEntity newOrderAbroadLogisticsEntity) {
        List<ConsumeEntity> consumeList = consumeService.selectList(new EntityWrapper<ConsumeEntity>().eq("order_id",newOrderAbroadLogisticsEntity.getOrderId()).eq("type","物流费").eq("abroad_waybill",newOrderAbroadLogisticsEntity.getAbroadWaybill()));
        if(consumeList == null || consumeList.size() == 0){
            NewOrderEntity neworderEntity = newOrderService.selectById(newOrderAbroadLogisticsEntity.getOrderId());
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
            consumeEntity1.setAbroadWaybill(neworderEntity.getAbroadWaybill());
            consumeEntity1.setCreateTime(new Date());
            consumeService.insert(consumeEntity1);
        }
    }
    @Override
    public void serviceFee(NewOrderEntity newOrderEntity) {
        if(newOrderEntity.getPlatformCommissions() == null || newOrderEntity.getPlatformCommissions().compareTo(new BigDecimal(0)) == 0){
            //到账金额(人民币)
            BigDecimal accountMoney = newOrderEntity.getAccountMoneyCny();
            //平台佣金
            BigDecimal companyPoint = deptService.selectById(newOrderEntity.getDeptId()).getCompanyPoints();
            BigDecimal platformCommissions = accountMoney.multiply(companyPoint).setScale(2,BigDecimal.ROUND_HALF_UP);
            newOrderEntity.setPlatformCommissions(platformCommissions);
            newOrderService.updateById(newOrderEntity);
            deductionFW(newOrderEntity);
        }
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

  /*  @Override
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
        *//**
         * 根据List数组，生成XML数据
         *//*
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
        //进行数据上传
           String feedSubmissionId = submitLogisticsService.submitFeed(serviceURL.get(0),sellerId,mwsAuthToken,feedType,filePath,accessKey,secretKey);
            NewOrderAbroadLogisticsEntity newabroadLogisticsEntity = newOrderAbroadLogisticsService.selectOne(new EntityWrapper<NewOrderAbroadLogisticsEntity>().eq("order_id",orderId));
            //判读亚马逊后台订单的状态
            if(StringUtils.isNotBlank(feedSubmissionId)){
                newabroadLogisticsEntity.setIsSynchronization(1);//表示同步成功
                newOrderAbroadLogisticsService.updateById(newabroadLogisticsEntity);
            }else{
                logger.error("同步失败,请重新上传订单...");
                newabroadLogisticsEntity.setIsSynchronization(0);//表示同步失败
                newOrderAbroadLogisticsService.updateById(newabroadLogisticsEntity);
            }
    }*/

    @Override
    public Map<String,String> DeleteOrder(String type,String wayBillNumber) {
        String  jsonStr="{\"Type\":"+"\""+type+"\",\"OrderNumber\":"+"\""+wayBillNumber+"\"}";
        System.out.println("jsonStr:" + jsonStr);
        return NewAbroadLogisticsUtil.delOrder(jsonStr);

    }

    @Override
    public Map<String, String> printOrder(String orderNumber) {
        StringJoiner sj=new StringJoiner("\\\",\"","[\\\"","\\\"]");
        sj.add(orderNumber);
        return NewAbroadLogisticsUtil.printOrder(sj.toString().replace("\\",""));
    }

    @Override
    public Map<String,String>  getShippingFeeDetail(String orderNumber) {
       return NewAbroadLogisticsUtil.getShippingFeeDetail(orderNumber);
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
     * 三态推送接口
     * @param customerOrderNo
     * @param amazonOrderId
     * @param shipperAddressType
     * @param shippingMethod
     * @return
     */
    @Override
    public Map<String, String> pushOrder(String customerOrderNo,String amazonOrderId,int shipperAddressType, String shippingMethod,String itemCode,String chineseName,String englishName,List<NewOrderItemRelationshipEntity> list) {
        NewOrderEntity neworderEntity = this.selectOne(new EntityWrapper<NewOrderEntity>().eq("amazon_order_id", amazonOrderId));
        ProductShipAddressEntity shipAddressEntity = productShipAddressService.selectOne(new EntityWrapper<ProductShipAddressEntity>().eq("amazon_order_id", amazonOrderId));
        //推送--订单基本信息
        AddOrderRequestInfoArray addOrderRequestInfo = new AddOrderRequestInfoArray();
        java.util.List<GoodsDetailsArray> _goodsDetailsArray = addOrderRequestInfo
                .getGoodsDetails();
        addOrderRequestInfo.setCustomerOrderNo(customerOrderNo);// 订单编号
        addOrderRequestInfo.setShipperAddressType(shipperAddressType);//发货地址类型：1，默认；2，用户传送的地址信息
        addOrderRequestInfo.setShippingMethod(shippingMethod);//货运方式(TPRQM)
        addOrderRequestInfo.setShipperName("创淘跨境");//发件人姓名zhangsan
        addOrderRequestInfo.setRecipientName(shipAddressEntity.getShipName());//收件人姓名test
        addOrderRequestInfo.setRecipientCountry(shipAddressEntity.getShipCountry());//收件人国家Canada
        addOrderRequestInfo.setRecipientCity(shipAddressEntity.getShipCity());//收件人城市GR
        addOrderRequestInfo.setRecipientState(shipAddressEntity.getShipRegion());//收件人州或省份OP
        addOrderRequestInfo.setRecipientPhone(shipAddressEntity.getShipTel());//收件人电话（length:1-16）3242342342423
        addOrderRequestInfo.setRecipientZipCode(shipAddressEntity.getShipZip());//收件人邮编（length:1-10）A0N 2L0
        addOrderRequestInfo.setRecipientEmail("test@google.com");//收件人电子邮件
        addOrderRequestInfo.setOrderStatus("sumbmitted");//订单状态：提交订单，confirmed；订单预提交状态，preprocess；提交且交寄订单，sumbmitted；删除订单，delete,默认交寄状态
        addOrderRequestInfo.setRecipientAddress(shipAddressEntity.getShipAddressDetail());//收件人详细地址（length:5-70）sdfsdf sdafsf
        //每个国家的汇率
        BigDecimal countryRate=neworderEntity.getMomentRate();
        BigDecimal usdRate=new BigDecimal(6.71);
        addOrderRequestInfo.setGoodsDescription("sdfsda dsf ");//包裹内物品描述（length:1-100）sdfsda dsf
//        addOrderRequestInfo.setShippingWorth((float) 2.0);//销售运费（适用DEAM1,DERAM1）（不必须）(float) 2.0
        addOrderRequestInfo.setPieceNumber("1");//每票的件数，只有这些方式HKDHL,HKDHL1,CNUPS,SZUPS,HKUPS,SGDHL,EUTLP,CNFEDEX,HKFEDEX,CNSFEDEX,HKSFEDEX,EUEXP3必填
//        addOrderRequestInfo.setTaxesNumber("125698");//税号，6-12位数字125698
        addOrderRequestInfo.setIsRemoteConfirm("0");//是否同意收偏远费0不同意，1同意
        //推送--订单详情
        BigDecimal totalPrice=new BigDecimal(0.00);
        BigDecimal totolNum=new BigDecimal(0);
       /* List<NewOrderItemEntity> productOrderItemEntitys=newOrderItemService.selectList(new EntityWrapper<NewOrderItemEntity>().eq("amazon_order_id",amazonOrderId));
        for(NewOrderItemEntity productOrderItemEntity:productOrderItemEntitys){
            GoodsDetailsArray _goodsDetails = new GoodsDetailsArray();
            _goodsDetails.setDetailDescription("ghgdjhgj");//详细物品描述（length:1-140）ghgdjhgj
            _goodsDetails.setDetailDescriptionCN("商品中文sss名");//详细物品中文描述（必须包含中文字符，length:1-140）商品中文sss名
            _goodsDetails.setDetailCustomLabel(productOrderItemEntity.getProductSku());//详细物品客户自定义标签（length:1-20，不必须）SKU NAME
            _goodsDetails.setDetailQuantity(productOrderItemEntity.getOrderItemNumber().toString());//货品总数量"2"
            BigDecimal orderNum=new BigDecimal(productOrderItemEntity.getOrderItemNumber());
            _goodsDetails.setDetailWorth((productOrderItemEntity.getProductPrice().multiply(countryRate).divide(usdRate).setScale(2,BigDecimal.ROUND_HALF_UP)).toString());//货品中每个货物的价格（单位美元USD）"2"
            _goodsDetails.setHsCode(itemCode);//商品编码,海关编码（length:1-20）"15633569"////hs code1只能输入8或10位数字
            _goodsDetails.setEnMaterial("dsdasd");//物品英文材质（length:0-50）"dsdasd"
            _goodsDetails.setCnMaterial("daaaf");//物品中文材质（0-50）"daaaf"
            _goodsDetailsArray.add(_goodsDetails);
            totalPrice=(productOrderItemEntity.getProductPrice().multiply(orderNum).multiply(countryRate)).divide(usdRate);
            totalPrice=totalPrice.add(totalPrice);
            totolNum+=productOrderItemEntity.getOrderItemNumber();
        }*/
//        List<NewOrderItemRelationshipEntity> newOrderItemRelationshipEntities=newOrderItemRelationshipService.selectList(new EntityWrapper<NewOrderItemRelationshipEntity>().eq("relationship_id",relationShipId));
        for(NewOrderItemRelationshipEntity newOrderItemRelationshipEntity:list){
            GoodsDetailsArray _goodsDetails = new GoodsDetailsArray();
            newOrderItemRelationshipEntity.setItemCnMaterial(chineseName);
            newOrderItemRelationshipEntity.setItemEnMaterial(englishName);
            _goodsDetails.setDetailDescription(englishName);//详细物品描述（length:1-140）ghgdjhgj
            _goodsDetails.setDetailDescriptionCN(chineseName);//详细物品中文描述（必须包含中文字符，length:1-140）商品中文sss名
            _goodsDetails.setDetailCustomLabel(newOrderItemRelationshipEntity.getProductSku());//详细物品客户自定义标签（length:1-20，不必须）SKU NAME
            _goodsDetails.setDetailQuantity(newOrderItemRelationshipEntity.getOrderItemNumber().toString());//货品总数量"2"
            BigDecimal orderNum=new BigDecimal(newOrderItemRelationshipEntity.getOrderItemNumber());
            System.out.println("货品明细中的数量:"+orderNum);
            _goodsDetails.setDetailWorth((newOrderItemRelationshipEntity.getProductPrice().multiply(countryRate).divide(usdRate,2,BigDecimal.ROUND_HALF_UP)).toString());//货品中每个货物的价格（单位美元USD）"2"
            System.out.println("货品明细中的价格"+(newOrderItemRelationshipEntity.getProductPrice().multiply(countryRate).divide(usdRate,2,BigDecimal.ROUND_HALF_UP)).toString()+"USD");
            _goodsDetails.setHsCode(itemCode);//商品编码,海关编码（length:1-20）"15633569"////hs code1只能输入8或10位数字
            _goodsDetails.setEnMaterial(englishName);//物品英文材质（length:0-50）"dsdasd"
            _goodsDetails.setCnMaterial(chineseName);//物品中文材质（0-50）"daaaf"
            _goodsDetailsArray.add(_goodsDetails);
            totalPrice=(newOrderItemRelationshipEntity.getProductPrice().multiply(orderNum).multiply(countryRate)).divide(usdRate,2,BigDecimal.ROUND_HALF_UP);
//            totalPrice=totalPrice.add(totalPrice);
            totolNum=orderNum;
            newOrderItemRelationshipEntity.setUsdPrice(totalPrice);
            newOrderItemRelationshipEntity.setItemQuantity(orderNum.toString());
            newOrderItemRelationshipEntity.setOrderItemNumber(totolNum.intValue());
            newOrderItemRelationshipEntity.setItemCode(itemCode);
            newOrderItemRelationshipService.insert(newOrderItemRelationshipEntity);
//            totolNum=totolNum.add(totolNum);
        }

        addOrderRequestInfo.setGoodsDetails(_goodsDetailsArray);
        addOrderRequestInfo.setGoodsDeclareWorth(totalPrice.toString());
        System.out.println("申报总价值："+totalPrice.toString());
        addOrderRequestInfo.setEvaluate(totalPrice.toString());//投保价值，投保价值必须大于等于申报总价值且小于申报总价值的150%(5)
        addOrderRequestInfo.setGoodsQuantity(totolNum.toString());//总数
        System.out.println("数量"+totolNum.toString());
       return NewAbroadLogisticsSFCUtil.pushOrder(addOrderRequestInfo);
    }

    @Override
    public Map<String,String> updateOrder(String orderCode, String orderStatus) {
       return  NewAbroadLogisticsSFCUtil.updateOrder(orderCode,orderStatus);
    }

    @Override
    public String print(String orderId, int printType, String printFileType, int printSize, int printSort) {
        return NewAbroadLogisticsSFCUtil.print(orderId,printType,printFileType,printSize,printSort);
    }


}
