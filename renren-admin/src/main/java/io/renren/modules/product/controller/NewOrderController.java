package io.renren.modules.product.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderRequest;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.JsonArray;
import io.renren.common.utils.DateUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.entity.AmazonMarketplaceEntity;
import io.renren.modules.logistics.entity.NewOrderDomesticLogisticsEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.service.AmazonMarketplaceService;
import io.renren.modules.logistics.service.NewOrderDomesticLogisticsService;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.amazon.util.FileUtil;
import io.renren.modules.logistics.entity.*;
import io.renren.modules.logistics.service.*;
import io.renren.modules.logistics.util.XmlUtils;
import io.renren.modules.order.entity.NewProductShipAddressEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.entity.RemarkEntity;
import io.renren.modules.order.service.NewProductShipAddressService;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.order.service.RemarkService;
import io.renren.modules.product.dto.NewOrderDTO;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import io.renren.modules.product.vm.OrderItemModel;
import io.renren.modules.product.vm.OrderModel;
import io.renren.modules.product.vm.OrderVM;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.NoticeService;
import io.renren.modules.sys.service.SysDeptService;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import static io.renren.modules.product.controller.OrderController.invokeGetOrder;


/**
 * 新订单表
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-28 14:50:57
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("amazon/neworder")
public class NewOrderController extends AbstractController {

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
    private NewOrderService newOrderService;
    @Autowired
    private NewOrderItemService newOrderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductOrderItemService productOrderItemService;
    @Autowired
    private ProductShipAddressService productShipAddressService;
    @Autowired
    private NewProductShipAddressService newproductShipAddressService;
    @Autowired
    private NewOrderAbroadLogisticsService newOrderAbroadLogisticsService;
    @Autowired
    private NewOrderDomesticLogisticsService newOrderDomesticLogisticsService;
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
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;
    @Autowired
    private SubmitLogisticsService submitLogisticsService;
    @Autowired
    private AmazonGrantService amazonGrantService;
    @Autowired
    private AmazonRateService amazonRateService;
    @Autowired
    private LogisticsChannelService logisticsChannelService;
    @Value(("${file.path}"))
    private String fileStoragePath;

    /**
     * 我的订单
     */
    @RequestMapping("/getMyList")
//    @RequiresPermissions("product:order:mylist")
    public R getMyList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = newOrderService.queryMyPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }

    /**
     * 获取物流列表-查询
     * @param params
     * @return
     */
    @RequestMapping("/depotOrderList")
    public R depotOrderList(@RequestParam Map<String, Object> params){
        PageUtils page = newOrderService.queryNewAllPage(params);
        return R.ok().put("page",page);
    }
    /**
     * 列表入库
     * @param
     * @return
     */
    @RequestMapping("/listruku")
    public R listruku(@RequestParam Long orderId){
        NewOrderEntity orderEntity = newOrderService.selectById(orderId);
        List<NewOrderDomesticLogisticsEntity> domesticList = newOrderDomesticLogisticsService.selectList(new EntityWrapper<NewOrderDomesticLogisticsEntity>().eq("waybill",orderEntity.getDomesticWaybill()));
        for(NewOrderDomesticLogisticsEntity dom : domesticList){
            dom.setState("已入库");
        }
        newOrderDomesticLogisticsService.updateBatchById(domesticList);
        List<NewOrderDomesticLogisticsEntity> domesticList1 = newOrderDomesticLogisticsService.selectList(new EntityWrapper<NewOrderDomesticLogisticsEntity>().eq("order_id",orderId));
        if(domesticList.size() == domesticList1.size()) {
            orderEntity.setOrderStatus("Warehousing");
            orderEntity.setOrderState("仓库已入库");
            newOrderService.updateById(orderEntity);
        }
        return R.ok("入库成功");
    }
    /**
     * 列表入库
     * @param
     * @return
     */
    @RequestMapping("/formruku")
    public R formruku(@RequestBody NewOrderDomesticLogisticsEntity domestic){
        Long orderId = domestic.getOrderId();
        String waybill = domestic.getWaybill();
        if(orderId != null && StringUtils.isNotBlank(waybill)){
            List<NewOrderDomesticLogisticsEntity> domesticList = newOrderDomesticLogisticsService.selectList(new EntityWrapper<NewOrderDomesticLogisticsEntity>().eq("waybill",waybill));
            for(NewOrderDomesticLogisticsEntity dom : domesticList){
                dom.setState("已入库");
            }
            newOrderDomesticLogisticsService.updateBatchById(domesticList);
            List<NewOrderDomesticLogisticsEntity> domesticList1 = newOrderDomesticLogisticsService.selectList(new EntityWrapper<NewOrderDomesticLogisticsEntity>().eq("order_id",orderId));
            if(domesticList.size() == domesticList1.size()){
                NewOrderEntity orderEntity = newOrderService.selectById(orderId);
                orderEntity.setOrderStatus("Warehousing");
                orderEntity.setOrderState("仓库已入库");
                newOrderService.updateById(orderEntity);
            }
        }
        return R.ok("入库成功");
    }
    /**
     * 列表出库
     * @param
     * @return
     */
    @RequestMapping("/listchuku")
    public R listchuku(@RequestParam Long orderId){
        //订单id
        NewOrderEntity orderEntity = newOrderService.selectById(orderId);
        NewOrderAbroadLogisticsEntity abroad = newOrderAbroadLogisticsService.selectOne(new EntityWrapper<NewOrderAbroadLogisticsEntity>().eq("abroad_waybill",orderEntity.getAbroadWaybill()));
        if(abroad.getIsDeleted() == 1){
            return R.error("出库失败，运单已销毁");
        }else{
            abroad.setState("已出库");
            newOrderAbroadLogisticsService.updateById(abroad);
            orderEntity.setOrderStatus("IntlShipped");
            orderEntity.setOrderState("仓库已出库");
            newOrderService.updateById(orderEntity);
        }
        return R.ok();
    }
    /**
     * 列表出库
     * @param
     * @return
     */
    @RequestMapping("/formchuku")
    public R formchuku(@RequestBody NewOrderAbroadLogisticsEntity abroad){
        if(abroad.getIsDeleted() == 1){
            return R.error("此运单已销毁");
        }else{
            Long orderId = abroad.getOrderId();
            if(orderId != null){
                abroad.setState("已出库");
                newOrderAbroadLogisticsService.updateById(abroad);
                NewOrderEntity orderEntity = newOrderService.selectById(orderId);
                orderEntity.setOrderStatus("IntlShipped");
                orderEntity.setOrderState("仓库已出库");
                newOrderService.updateById(orderEntity);
            }else{
                return R.error();
            }
        }
        return R.ok();
    }
    /**
     * 所有订单
     */
    @RequestMapping("/getAllList")
//    @RequiresPermissions("product:order:alllist")
    public R getAllList(@RequestParam Map<String, Object> params){
        Map<String, Object> map = newOrderService.queryAllPage(params, getDeptId());
        return R.ok().put("page", map.get("page")).put("orderCounts",map.get("orderCounts"));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{orderId}")
    @RequiresPermissions("amazon:neworder:info")
    public R info(@PathVariable("orderId") Long orderId){
        NewOrderEntity newOrder = newOrderService.selectById(orderId);

        return R.ok().put("newOrder", newOrder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("amazon:neworder:save")
    public R save(@RequestBody NewOrderEntity newOrder){
        newOrderService.insert(newOrder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("amazon:neworder:update")
    public R update(@RequestBody NewOrderEntity newOrder){
        ValidatorUtils.validateEntity(newOrder);
        newOrderService.updateAllColumnById(newOrder);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("amazon:neworder:delete")
    public R delete(@RequestBody Long[] orderIds){
        newOrderService.deleteBatchIds(Arrays.asList(orderIds));
        return R.ok();
    }

    /**
     * 订单详情
     */
    @RequestMapping("/getOrderInfo")
    public R getOrderInfo(@RequestParam(value = "orderId") Long orderId){
        NewOrderEntity neworderEntity =newOrderService.selectById(orderId);
        BigDecimal momentRate = neworderEntity.getMomentRate();
        String orderStatus = neworderEntity.getOrderStatus();
        String abnormalStatus = neworderEntity.getAbnormalStatus();
        //更新订单物流等信息
//        new RefreshOrderThread(orderId).start();
        NewOrderDTO orderDTO = new NewOrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setAmazonOrderId(neworderEntity.getAmazonOrderId());
        orderDTO.setBuyDate(neworderEntity.getBuyDate());
        orderDTO.setOrderStatus(orderStatus);
        orderDTO.setOrderState(neworderEntity.getOrderState());

        orderDTO.setAbnormalStatus(abnormalStatus);
        orderDTO.setAbnormalState(neworderEntity.getAbnormalState());
        orderDTO.setMomentRate(momentRate);
        String AmazonOrderId=neworderEntity.getAmazonOrderId();
        List<NewOrderItemEntity> productOrderItemEntitys=newOrderItemService.selectList(new EntityWrapper<NewOrderItemEntity>().eq("amazon_order_id",AmazonOrderId));
        orderDTO.setShopName(neworderEntity.getShopName());
        orderDTO.setOrderNumber(neworderEntity.getOrderNumber());
        orderDTO.setPurchasePrice(neworderEntity.getPurchasePrice());
        NewProductShipAddressEntity newshipAddress = newproductShipAddressService.selectOne(
                new EntityWrapper<NewProductShipAddressEntity>().eq("amazon_order_id",AmazonOrderId)
        );
        if(newshipAddress == null){
            orderDTO.setShipAddress(new ProductShipAddressEntity());
        }else{
            ProductShipAddressEntity shipAddress=productShipAddressService.selectOne(
                    new EntityWrapper<ProductShipAddressEntity>().eq("order_id",newshipAddress.getOrderId())
            );
            orderDTO.setShipAddress(shipAddress);
        }

        List<DomesticLogisticsEntity> domesticLogisticsList = domesticLogisticsService.selectList(
                new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",orderId)
        );
        orderDTO.setDomesticLogisticsList(domesticLogisticsList);
        //国际物流
        NewOrderAbroadLogisticsEntity newabroadLogistics = newOrderAbroadLogisticsService.selectOne(new EntityWrapper<NewOrderAbroadLogisticsEntity>().eq("order_id",orderId));
        if(newabroadLogistics == null){
            orderDTO.setAbroadLogistics(new NewOrderAbroadLogisticsEntity());
        }else{
            orderDTO.setAbroadLogistics(newabroadLogistics);
        }
        for (NewOrderItemEntity newproductOrderItemEntity:productOrderItemEntitys) {
            ProductsEntity productsEntity = productsService.selectById(newproductOrderItemEntity.getProductId());
            if(StringUtils.isBlank(newproductOrderItemEntity.getProductTitle()) && productsEntity != null){
                newproductOrderItemEntity.setProductTitle(productsEntity.getProductTitle());
            }
            //设置amazon产品链接 amazonProductUrl
            AmazonMarketplaceEntity amazonMarketplaceEntity = amazonMarketplaceService.selectOne(new EntityWrapper<AmazonMarketplaceEntity>().eq("country_code",neworderEntity.getCountryCode()));
            String amazonProductUrl = amazonMarketplaceEntity.getAmazonSite() + "/gp/product/" + newproductOrderItemEntity.getProductAsin();
            newproductOrderItemEntity.setProductImageUrl(amazonProductUrl);
            DomesticLogisticsEntity domesticLogistics = domesticLogisticsService.selectOne(new EntityWrapper<DomesticLogisticsEntity>().eq("item_id",newproductOrderItemEntity.getItemId()));
            newproductOrderItemEntity.setDomesticLogistics(domesticLogistics);
        }
        //判断订单异常状态——不属于退货
        if(!ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_RETURN.equals(abnormalStatus)){
            //国际已发货、已完成订单
            if(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_INTLSHIPPED.equals(orderStatus) || ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_FINISH.equals(orderStatus)){
                //获取订单金额（外币）
                BigDecimal orderMoneyForeign = neworderEntity.getOrderMoney();
                //设置订单金额（外币）
                orderDTO.setOrderMoneyForeign(orderMoneyForeign);
                //设置订单金额（人民币）
                orderDTO.setOrderMoney(neworderEntity.getOrderMoneyCny());
                //设置Amazon佣金（外币）
                orderDTO.setAmazonCommissionForeign(neworderEntity.getAmazonCommission());
                //设置Amazon佣金（人民币）
                orderDTO.setAmazonCommission(neworderEntity.getAmazonCommissionCny());
                //设置到账金额（外币）
                orderDTO.setAccountMoneyForeign(neworderEntity.getAccountMoney());
                //设置到账金额（人民币）
                orderDTO.setAccountMoney(neworderEntity.getAccountMoneyCny());
                //采购价
                BigDecimal purchasePrice = neworderEntity.getPurchasePrice();
                orderDTO.setPurchasePrice(purchasePrice);
                //国际运费
                BigDecimal interFreight = neworderEntity.getInterFreight();
                orderDTO.setInterFreight(interFreight);
                //平台佣金
                BigDecimal platformCommissions = neworderEntity.getPlatformCommissions();
                orderDTO.setPlatformCommissions(platformCommissions);
                //利润
                BigDecimal orderProfit = neworderEntity.getOrderProfit();
                orderDTO.setOrderProfit(orderProfit);
                //利润率
                BigDecimal profitRate = neworderEntity.getProfitRate();
                orderDTO.setProfitRate(profitRate);
            }else {
                //属于取消订单不处理
                if(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_CANCELED.equals(orderStatus)){

                }else{
                    //不属于取消订单
                    //获取订单金额（外币）
                    BigDecimal orderMoneyForeign = neworderEntity.getOrderMoney();
                    //设置订单金额（外币）
                    orderDTO.setOrderMoneyForeign(orderMoneyForeign);
                    //设置订单金额（人民币）
                    orderDTO.setOrderMoney(neworderEntity.getOrderMoneyCny());
                    //设置Amazon佣金（外币）
                    orderDTO.setAmazonCommissionForeign(neworderEntity.getAmazonCommission());
                    //设置Amazon佣金（人民币）
                    orderDTO.setAmazonCommission(neworderEntity.getAmazonCommissionCny());
                    //设置到账金额（外币）
                    orderDTO.setAccountMoneyForeign(neworderEntity.getAccountMoney());
                    //设置到账金额（人民币）
                    orderDTO.setAccountMoney(neworderEntity.getAccountMoneyCny());
                }
            }
        }else{
            //退货
            //获取订单金额（外币）
            BigDecimal orderMoneyForeign = neworderEntity.getOrderMoney();
            orderDTO.setOrderMoneyForeign(orderMoneyForeign);
            orderDTO.setOrderMoney(orderMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //获取Amazon佣金（外币）
            BigDecimal amazonCommissionForeign = neworderEntity.getAmazonCommission();
            orderDTO.setAmazonCommissionForeign(amazonCommissionForeign);
            orderDTO.setAmazonCommission(amazonCommissionForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //到账金额
            BigDecimal accountMoneyForeign = neworderEntity.getAccountMoney();
            orderDTO.setAccountMoneyForeign(accountMoneyForeign);
            orderDTO.setAccountMoney(accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //平台佣金
            BigDecimal platformCommissions = neworderEntity.getPlatformCommissions();
            orderDTO.setPlatformCommissions(platformCommissions);
            //国际运费
            BigDecimal interFreight = neworderEntity.getInterFreight();
            orderDTO.setInterFreight(interFreight);
            //退货费用
            BigDecimal returnCost = neworderEntity.getReturnCost();
            orderDTO.setReturnCost(returnCost);
            //利润
            BigDecimal orderProfit = neworderEntity.getOrderProfit();
            orderDTO.setOrderProfit(orderProfit);
            //利润率
            BigDecimal profitRate = neworderEntity.getProfitRate();
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
     * 删除国内物流
     */
    @RequestMapping("/deleteLogistic")
    public R deleteLogistic(@RequestParam Long domesticLogisticsId){
        DomesticLogisticsEntity logisticsEntity = domesticLogisticsService.selectById(domesticLogisticsId);
        NewOrderEntity neworderEntity = newOrderService.selectById(logisticsEntity.getOrderId());
        domesticLogisticsService.deleteById(domesticLogisticsId);
        List<DomesticLogisticsEntity> list = domesticLogisticsService.selectList(new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",neworderEntity.getOrderId()));
        BigDecimal purchasePrice = new BigDecimal(0.0);
        if(list != null && list.size() > 0){
            for(DomesticLogisticsEntity domestic : list){
                purchasePrice = purchasePrice.add(domestic.getPrice());
            }
        }
        neworderEntity.setPurchasePrice(purchasePrice);
        //已有利润
        if(neworderEntity.getProfitRate().compareTo(new BigDecimal(0.0)) != 0){
            //利润率
            //新利润
            BigDecimal orderProfit = neworderEntity.getOrderProfit().add(logisticsEntity.getPrice());
            neworderEntity.setOrderProfit(orderProfit);
            BigDecimal profitRate = orderProfit.divide(neworderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
            neworderEntity.setProfitRate(profitRate);
            newOrderService.updateById(neworderEntity);
        }
        newOrderService.updateById(neworderEntity);
        RemarkEntity remark = new RemarkEntity();
        remark.setOrderId(neworderEntity.getOrderId());
        remark.setUserName(getUser().getDisplayName());
        remark.setUserId(getUserId());
        remark.setRemark("删除采购信息");
        remark.setType("log");
        remark.setUpdateTime(new Date());
        return R.ok();
    }

    /**
     * 删除国外物流
     */
    @RequestMapping("/deleteLogisticAbroad")
    public R deleteLogisticAbroad(@RequestParam Long abroad_logistics_id){
        NewOrderAbroadLogisticsEntity newOrderAbroadLogisticsEntity = newOrderAbroadLogisticsService.selectById(abroad_logistics_id);
        //云途
        if(newOrderAbroadLogisticsEntity.getPackageType()==0) {
            if (newOrderAbroadLogisticsEntity != null) {
                String type = "1";//云途号删除
                String wayBillNumber = newOrderAbroadLogisticsEntity.getAbroadWaybill();
                Map<String,String> result=newOrderService.DeleteOrder(type,wayBillNumber);
                if("false".equals(result.get("code"))){
                    return R.error("订单删除失败,错误原因：" + result.get("msg"));
                }
                newOrderAbroadLogisticsEntity.setIsDeleted(1);
            }
            return R.ok();
        }else{
            return R.ok();//三态
        }
    }

    /**
     * 打印物流单号
     */
    @RequestMapping("/printLogisticAbroad")
    public R printLogisticAbroad(@RequestParam Long abroad_logistics_id){
        NewOrderAbroadLogisticsEntity newOrderAbroadLogisticsEntity = newOrderAbroadLogisticsService.selectById(abroad_logistics_id);
        //云途
        if(newOrderAbroadLogisticsEntity.getPackageType()==0) {
            if (newOrderAbroadLogisticsEntity != null) {
                String wayBillNumber = newOrderAbroadLogisticsEntity.getAbroadWaybill();
                newOrderService.printOrder(wayBillNumber);
            }
            return R.ok();
        }else{
            return R.ok();//三态
        }
    }

    /**
     * 获取运费详情
     */
    @RequestMapping("/getShippingFeeDetail")
    public R getShippingFeeDetail(@RequestParam Long abroad_logistics_id){
        NewOrderAbroadLogisticsEntity newOrderAbroadLogisticsEntity = newOrderAbroadLogisticsService.selectById(abroad_logistics_id);
        //云途
        if(newOrderAbroadLogisticsEntity.getPackageType()==0) {
            if (newOrderAbroadLogisticsEntity != null) {
                String wayBillNumber = newOrderAbroadLogisticsEntity.getAbroadWaybill();
                Map<String,String> result=newOrderService.getShippingFeeDetail(wayBillNumber);
                if(!"false".equals(result.get("code"))){//获取成功的
                    //追踪号
                    String truckNumber=result.get("TrackingNumber");
                    //运费
                    String Freight=result.get("Freight");
                    //总费用
                    String totalFee=result.get("TotalFee");
                    BigDecimal bigDecimal=new BigDecimal(totalFee);
                    BigDecimal bigDecimal2=new BigDecimal(1.1);
                    newOrderAbroadLogisticsEntity.setTrackWaybill(truckNumber);
                    newOrderAbroadLogisticsEntity.setInterFreight(bigDecimal.multiply(bigDecimal2));
                }
            }
            return R.ok();
        }else{
            return R.ok();//三态
        }
    }


    /**
     * 修改订单状态
     */
    @RequestMapping("/updateState")
    public R updateState(@RequestBody OrderVM orderVM){
        if("仓库已入库".equals(orderVM.getOrderState())){
            //获取可用余额
            SysDeptEntity dept = deptService.selectById(getDeptId());
            if(dept.getAvailableBalance().compareTo(new BigDecimal(500.00)) != 1){
                return R.error("余额不足，请联系公司管理员及时充值后再次尝试");
            }
            //推送订单
//            Map<String,String> result = newOrderService.pushOrder(orderVM.amazonOrderId,orderVM.packageType,orderVM.channelName);
//            if("false".equals(result.get("code"))){
//                return R.error("订单推送失败,错误原因：" + result.get("msg"));
//            }
        }
        boolean flag = newOrderService.updateState(orderVM.getOrderId(),orderVM.getOrderState());
        if(flag){
            //添加操作日志
            RemarkEntity remark = new RemarkEntity();
            remark.setOrderId(orderVM.getOrderId());
            remark.setType("log");
            if("国内物流已采购".equals(orderVM.getOrderState())){
                remark.setRemark("订单国内物流已采购");
            }else if("国内物流已发货".equals(orderVM.getOrderState())){
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
        boolean flag = newOrderService.updateAbnormalState(orderVM.getOrderIds(), orderVM.getAbnormalStatus(), orderVM.getAbnormalState());
        if(flag){
            return R.ok();
        }
        return R.error();
    }


    /**
     * 获取物流专线代码接口
     */
    @RequestMapping("/getShippingMethodCode")
    public R getShippingMethodCode(@RequestBody OrderVM orderVM){
        //先调用获取物流专线代码的方法
        List<String> list=newOrderService.getShippingMethodCode(orderVM.getPackageType());
        if(CollectionUtils.isEmpty(list)){
            return R.error("物流信息不存在");
        }else{
            JSONArray jsonArray = JSONArray.fromObject(list);
            return  R.ok(jsonArray.toString());
        }

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

        String  amazonOrderId=orderVM.getAmazonOrderId();
        //推送订单
        if(orderVM.getPackageType() == 0){
            Map<String,String> result = newOrderService.pushOrder(amazonOrderId,orderVM.getPackageType(),orderVM.getChannelCode(),orderVM.getChineseName(),orderVM.getEnglishName(),orderVM.getLength(),orderVM.getWidth(),orderVM.getHeight(),orderVM.getWeight());
            if("false".equals(result.get("code")) || "0".equals(result.get("Status"))){
                return R.error("订单推送失败,错误原因：" + result.get("msg"));
            }else{
                NewOrderAbroadLogisticsEntity newabroadLogistics = newOrderAbroadLogisticsService.selectOne(new EntityWrapper<NewOrderAbroadLogisticsEntity>().eq("order_id",orderId));
                //生成国际物流单号
                String abroadWaybill = result.get("WayBillNumber");
                //获取国际追踪号
                String track_waybill=result.get("TrackingNumber");
                NewOrderEntity neworder = newOrderService.selectById(orderId);
                //设置状态为虚发货
                neworder.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_SHIPPED);
                neworder.setOrderState("虚发货");
                //进行逻辑判断（走虚发货步骤时判断，新物流则查询旧订单中是否有，有则删除所有关联信息；旧物流则查询新订单中是否有该订单，有则删除所有关联信息。）
                OrderEntity orderEntity=orderService.selectById(orderId);
                if(neworder !=null && orderEntity!=null){
                    List<ProductOrderItemEntity>  productOrderItemEntities=productOrderItemService.selectList(new EntityWrapper<ProductOrderItemEntity>().eq("amazon_order_id",orderEntity.getAmazonOrderId()));
                    if(productOrderItemEntities.size()>0){
                        for(ProductOrderItemEntity productOrderItemEntity:productOrderItemEntities){
                            productOrderItemService.deleteById(productOrderItemEntity.getItemId());
                        }
                    }
                    orderService.deleteById(orderId);
                }
                //设置国际物流单号
                neworder.setAbroadWaybill(abroadWaybill);
                if(newabroadLogistics != null){
                    newabroadLogistics.setAbroadWaybill(abroadWaybill);
                    newabroadLogistics.setTrackWaybill(track_waybill);
                    newabroadLogistics.setLength(orderVM.getLength());
                    newabroadLogistics.setWidth(orderVM.getWidth());
                    newabroadLogistics.setHeight(orderVM.getHeight());
                    newabroadLogistics.setWeight(orderVM.getWeight());
                    newabroadLogistics.setChineseName(orderVM.getChineseName());
                    newabroadLogistics.setEnglishName(orderVM.getEnglishName());
                    newabroadLogistics.setPackageType(orderVM.getPackageType());
                    newabroadLogistics.setChannelName(orderVM.getChannelName());
                    LogisticsChannelEntity logisticsChannelEntity=logisticsChannelService.selectOne(new EntityWrapper<LogisticsChannelEntity>().eq("channel_name",orderVM.getChannelName()));
                    if(logisticsChannelEntity!=null){
                        newabroadLogistics.setChannelCode(logisticsChannelEntity.getChannelCode());//测试用的
                    }
                    newabroadLogistics.setAbroadWaybill(abroadWaybill);
                    newabroadLogistics.setIsSynchronization(2);//表示正在同步中
                    newabroadLogistics.setUpdateTime(new Date());
                    newabroadLogistics.setShipTime(new Date());
                    newOrderAbroadLogisticsService.updateById(newabroadLogistics);
                }else{
                    //生成国际物流对象
                    newabroadLogistics = new NewOrderAbroadLogisticsEntity();
                    newabroadLogistics.setOrderId(orderId);
                    newabroadLogistics.setLength(orderVM.getLength());
                    newabroadLogistics.setWidth(orderVM.getWidth());
                    newabroadLogistics.setHeight(orderVM.getHeight());
                    newabroadLogistics.setWeight(orderVM.getWeight());
                    newabroadLogistics.setChineseName(orderVM.getChineseName());
                    newabroadLogistics.setEnglishName(orderVM.getEnglishName());
                    newabroadLogistics.setPackageType(orderVM.getPackageType());
                    newabroadLogistics.setChannelName(orderVM.getChannelName());
                    LogisticsChannelEntity logisticsChannelEntity=logisticsChannelService.selectOne(new EntityWrapper<LogisticsChannelEntity>().eq("channel_name",orderVM.getChannelName()));
                    if(logisticsChannelEntity!=null){
                        newabroadLogistics.setChannelCode(logisticsChannelEntity.getChannelCode());//测试用的
                    }
                    newabroadLogistics.setAbroadWaybill(abroadWaybill);
                    newabroadLogistics.setTrackWaybill(track_waybill);
                    newabroadLogistics.setIsSynchronization(2);//表示正在同步中
                    newabroadLogistics.setCreateTime(new Date());
                    newabroadLogistics.setUpdateTime(new Date());
                    newabroadLogistics.setShipTime(new Date());

                    newOrderAbroadLogisticsService.insert(newabroadLogistics);
                }
                newOrderService.updateById(neworder);
                //准备订单国际物流上传信息模型
                SendDataMoedl sendDataMoedl = synchronizationZhenModel(neworder,newabroadLogistics,"Yun Express");
                // 将运单号同步到亚马逊平台
                newOrderService.amazonUpdateLogistics(sendDataMoedl,orderId);
                return R.ok().put("newabroadLogistics",newabroadLogistics);
            }
        }else{
            // TODO: 2019/3/31 三态推送
            return R.ok();
        }



    }

    /**
     * 真实发货信息 ——封装物流信息
     * 后置：上传数据到亚马逊
     * @param neworderEntity
     * @param newabroadLogisticsEntity
     */
    private SendDataMoedl synchronizationZhenModel(NewOrderEntity neworderEntity, NewOrderAbroadLogisticsEntity newabroadLogisticsEntity,String carrierName){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String amazonOrderId = neworderEntity.getAmazonOrderId();
        String trackWaybill = newabroadLogisticsEntity.getTrackWaybill();
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
        List<NewOrderItemEntity> productOrderItemEntities=newOrderItemService.selectList(new EntityWrapper<NewOrderItemEntity>().eq("amazon_order_id",amazonOrderId));
        for (NewOrderItemEntity productOrderItemEntity:productOrderItemEntities) {
            Message message=new Message();//如果要确认多个订单可以增加多个<message>
            message.setMessageID(String.valueOf(count++));
            OrderFulfillment orderful=new OrderFulfillment();
            orderful.setAmazonOrderID(amazonOrderId);
            orderful.setFulfillmentDate(shipDate);
            FulfillmentData fd=new FulfillmentData();
            fd.setCarrierName(carrierName);
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
     * 手动更新订单状态
     * orderVM: orderId,
     */
    /*@RequestMapping("/manualUpdateOrder")
    public R manualUpdateOrder(@RequestBody OrderVM orderVM){
        Long orderId = orderVM.getOrderId();

        NewOrderEntity neworderEntity = newOrderService.selectById(orderId);
        String status = neworderEntity.getOrderStatus();
        String abStatus = neworderEntity.getAbnormalStatus();
        String amazonOrderId = neworderEntity.getAmazonOrderId();
        if(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_PENDING.equals(status) || ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_UNSHIPPED.equals(status)){
            OrderModel orderModel = newOrderService.updateOrderAmazonStatus(amazonOrderId,neworderEntity);
            if(orderModel != null){
                //amazon状态更新
//                new RefreshAmazonStateThread(orderEntity,orderModel).start();
                //调用启动线程池的方法
                RefreshAmazonState(neworderEntity,orderModel);
            }
        }else{
            //不为取消订单时执行
            if(!ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_CANCELED.equals(status) && !ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_FINISH.equals(status) && !ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_RETURN.equals(abStatus)){
                //国际物流更新
//                new RefreshOrderThread(orderId).start();
                RefreshOrder(orderId);
            }
        }
        return R.ok();
    }*/

    @Async("taskExecutor")
    public void RefreshAmazonState(NewOrderEntity neworderEntity,OrderModel orderModel){
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
                NewOrderItemEntity productOrderItemEntity = newOrderItemService.selectOne(new EntityWrapper<NewOrderItemEntity>().eq("order_item_id", orderItemModel.getOrderItemId()));
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
                newOrderItemService.updateById(productOrderItemEntity);
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
        neworderEntity.setCountryCode(orderModel.getCountry());
        //设置汇率
        BigDecimal rate = new BigDecimal(0.00);
        String rateCode = orderModel.getCurrencyCode();
        if(StringUtils.isNotBlank(rateCode)){
            rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code",rateCode)).getRate();
        }
        BigDecimal orderMoney = orderModel.getOrderMoney();
        if(orderMoney.compareTo(new BigDecimal("0.00")) != 0){
            neworderEntity.setOrderMoney(orderMoney);
            neworderEntity.setOrderMoneyCny(orderMoney.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //获取Amazon佣金（外币）
            BigDecimal amazonCommission = orderMoney.multiply(new BigDecimal(0.15).setScale(2,BigDecimal.ROUND_HALF_UP));
            neworderEntity.setAmazonCommission(amazonCommission);
            neworderEntity.setAmazonCommissionCny(amazonCommission.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
            //到账金额
            BigDecimal accountMoney = orderMoney.subtract(amazonCommission);
            neworderEntity.setAccountMoney(accountMoney);
            neworderEntity.setAccountMoneyCny(accountMoney.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        //获取状态判断是否为取消
        if(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_CANCELED.equals(modelStatus)){
            neworderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_CANCELED);
            neworderEntity.setOrderState("取消");
        }else{
            String orderStatus = neworderEntity.getOrderStatus();
            //获取当前订单状态判断是否为待付款、已付款、虚发货
            List amazonStateList = Arrays.asList(ConstantDictionary.OrderStateCode.AMAZON_ORDER_STATE);
            if(amazonStateList.contains(orderStatus)){
                //获取返回状态判断是否为待付款、已付款、虚发货
                if(amazonStateList.contains(modelStatus)){
                    //判断两个状态不想等时更改状态
                    if(!modelStatus.equals(orderStatus)){
                        neworderEntity.setOrderStatus(modelStatus);
                        String orderState = dataDictionaryService.selectOne(
                                new EntityWrapper<DataDictionaryEntity>()
                                        .eq("data_type","AMAZON_ORDER_STATE")
                                        .eq("data_number",modelStatus)
                        ).getDataContent();

                        neworderEntity.setOrderState(orderState);
                        newOrderService.updateById(neworderEntity);
                        //新增/修改收货人信息
                        ProductShipAddressEntity productShipAddressEntity = orderModel.getProductShipAddressEntity();
                        if(productShipAddressEntity != null){//判断返回值是否有收件人信息
                            ProductShipAddressEntity shipAddress = productShipAddressService.selectOne(
                                    new EntityWrapper<ProductShipAddressEntity>().eq("order_id",neworderEntity.getOrderId())
                            );
                            if(shipAddress == null){
                                productShipAddressEntity.setOrderId(neworderEntity.getOrderId());
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

 /*   @Async("taskExecutor")
    public void RefreshOrder(Long orderId){
        //订单对象
        NewOrderEntity neworderEntity = newOrderService.selectById(orderId);
        //国际物流对象
        NewOrderAbroadLogisticsEntity newabroadLogisticsEntity = newOrderAbroadLogisticsService.selectOne(new EntityWrapper<NewOrderAbroadLogisticsEntity>().eq("order_id",orderId));
        if(newabroadLogisticsEntity == null){
            newabroadLogisticsEntity = new NewOrderAbroadLogisticsEntity();
        }
        String amazonOrderId = neworderEntity.getAmazonOrderId();
        String abnormalStatus = neworderEntity.getAbnormalStatus();
        String orderStatus = neworderEntity.getOrderStatus();
//            BigDecimal momentRate = orderEntity.getMomentRate();
        //不属于退货
        if(!ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_RETURN.equals(abnormalStatus) || neworderEntity.getInterFreight().compareTo(new BigDecimal(0.00)) == 0){
            Map<String,Object> map = AbroadLogisticsUtil.getOrderDetail(amazonOrderId);
            if("true".equals(map.get("code"))){
                neworderEntity.setUpdateTime(new Date());
                ReceiveOofayData receiveOofayData = (ReceiveOofayData)map.get("receiveOofayData");
                //如果订单状态在物流仓库未签收和仓库已入库时，更新订单的国际物流信息
                if( ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_WAREHOUSING.equals(orderStatus)){
                    int status = 0;
                    //状态转变为仓库已入库
                    if(receiveOofayData.isWarehousing ){
                        neworderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_WAREHOUSING);
                        neworderEntity.setOrderState("仓库已入库");
                        newabroadLogisticsEntity.setState("仓库已入库");
                    }else{
                        if(StringUtils.isNotBlank(receiveOofayData.getStatusStr())){
                            status = Integer.parseInt(receiveOofayData.getStatusStr());
                            if(status == 3){
                                newabroadLogisticsEntity.setState("出库");
                                //国际已发货
                                newOrderService.internationalShipments(neworderEntity);
                            }
                        }
                    }
                    newabroadLogisticsEntity.setUpdateTime(new Date());
                }
                //设置国际物流公司
                if(StringUtils.isNotBlank(receiveOofayData.getDestTransportCompany())){
                    System.out.println("物流公司：" + receiveOofayData.getDestTransportCompany());
                    newabroadLogisticsEntity.setDestTransportCompany(receiveOofayData.getDestTransportCompany());
                    //设置国际物流渠道
                    if(StringUtils.isNotBlank(receiveOofayData.getDestChannel())){
                        newabroadLogisticsEntity.setDestChannel(receiveOofayData.getDestChannel());
                    }else{
                        newabroadLogisticsEntity.setDestChannel("Standard");
                    }
                }
                //设置国内跟踪号
                if(StringUtils.isNotBlank(receiveOofayData.getDomesticTrackWaybill())){
                    newabroadLogisticsEntity.setDomesticTrackWaybill(receiveOofayData.getDomesticTrackWaybill());
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
                    newabroadLogisticsEntity.setShipTime(shipTime);
                }
                //设置实际重量
                if(StringUtils.isNotBlank(receiveOofayData.getActualWeight()) && !"0.0".equals(receiveOofayData.getActualWeight())){
                    newabroadLogisticsEntity.setActualWeight(receiveOofayData.getActualWeight());
                }
                //设置目的地查询网址
                if(StringUtils.isNotBlank(receiveOofayData.getDestQueryUrl())){
                    newabroadLogisticsEntity.setDestQueryUrl(receiveOofayData.getDestQueryUrl());
                }
                //设置服务查询网址
                if(StringUtils.isNotBlank(receiveOofayData.getServiceQueryUrl())){
                    newabroadLogisticsEntity.setServiceQueryUrl(receiveOofayData.getServiceQueryUrl());
                }
                //设置联系电话
                if(StringUtils.isNotBlank(receiveOofayData.getMobile())){
                    newabroadLogisticsEntity.setMobile(receiveOofayData.getMobile());
                }
                //有运费
                if(StringUtils.isNotBlank(receiveOofayData.getInterFreight()) && new BigDecimal(receiveOofayData.getInterFreight()).compareTo(new BigDecimal(0.00)) == 1 && neworderEntity.getInterFreight().compareTo(new BigDecimal(0.00)) == 0){
                    //计算国际运费、平台佣金、利润
                    //国际运费
                    BigDecimal interFreight = new BigDecimal(receiveOofayData.getInterFreight());
                    newabroadLogisticsEntity.setInterFreight(interFreight);
                    neworderEntity.setInterFreight(interFreight);
                    //到账金额(人民币)
                    BigDecimal accountMoney = neworderEntity.getAccountMoneyCny();
                    //平台佣金
                    BigDecimal companyPoint = deptService.selectById(neworderEntity.getDeptId()).getCompanyPoints();
                    BigDecimal platformCommissions = accountMoney.multiply(companyPoint).setScale(2,BigDecimal.ROUND_HALF_UP);
                    neworderEntity.setPlatformCommissions(platformCommissions);
                    //利润 到账-国际运费-采购价-平台佣金
                    BigDecimal orderProfit = accountMoney.subtract(neworderEntity.getPurchasePrice()).subtract(interFreight).subtract(platformCommissions).setScale(2,BigDecimal.ROUND_HALF_UP);
                    neworderEntity.setOrderProfit(orderProfit);
                    //利润率
                    BigDecimal profitRate = orderProfit.divide(neworderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
                    neworderEntity.setProfitRate(profitRate);
                    newOrderService.deduction(neworderEntity);
                }
                //判断状态为国际已发货或已完成
                else if(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_INTLSHIPPED.equals(orderStatus) || ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_FINISH.equals(orderStatus)){
                    //判断订单数据中国际运费是否为空
                    if(neworderEntity.getInterFreight().compareTo(new BigDecimal(0.00)) == 0){
                        neworderEntity.setUpdateTime(new Date());
                        //有运费

                        if(StringUtils.isNotBlank(receiveOofayData.getInterFreight()) && new BigDecimal(receiveOofayData.getInterFreight()).compareTo(new BigDecimal(0.00)) == 1){
                            //计算国际运费、平台佣金、利润
                            //国际运费
                            BigDecimal interFreight = new BigDecimal(receiveOofayData.getInterFreight());
                            newabroadLogisticsEntity.setInterFreight(interFreight);
                            neworderEntity.setInterFreight(interFreight);
                            //到账金额(人民币)
                            BigDecimal accountMoney = neworderEntity.getAccountMoneyCny();
                            //平台佣金
                            BigDecimal companyPoint = deptService.selectById(neworderEntity.getDeptId()).getCompanyPoints();
                            BigDecimal platformCommissions = accountMoney.multiply(companyPoint).setScale(2,BigDecimal.ROUND_HALF_UP);
                            neworderEntity.setPlatformCommissions(platformCommissions);
                            //利润 到账-国际运费-采购价-平台佣金
                            BigDecimal orderProfit = accountMoney.subtract(neworderEntity.getPurchasePrice()).subtract(interFreight).subtract(platformCommissions).setScale(2,BigDecimal.ROUND_HALF_UP);
                            neworderEntity.setOrderProfit(orderProfit);
                            //利润率
                            BigDecimal profitRate = orderProfit.divide(neworderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
                            neworderEntity.setProfitRate(profitRate);
                            newOrderService.deduction(neworderEntity);
                        }
                    }

                    //设置转单号
                    if(StringUtils.isBlank(newabroadLogisticsEntity.getTrackWaybill())){
                        Map<String,String> trackWaybillMap = AbroadLogisticsUtil.getTrackWaybill(amazonOrderId);
                        if("true".equals(trackWaybillMap.get("code"))){
                            String trackWaybill = trackWaybillMap.get("trackWaybill");
                            if(StringUtils.isNotBlank(trackWaybill)){
                                newabroadLogisticsEntity.setTrackWaybill(trackWaybill);
                                newabroadLogisticsEntity.setIsSynchronization(0);
                            }
                        }
                    }
                }
                if(newabroadLogisticsEntity.getOrderId() == null){
                    newabroadLogisticsEntity.setOrderId(orderId);
                    newOrderAbroadLogisticsService.insert(newabroadLogisticsEntity);
                }else{
                    newOrderAbroadLogisticsService.updateById(newabroadLogisticsEntity);
                }
                newOrderService.insertOrUpdate(neworderEntity);
                //同步转单号
                if(StringUtils.isNotBlank(newabroadLogisticsEntity.getTrackWaybill()) && newabroadLogisticsEntity.getIsSynchronization() == 0){
                    SendDataMoedl sendDataMoedl = synchronizationZhenModel(neworderEntity,newabroadLogisticsEntity);
                    amazonUpdateLogistics(sendDataMoedl,orderId);
                }
            }
        }
    }*/

    /**
     * 上传国际物流信息到amazon
     * @param sendDataMoedl
     */
    public void amazonUpdateLogistics(SendDataMoedl sendDataMoedl, Long orderId) {
        List<Shipping> list = sendDataMoedl.getList();
        List<String> serviceURL = sendDataMoedl.getServiceURL();
        List<String> marketplaceIds = sendDataMoedl.getMarketplaceIds();
        String sellerId = sendDataMoedl.getSellerId();
        String mwsAuthToken = sendDataMoedl.getMwsAuthToken();
        //获得授权表里的region字段的值，判断其逻辑
        AmazonGrantEntity amazonGrantEntity = amazonGrantService.selectOne(new EntityWrapper<AmazonGrantEntity>().eq("merchant_id", sellerId).eq("grant_token", mwsAuthToken));
        String accessKey = null;
        String secretKey = null;
        if (amazonGrantEntity != null) {
            int region = amazonGrantEntity.getRegion();
            if (region == 0) {//北美
                accessKey = naAccessKey;
                secretKey = naSecretKey;
            } else if (region == 1) {//欧洲
                accessKey = euAccessKey;
                secretKey = euSecretKey;
            } else if (region == 2) {//日本
                accessKey = jpAccessKey;
                secretKey = jpSecretKey;
            } else if (region == 3) {//澳大利亚
                accessKey = auAccessKey;
                secretKey = auSecretKey;
            }
        }

        /**
         * 根据List数组，生成XML数据
         */
        String resultXml = XmlUtils.getXmlFromList(list);
        //打印生成xml数据
        FileWriter outdata = null;
//        FileUtil.generateFilePath(fileStoragePath,"shipping",orderId);
        String filePath = FileUtil.generateFilePath(fileStoragePath, "shipping", orderId);
        String feedType = "_POST_ORDER_FULFILLMENT_DATA_";
        try {
            outdata = new FileWriter(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter outfile = new PrintWriter(outdata);
        outfile.println(resultXml);// 输出String
        outfile.flush();// 输出缓冲区的数据
        outfile.close();
//         List<Object> responseList =listOrdersAsyncService.invokeListOrders(client,requestList);
        //进行数据上传(步骤一)
        String feedSubmissionId = submitLogisticsService.submitFeed(serviceURL.get(0), sellerId, mwsAuthToken, feedType, filePath, accessKey, secretKey);
        //进行数据上传(步骤二)
        List<String> feedSubmissionIds = submitLogisticsService.getFeedSubmissionList(serviceURL.get(0), sellerId, mwsAuthToken, feedSubmissionId, accessKey, secretKey);
        System.out.println("==========================" + feedSubmissionIds.get(0) + "=============================");
        if (feedSubmissionIds.size() > 0 && feedSubmissionIds != null) {
            //进行数据上传(步骤三)
            submitLogisticsService.getFeedSubmissionResult(serviceURL.get(0), sellerId, mwsAuthToken, feedSubmissionIds.get(0), accessKey, secretKey);
            NewOrderAbroadLogisticsEntity newabroadLogisticsEntity = newOrderAbroadLogisticsService.selectOne(new EntityWrapper<NewOrderAbroadLogisticsEntity>().eq("order_id", orderId));
            String amazonOrderId = newOrderService.selectById(orderId).getAmazonOrderId();
            //从后代调用接口获取亚马逊后台的订单状态
            String orderStatus = "";
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
            List<Object> responseList = invokeGetOrder(client, requestList);
            Boolean isSuccess = false;
            GetOrderResponse getOrderResponse = null;
            for (Object tempResponse : responseList) {
                // Object 转换 ListOrdersResponse 还是 MarketplaceWebServiceOrdersException
                String className = tempResponse.getClass().getName();
                if ((GetOrderResponse.class.getName()).equals(className) == true) {
                    System.out.println("responseList 类型是 GetOrderResponse。");
                    GetOrderResponse response = (GetOrderResponse) tempResponse;
                    System.out.println(response.toXML());
                    orderStatus = response.toXML();
                    if (orderStatus.contains("<OrderStatus>")) {
                        orderStatus = orderStatus.substring(orderStatus.indexOf("<OrderStatus>"), orderStatus.indexOf("</OrderStatus>")).replace("<OrderStatus>", "");
                    }
                    isSuccess = true;
                } else {
                    System.out.println("responseList 类型是 MarketplaceWebServiceOrderException。");
                    isSuccess = false;
                    continue;
                }
            }

            //判读亚马逊后台订单的状态
            if ("Shipped".equals(orderStatus)) {
                newabroadLogisticsEntity.setIsSynchronization(1);//表示同步成功
                newOrderAbroadLogisticsService.updateById(newabroadLogisticsEntity);
            } else {
                logger.error("同步失败,请重新上传订单...");
                newabroadLogisticsEntity.setIsSynchronization(0);//表示同步失败
                newOrderAbroadLogisticsService.updateById(newabroadLogisticsEntity);
            }

        }

    }

}