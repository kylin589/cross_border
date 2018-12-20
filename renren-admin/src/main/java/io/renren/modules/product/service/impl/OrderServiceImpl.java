package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.product.dao.OrderDao;
import io.renren.modules.product.entity.AmazonRateEntity;
import io.renren.modules.product.entity.DataDictionaryEntity;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.entity.OrderStatisticsEntity;
import io.renren.modules.product.service.AmazonRateService;
import io.renren.modules.product.service.DataDictionaryService;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
                .eq(deptId!=1L,"dept_id",deptId);

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
     * 是否区分不同国家还是区分不同区域
     * 有没有国家的字段、币种字段
     * @param objList
     */
    @Override
    public void updateOrder(SysUserEntity user, List<OrderEntity> objList) {
        for(OrderEntity i : objList ){
            //获取亚马逊订单id
            String amazonOrderId = i.getAmazonOrderId();
            //判断该订单是否存在
            OrderEntity orderEntity = this.selectOne(new EntityWrapper<OrderEntity>().eq("amazon_order_id",amazonOrderId));
            if(orderEntity == null){
                //新增订单
                // TODO: 2018/12/21 设置基本属性*
                orderEntity.setUserId(user.getUserId());
                orderEntity.setDeptId(user.getDeptId());
                orderEntity.setUpdateTime(new Date());

                //获取订单金额（外币）*
                BigDecimal orderMoney = orderEntity.getOrderMoney();
                //获取Amazon佣金（外币）
                BigDecimal amazonCommission = orderMoney.multiply(new BigDecimal(0.15));
                //到账金额
                BigDecimal accountMoney = orderMoney.subtract(amazonCommission);
                //TODO: 2018/12/21设置金额*
                //设置汇率*
                BigDecimal rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("","countryCode")).getRate();
                orderEntity.setMomentRate(rate);
                // TODO: 2018/12/21 新增收货人信息*
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
}

