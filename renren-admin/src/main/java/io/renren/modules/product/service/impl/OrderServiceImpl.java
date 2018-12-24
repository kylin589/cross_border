package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.vm.OrderModel;
import io.renren.modules.sys.dto.FranchiseeStatisticsDto;
import io.renren.modules.sys.dto.PlatformStatisticsDto;
import io.renren.modules.sys.dto.UserStatisticsDto;
import io.renren.modules.util.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.logistics.DTO.OmsOrder;
import io.renren.modules.logistics.DTO.OmsOrderDetail;
import io.renren.modules.logistics.DTO.OmsShippingAddr;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.logistics.util.AbroadLogisticsUtil;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.product.dao.OrderDao;
import io.renren.modules.product.service.AmazonRateService;
import io.renren.modules.product.service.DataDictionaryService;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.sys.dto.StatisticsDto;
import io.renren.modules.sys.entity.ConsumeEntity;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.ConsumeService;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.vm.StatisticsVM;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private AmazonRateService amazonRateService;
    @Autowired
    private ProductShipAddressService productShipAddressService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ConsumeService consumeService;
    @Autowired
    private ProductsService productsService;
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
        EntityWrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
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
                .eq("user_id",userId);

        Page<OrderEntity> page = this.selectPage(
                new Query<OrderEntity>(params).getPage(),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        Map<String, Object> condition = new HashMap<>(1);
        condition.put("userId",userId);
        OrderStatisticsEntity orderCounts = baseMapper.statisticsOrderCounts(condition);
        //核算订单数
        int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH));
        orderCounts.setOrderCounts(completeCounts);
        //退货数
        int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("abnormal_status", ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
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
        EntityWrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
        if(deptId == 1L){
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
                    .eq(StringUtils.isNotBlank(qDeptId),"dept_id",qDeptId);
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
                    .eq("dept_id",deptId);
        }


        Page<OrderEntity> page = this.selectPage(
                new Query<OrderEntity>(params).getPage(),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        OrderStatisticsEntity orderCounts = new OrderStatisticsEntity();
        Map<String, Object> condition = new HashMap<>(1);
        if(deptId == 1L){
            //总公司
            orderCounts = baseMapper.statisticsOrderCounts(condition);
            //核算订单数
            int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH));
            orderCounts.setOrderCounts(completeCounts);
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("abnormal_status", ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
            orderCounts.setReturnCounts(returnCounts);
        }else{
            //加盟商
            condition.put("deptId",deptId);
            orderCounts = baseMapper.statisticsOrderCounts(condition);
            //核算订单数
            int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH));
            orderCounts.setOrderCounts(completeCounts);
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("abnormal_status", ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
            orderCounts.setReturnCounts(returnCounts);
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("page",pageUtils);
        map.put("orderCounts",orderCounts);
        return map;
    }

    @Override
    public boolean updateState(Long orderId, String orderState) {
        String orderStatus = dataDictionaryService.selectOne(
                new EntityWrapper<DataDictionaryEntity>()
                        .eq("data_type","AMAZON_ORDER_STATE")
                        .eq("data_content",orderState)
        ).getDataNumber();
        
        int flag = baseMapper.updateState(orderId,orderState,orderStatus);
        if(flag != 0){
            return true;
        }
        return false;
    }

    @Override
    public Map<String,String> pushOrder(Long orderId) {
        OrderEntity orderEntity = this.selectById(orderId);
        ProductShipAddressEntity shipAddressEntity = productShipAddressService.selectOne(new EntityWrapper<ProductShipAddressEntity>().eq("order_id",orderId));
        //推送--订单基本信息
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrder_sn(orderId.toString());
        omsOrder.setOrder_currency(orderEntity.getRateCode());
        //设置时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS+08:00");
        omsOrder.setOrder_date(sdf.format(orderEntity.getBuyDate()));
        omsOrder.setOrder_memo(shipAddressEntity.getShipCountry());
        //推送--订单详情
        OmsOrderDetail omsOrderDetail = new OmsOrderDetail();
        omsOrderDetail.setProduct_id(orderEntity.getProductSku());
        omsOrderDetail.setQuantity(orderEntity.getOrderNumber());
        omsOrderDetail.setSupplyexpressno(orderEntity.getDomesticWaybill());
        //推送—收货人信息
        OmsShippingAddr omsShippingAddr = new OmsShippingAddr();
        omsShippingAddr.setAddress_line1(shipAddressEntity.getShipAddressLine1());
        omsShippingAddr.setAddress_line2(shipAddressEntity.getShipAddressLine2());
        omsShippingAddr.setAddress_line3(shipAddressEntity.getShipAddressLine3());
        omsShippingAddr.setCustaddress(shipAddressEntity.getShipAddressLine1() + " " + shipAddressEntity.getShipAddressLine2() + " " + shipAddressEntity.getShipAddressLine3());
        omsShippingAddr.setCustcity(shipAddressEntity.getShipCity());
        omsShippingAddr.setCustcountry(shipAddressEntity.getShipCountry());
        omsShippingAddr.setCustomer(shipAddressEntity.getShipName());
        omsShippingAddr.setCustphone(shipAddressEntity.getShipTel());
        omsShippingAddr.setCuststate(shipAddressEntity.getShipRegion());
        omsShippingAddr.setCustzipcode(shipAddressEntity.getShipZip());
        JSONObject orderDataJson = new JSONObject();
        JSONObject omsOrderJson = JSONObject.fromObject(omsOrder);
        JSONArray orderDetailListJson = JSONArray.fromObject(omsOrderDetail);
        JSONObject omsShippingAddrJson = JSONObject.fromObject(omsShippingAddr);
        orderDataJson.put("order",omsOrderJson);
        orderDataJson.put("orderDetailList",orderDetailListJson);
        orderDataJson.put("address",omsShippingAddrJson);
        Map<String,String> result = AbroadLogisticsUtil.pushOrder(orderDataJson.toString());
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
            if(ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN.equals(abnormalStatus)){
                List<OrderEntity> orderList = new ArrayList<>();
                for(int i = 0; i < orderIds.length; i++){
                    OrderEntity orderEntity = this.selectById(orderIds[i]);
                    //订单当时税率
                    BigDecimal momentRate = orderEntity.getMomentRate();
                    //订单金额
                    BigDecimal orderMoneyForeign = orderEntity.getOrderMoney();
                    //获取Amazon佣金（外币）
                    BigDecimal amazonCommissionForeign = orderMoneyForeign.multiply(new BigDecimal(0.03)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    orderEntity.setAmazonCommission(amazonCommissionForeign);
                    //到账金额
                    BigDecimal accountMoneyForeign = new BigDecimal(0.00).subtract(amazonCommissionForeign);
                    orderEntity.setAccountMoney(accountMoneyForeign);
                    //国际运费
                    BigDecimal interFreight = orderEntity.getInterFreight();
                    if(interFreight.compareTo(new BigDecimal(0.0)) == 0){
                        //没产生运费
                        //利润 = 到账金额(人民币)
                        BigDecimal orderProfit = accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP);
                        orderEntity.setOrderProfit(orderProfit);
                    }else{
                        //产生运费
                        //平台佣金点数
                        BigDecimal companyPoint = deptService.selectById(orderEntity.getDeptId()).getCompanyPoints();
                        //平台服务费 = （订单金额-Amazon佣金）* 汇率 * 平台佣金点数
                        BigDecimal a = orderMoneyForeign.subtract(amazonCommissionForeign);
                        BigDecimal b = a.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP);
                        BigDecimal platformCommissions = b.multiply(companyPoint).setScale(2,BigDecimal.ROUND_HALF_UP);
                        orderEntity.setPlatformCommissions(platformCommissions);
                        //利润
                        //到账金额——>人民币
                        BigDecimal c = accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP);
                        BigDecimal orderProfit = c.subtract(interFreight).subtract(platformCommissions).setScale(2,BigDecimal.ROUND_HALF_UP);
                        orderEntity.setOrderProfit(orderProfit);
                        //扣款
                        deduction(orderEntity);
                    }
                    orderList.add(orderEntity);
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
    public void cancleOrder(String amazonOrderId) {
        OrderEntity orderEntity = this.selectOne(new EntityWrapper<OrderEntity>().eq("amazon_order_id",amazonOrderId));
        orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED);
        orderEntity.setOrderState("取消");
    }

    /**
     * 获取（更新）订单
     * 是否区分不同国家还是区分不同区域
     * 有没有国家的字段、币种字段
     * @param orderModelList
     */
    @Override
    public void updateOrder(SysUserEntity user, List<OrderModel> orderModelList) {
        for(OrderModel orderModel : orderModelList ){
            //获取亚马逊订单id
            String amazonOrderId = orderModel.getAmazonOrderId();
            //判断该订单是否存在
            OrderEntity orderEntity = this.selectOne(new EntityWrapper<OrderEntity>().eq("amazon_order_id",amazonOrderId));
            if(orderEntity == null){
                //新增订单
                String modelStatus = orderModel.getOrderStatus();
                if(!"Canceled".equals(modelStatus)){
                    // TODO: 2018/12/21 设置基本属性*
                    orderEntity.setAmazonOrderId(orderModel.getAmazonOrderId());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
                    try {
                        orderEntity.setBuyDate(sdf.parse(orderModel.getBuyDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if("PendingAvailability".equals(modelStatus) || "Pending".equals(modelStatus)){
                        //未付款
                        orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_PENDING);
                        orderEntity.setOrderState("待付款");
                    }else if("Unshipped".equals(modelStatus) || "PartiallyShipped".equals(modelStatus)){
                        //已付款
                        orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_UNSHIPPED);
                        orderEntity.setOrderState("已付款");
                    }
                    if(orderModel.getProductShipAddressEntity() != null && StringUtils.isNotBlank(orderModel.getProductShipAddressEntity().getShipCountry())){
                        orderEntity.setCountryCode(orderModel.getProductShipAddressEntity().getShipCountry());
                    }
//                    orderEntity.setShopName();
                    orderEntity.setProductSku(orderModel.getProductSku());
                    orderEntity.setProductAsin(orderModel.getProductAsin());
                    productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku",orderModel.getProductSku()));
                    orderEntity.setUserId(user.getUserId());
                    orderEntity.setDeptId(user.getDeptId());
                    orderEntity.setUpdateTime(new Date());
                    //设置汇率*
                    BigDecimal rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("","countryCode")).getRate();
                    orderEntity.setMomentRate(rate);
                    //获取订单金额（外币）*
                    BigDecimal orderMoney = orderEntity.getOrderMoney();
                    //获取Amazon佣金（外币）
                    BigDecimal amazonCommission = orderMoney.multiply(new BigDecimal(0.15));
                    //到账金额
                    BigDecimal accountMoney = orderMoney.subtract(amazonCommission);
                    //TODO: 2018/12/21设置金额*(同时设置人民币)
                    //设置订单金额（人民币：外币*当时汇率）
//                orderDTO.setOrderMoney(orderMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
                    //设置Amazon佣金（人民币：外币*当时汇率）
//                orderDTO.setAmazonCommission(amazonCommissionForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
                    //设置到账金额（人民币）
//                BigDecimal accountMoney = accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP);
//                orderDTO.setAccountMoney(accountMoney);
                    // TODO: 2018/12/21 新增收货人信息*
                }
            }else{
                //更新订单
                if(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals("")){//获取状态判断是否为取消*
                    //获取亚马逊订单id*
//                    this.cancleOrder();
                }else{
                    //获取当前订单状态判断是否为待付款、已付款、虚发货
                    if(Arrays.asList(ConstantDictionary.OrderStateCode.AMAZON_ORDER_STATE).contains(orderEntity.getOrderState())){
                        if(Arrays.asList(ConstantDictionary.OrderStateCode.AMAZON_ORDER_STATE).contains("")){//获取返回状态判断是否为待付款、已付款、虚发货
                            if(!"".equals(orderEntity.getOrderState())){//判断两个状态不想等时更改状态
                                orderEntity.setOrderStatus("");
                                String orderState = dataDictionaryService.selectOne(
                                        new EntityWrapper<DataDictionaryEntity>()
                                                .eq("data_type","AMAZON_ORDER_STATE")
                                                .eq("data_number","")
                                ).getDataContent();
                                orderEntity.setOrderState(orderState);
                            }
                        }
                    }
                    if(1==1){//判断返回值是否有收件人信息
                        ProductShipAddressEntity shipAddress = productShipAddressService.selectOne(
                                new EntityWrapper<ProductShipAddressEntity>().eq("order_id",orderEntity.getOrderId())
                        );
                        if(shipAddress == null){
                            // TODO: 2018/12/21 设置物流信息
                        }
                    }


                }
            }
        }
    }

    @Override
    public void internationalShipments(OrderEntity order) {
        order.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED);
        order.setOrderState("国际已发货");
        deduction(order);
    }


    @Override
    public void deduction(OrderEntity order){
        //扣款
        SysDeptEntity dept = deptService.selectById(order.getDeptId());
        //原来余额
        BigDecimal oldBalance = dept.getBalance();
        BigDecimal nowBalance = oldBalance.subtract(order.getInterFreight()).subtract(order.getPlatformCommissions());
        dept.setBalance(nowBalance);
        deptService.updateById(dept);
        //生成运费记录
        ConsumeEntity consumeEntity1 = new ConsumeEntity();
        consumeEntity1.setDeptId(order.getDeptId());
        consumeEntity1.setDeptName(deptService.selectById(order.getDeptId()).getName());
        consumeEntity1.setUserId(order.getUserId());
        consumeEntity1.setUserName(userService.selectById(order.getUserId()).getDisplayName());
        consumeEntity1.setType("物流费");
        consumeEntity1.setOrderId(order.getOrderId());
        consumeEntity1.setMoney(order.getInterFreight());
        consumeEntity1.setBeforeBalance(oldBalance);
        consumeEntity1.setAfterBalance(oldBalance.subtract(order.getInterFreight()));
        consumeEntity1.setAbroadWaybill(order.getAbroadWaybill());
        consumeEntity1.setCreateTime(new Date());
        consumeService.insert(consumeEntity1);
        //生成服务费记录
        ConsumeEntity consumeEntity2 = new ConsumeEntity();
        consumeEntity2.setDeptId(order.getDeptId());
        consumeEntity2.setDeptName(deptService.selectById(order.getDeptId()).getName());
        consumeEntity2.setUserId(order.getUserId());
        consumeEntity2.setUserName(userService.selectById(order.getUserId()).getDisplayName());
        consumeEntity2.setType("服务费");
        consumeEntity2.setOrderId(order.getOrderId());
        consumeEntity2.setMoney(order.getPlatformCommissions());
        consumeEntity2.setBeforeBalance(oldBalance.subtract(order.getInterFreight()));
        consumeEntity2.setAfterBalance(nowBalance);
        consumeEntity2.setCreateTime(new Date());
        consumeService.insert(consumeEntity2);
    }

    @Override
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
        EntityWrapper orderWrapper = new EntityWrapper<OrderEntity>();
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
    }

    @Override
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
        EntityWrapper orderWrapper = new EntityWrapper<OrderEntity>();
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
    }
    @Override
    public PlatformStatisticsDto platformStatistics(StatisticsVM vm){
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
    }

/*    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = "2018-12-14T15:13:03.290Z";
        String[] b = d.split(".");
        System.out.println(b.length);
//        Date a = sdf.parse(b);
//        System.out.println(a);
    }*/
}

