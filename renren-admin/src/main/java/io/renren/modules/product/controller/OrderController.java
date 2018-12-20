package io.renren.modules.product.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.logistics.entity.AbroadLogisticsEntity;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.service.AbroadLogisticsService;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.entity.RemarkEntity;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.order.service.RemarkService;
import io.renren.modules.product.dto.OrderDTO;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.vm.OrderVM;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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
        OrderEntity orderEntity = orderService.selectById(orderId);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setAmazonOrderId(orderEntity.getAmazonOrderId());
        orderDTO.setBuyDate(orderEntity.getBuyDate());
        String orderStatus = orderEntity.getOrderStatus();
        orderDTO.setOrderStatus(orderStatus);
        orderDTO.setOrderState(orderEntity.getOrderState());

        String abnormalStatus = orderEntity.getAbnormalStatus();
        orderDTO.setAbnormalStatus(abnormalStatus);
        orderDTO.setAbnormalState(orderEntity.getAbnormalState());

        orderDTO.setShopName(orderEntity.getShopName());
        orderDTO.setProductId(orderEntity.getProductId());
        orderDTO.setProductSku(orderEntity.getProductSku());
        orderDTO.setProductTitle(productsService.selectById(orderEntity.getProductId()).getProductTitle());
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

        BigDecimal momentRate = orderEntity.getMomentRate();
        orderDTO.setMomentRate(momentRate);
        //判断订单异常状态——不属于退货
        if(!ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN.equals(abnormalStatus)){
            //国际已发货、已完成订单
            if(ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED.equals(orderStatus) || ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH.equals(orderStatus)){
                //获取订单金额（外币）
                BigDecimal orderMoneyForeign = orderEntity.getOrderMoney();
                //设置订单金额（外币）
                orderDTO.setOrderMoneyForeign(orderMoneyForeign);
                //设置订单金额（人民币：外币*当时汇率）
                orderDTO.setOrderMoney(orderMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
                //获取Amazon佣金（外币）
                BigDecimal amazonCommissionForeign = orderEntity.getAmazonCommission();
                //订单表保存数据
                //设置Amazon佣金（外币）
                orderDTO.setAmazonCommissionForeign(amazonCommissionForeign);
                //设置Amazon佣金（人民币：外币*当时汇率）
                orderDTO.setAmazonCommission(amazonCommissionForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
                //到账金额
                BigDecimal accountMoneyForeign = orderEntity.getAccountMoney();
                orderDTO.setAccountMoneyForeign(accountMoneyForeign);
                orderDTO.setAccountMoney(accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
                //采购价
                BigDecimal purchasePrice = orderEntity.getPurchasePrice();
                orderDTO.setPurchasePrice(purchasePrice);
                // TODO: 2018/12/15  国际运费()\平台佣金(platformCommissions)\利润(orderProfit)
                //国际运费
                BigDecimal interFreight = orderEntity.getInterFreight();
                //平台佣金
                BigDecimal platformCommissions = orderEntity.getPlatformCommissions();
                //利润
                BigDecimal orderProfit = orderEntity.getOrderProfit();

            }else {
                //属于取消订单不处理
                if(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(orderStatus)){

                }else{
                    //不属于取消订单
                    //获取订单金额（外币）
                    BigDecimal orderMoneyForeign = orderEntity.getOrderMoney();
                    //设置订单金额（外币）
                    orderDTO.setOrderMoneyForeign(orderMoneyForeign);
                    //设置订单金额（人民币：外币*当时汇率）
                    orderDTO.setOrderMoney(orderMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
                    //获取Amazon佣金（外币）
                    BigDecimal amazonCommissionForeign = orderEntity.getAmazonCommission();
                    //设置Amazon佣金（外币）
                    orderDTO.setAmazonCommissionForeign(amazonCommissionForeign);
                    //设置Amazon佣金（人民币：外币*当时汇率）
                    orderDTO.setAmazonCommission(amazonCommissionForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
                    //到账金额
                    BigDecimal accountMoneyForeign = orderEntity.getAccountMoney();
                    orderDTO.setAccountMoneyForeign(accountMoneyForeign);
                    orderDTO.setAccountMoney(accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
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
            //平台佣金设置为0.00
            orderDTO.setPlatformCommissions(new BigDecimal(0.00));
            //国际运费
            BigDecimal interFreight = orderEntity.getInterFreight();
            orderDTO.setInterFreight(interFreight);
            //退货费用
            BigDecimal returnCost = orderEntity.getReturnCost();
            orderDTO.setReturnCost(returnCost);
            //利润
            BigDecimal orderProfit = accountMoneyForeign.multiply(momentRate).subtract(returnCost).setScale(2,BigDecimal.ROUND_HALF_UP);
            orderDTO.setOrderProfit(orderProfit);
        }
        List<RemarkEntity> remarkList = remarkService.selectList(new EntityWrapper<RemarkEntity>().eq("type","reamrk").eq("order_id",orderId));
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
        ValidatorUtils.validateEntity(order);
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
     * 修改订单状态
     */
    @RequestMapping("/updateState")
    public R updateState(@RequestBody OrderVM orderVM){
        boolean flag = orderService.updateState(orderVM.getOrderId(),orderVM.getOrderState());
        if(flag){
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
        Long orderId = orderVM.getOrderId();
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("YT");
        for (int i = 0; i < 16; i++) {
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
        orderService.updateById(order);
        //生成国际物流对象
        AbroadLogisticsEntity abroadLogistics = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
        if(abroadLogistics == null){
            abroadLogistics = new AbroadLogisticsEntity();
            abroadLogistics.setOrderId(orderId);
            abroadLogistics.setAbroadWaybill(abroadWaybill);
            abroadLogisticsService.insert(abroadLogistics);
        }else{
            abroadLogistics.setAbroadWaybill(abroadWaybill);
            abroadLogisticsService.updateById(abroadLogistics);
        }

        // TODO: 2018/12/18 将运单号同步到亚马逊平台
        return R.ok();
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
        //获取运单号
        String abroadWaybill = abroadLogistics.getAbroadWaybill();
        //获取跟踪号
        String trackWaybill = abroadLogistics.getTrackWaybill();
        boolean flag = false;
        // TODO: 2018/12/18 将运单号同步到亚马逊平台
        if(flag){
            abroadLogistics.setIsSynchronization(1);
            abroadLogistics.setUpdateTime(new Date());
            abroadLogisticsService.updateById(abroadLogistics);
            return R.ok();
        }else{
            return R.error("同步失败");
        }
    }

}
