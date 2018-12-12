package io.renren.modules.product.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.product.dto.OrderDTO;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.service.OrderService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



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
     * 所有订单
     */
    @RequestMapping("/getAllList")
//    @RequiresPermissions("product:order:alllist")
    public R getAllList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = orderService.queryAllPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }
    /**
     * 订单详情
     */
    @RequestMapping("/getOrderInfo")
    public R getOrderInfo(@RequestParam(value = "orderId") Long orderId){
        OrderEntity orderEntity = orderService.queryInfoById(orderId);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setAmazonOrderId(orderEntity.getAmazonOrderId());
        orderDTO.setBuyDate(orderEntity.getBuyDate());
        String orderStatus = orderEntity.getOrderStatus();
        orderDTO.setOrderStatus(orderStatus);
        orderDTO.setOrderState(orderEntity.getOrderState());
        orderDTO.setShopName(orderEntity.getShopName());
        orderDTO.setProductId(orderEntity.getProductId());
        orderDTO.setProductSku(orderEntity.getProductSku());
        orderDTO.setProductAsin(orderEntity.getProductAsin());
        orderDTO.setOrderNumber(orderEntity.getOrderNumber());
        orderDTO.setPurchasePrice(orderEntity.getPurchasePrice());
        ProductShipAddressEntity shipAddress = productShipAddressService.selectOne(
                new EntityWrapper<ProductShipAddressEntity>().eq("order_id",orderId)
        );
        orderDTO.setShipAddress(shipAddress);
        List<DomesticLogisticsEntity> domesticLogisticsList = domesticLogisticsService.selectList(
                new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",orderId)
        );
        orderDTO.setDomesticLogisticsList(domesticLogisticsList);
        // TODO: 2018/12/11 国际物流
        BigDecimal momentRate = orderEntity.getMomentRate();
        orderDTO.setMomentRate(momentRate);
        //判断订单状态
//        if(orderStatus.equals(ConstantDictionary.OrderStateCode))
        //获取订单金额（外币）
        BigDecimal orderMoneyForeign = orderEntity.getOrderMoney();
        //设置订单金额（外币）
        orderDTO.setOrderMoneyForeign(orderMoneyForeign);
        //设置订单金额（人民币：外币*当时汇率）
        orderDTO.setOrderMoney(orderMoneyForeign.multiply(momentRate));
        //获取Amazon佣金（外币）
        BigDecimal amazonCommissionForeign = orderEntity.getAmazonCommission();
        //订单表保存数据
        //设置Amazon佣金（外币）
        orderDTO.setAmazonCommissionForeign(amazonCommissionForeign);
        //设置Amazon佣金（人民币：外币*当时汇率）
        orderDTO.setAmazonCommission(amazonCommissionForeign.multiply(momentRate));
        //

        return R.ok();
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
        ValidatorUtils.validateEntity(order);
        orderService.updateAllColumnById(order);//全部更新
        
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
     * 修改状态
     */
    @RequestMapping("/updateState")
    public R updateState(@RequestParam Long orderId, @RequestParam String orderState){
        boolean flag = orderService.updateState(orderId,orderState);
        if(flag){
            return R.ok();
        }
        return R.error();
    }

}
