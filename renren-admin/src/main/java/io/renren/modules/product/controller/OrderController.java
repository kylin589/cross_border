package io.renren.modules.product.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.logistics.DTO.ReceiveOofayData;
import io.renren.modules.logistics.entity.AbroadLogisticsEntity;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.service.AbroadLogisticsService;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.logistics.util.AbroadLogisticsUtil;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        if(productsEntity != null){
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
        orderService.updateById(order);
        //生成国际物流对象
        AbroadLogisticsEntity abroadLogistics = new AbroadLogisticsEntity();
        abroadLogistics = new AbroadLogisticsEntity();
        abroadLogistics.setOrderId(orderId);
        abroadLogistics.setAbroadWaybill(abroadWaybill);
        abroadLogistics.setIsSynchronization(0);
        abroadLogistics.setCreateTime(new Date());
        abroadLogistics.setUpdateTime(new Date());
        abroadLogisticsService.insert(abroadLogistics);
        // TODO: 2018/12/18 将运单号同步到亚马逊平台
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
            OrderModel orderModel = orderService.updateOrderAmazonStatus(amazonOrderId);
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
     * 手动刷新订单时调用
     * 状态不为亚马逊状态时
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
                        //设置国际物流跟踪号
                        if(StringUtils.isBlank(receiveOofayData.getTrackWaybill())){
                            abroadLogisticsEntity.setTrackWaybill(receiveOofayData.getTrackWaybill());
                            NoticeEntity noticeEntity = new NoticeEntity();
                            noticeEntity.setCreateTime(new Date());
                            noticeEntity.setNoticeContent("订单编号：" + orderId + "的物流跟踪号以生成，请尽快同步。");
                            noticeEntity.setUserId(orderEntity.getUserId());
                            noticeEntity.setDeptId(orderEntity.getDeptId());
                            noticeService.insert(noticeEntity);
                        }else if(!abroadLogisticsEntity.getTrackWaybill().equals(receiveOofayData.getTrackWaybill())){
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
                            //平台佣金
                            BigDecimal accountMoneyForeign = orderEntity.getAccountMoney();
                            BigDecimal accountMoney = accountMoneyForeign.multiply(momentRate).setScale(2,BigDecimal.ROUND_HALF_UP);
                            BigDecimal companyPoint = deptService.selectById(orderEntity.getDeptId()).getCompanyPoints();
                            BigDecimal platformCommissions = accountMoney.multiply(companyPoint).setScale(2,BigDecimal.ROUND_HALF_UP);
                            orderEntity.setPlatformCommissions(platformCommissions);
                            //利润 到账-国际运费-采购价-平台佣金
                            BigDecimal orderProfit = accountMoneyForeign.multiply(momentRate).subtract(orderEntity.getPurchasePrice()).subtract(interFreight).setScale(2,BigDecimal.ROUND_HALF_UP);
                            orderEntity.setOrderProfit(orderProfit);
                            orderService.updateById(orderEntity);
                        }
                        //状态转变为入库
                        if(receiveOofayData.isWarehousing && ConstantDictionary.OrderStateCode.ORDER_STATE_WAITINGRECEIPT.equals(orderStatus)){
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_WAREHOUSING);
                            orderEntity.setOrderState("入库");
                            abroadLogisticsEntity.setState("入库");
                        }else{
                            if(receiveOofayData.getStatusStr() != null && receiveOofayData.getStatusStr() != ""){
                                status = Integer.parseInt(receiveOofayData.getStatusStr());
                                if(status == 2){
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
