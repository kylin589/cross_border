package io.renren.modules.product.service.impl;

import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.product.entity.DataDictionaryEntity;
import io.renren.modules.product.entity.OrderStatisticsEneity;
import io.renren.modules.product.service.DataDictionaryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.OrderDao;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.service.OrderService;
import org.springframework.web.bind.annotation.RequestParam;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Override
    public Map<String, Object> queryMyPage(Map<String, Object> params, Long userId) {
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
        wrapper.like(StringUtils.isNotBlank(shopName), "shop_name", shopName)
                .eq(StringUtils.isNotBlank(orderStatus), "order_status", orderStatus)
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
        OrderStatisticsEneity orderCounts = baseMapper.statisticsOrderCounts(condition);
        //退货数
        int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("order_status", ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
        orderCounts.setReturnCounts(returnCounts);
        Map<String, Object> map = new HashMap<>(2);
        map.put("page",pageUtils);
        map.put("orderCounts",orderCounts);
        return map;
    }
    @Override
    public Map<String, Object> queryAllPage(Map<String, Object> params, Long deptId) {
        //店铺名称
        String userId = (String) params.get("userId");
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
        wrapper.eq(StringUtils.isNotBlank(userId), "shop_name", userId)
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
                .eq(deptId!=1L,"dept_id",deptId);

        Page<OrderEntity> page = this.selectPage(
                new Query<OrderEntity>(params).getPage(),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        OrderStatisticsEneity orderCounts = new OrderStatisticsEneity();
        Map<String, Object> condition = new HashMap<>(1);
        if(deptId == 1){
            //总公司
            orderCounts = baseMapper.statisticsOrderCounts(condition);
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("order_status", ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
            orderCounts.setReturnCounts(returnCounts);
        }else{
            //加盟商
            condition.put("deptId",deptId);
            orderCounts = baseMapper.statisticsOrderCounts(condition);
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("order_status", ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN));
            orderCounts.setReturnCounts(returnCounts);
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("page",pageUtils);
        map.put("orderCounts",orderCounts);
        return map;
    }

    @Override
    public boolean updateState(@RequestParam Long orderId, @RequestParam String orderState) {
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
    public OrderEntity queryInfoById(Long orderId) {

        return null;
    }
}

