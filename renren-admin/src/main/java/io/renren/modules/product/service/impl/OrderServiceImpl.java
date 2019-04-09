package io.renren.modules.product.service.impl;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.amazon.dto.ListOrderItemsByNextTokenResponseDto;
import io.renren.modules.amazon.dto.ListOrdersResponseDto;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.amazon.util.FileUtil;
import io.renren.modules.logistics.DTO.*;
import io.renren.modules.logistics.entity.*;
import io.renren.modules.logistics.entity.Message;
import io.renren.modules.logistics.service.*;
import io.renren.modules.logistics.util.AbroadLogisticsUtil;
import io.renren.modules.logistics.util.NewAbroadLogisticsUtil;
import io.renren.modules.logistics.util.XmlUtils;
import io.renren.modules.order.component.OrderTimer;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.NewProductShipAddressService;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.product.dao.OrderDao;
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
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
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

import static com.amazonservices.mws.orders._2013_09_01.samples.ListOrderItemsAsyncSample.invokeListOrderItems;
import static io.renren.modules.amazon.util.XMLUtil.analysisListOrderItemsByNextTokenResponse;
import static io.renren.modules.amazon.util.XMLUtil.analysisListOrdersResponse;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
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
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    @Lazy
    private OrderService orderService;
    @Autowired
    private NewOrderService newOrderService;
    @Autowired
    private LogisticsChannelService logisticsChannelService;
    @Autowired
    private AbroadLogisticsService abroadLogisticsService;
    @Autowired
    private NewOrderAbroadLogisticsService newOrderAbroadLogisticsService;
    @Autowired
    private AmazonRateService amazonRateService;
    @Autowired
    private ProductShipAddressService productShipAddressService;
    @Autowired
    private NewProductShipAddressService newProductShipAddressService;
    @Autowired
    private ProductOrderItemService productOrderItemService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ConsumeService consumeService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private SubmitLogisticsService submitLogisticsService;
    @Autowired
    private AmazonGrantService amazonGrantService;
    @Autowired
    private AmazonGrantShopService amazonGrantShopService;
    @Autowired
    private NewOrderItemService newOrderItemService;
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
                .eq("user_id",userId)
                .eq("is_old",0)
                .orderBy("buy_date",false);

        Page<OrderEntity> page = this.selectPage(
                new Query<OrderEntity>(params).getPage(),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        Map<String, Object> condition = new HashMap<>(2);
        condition.put("userId",userId);
        condition.put("isOld",0);
        OrderStatisticsEntity orderCounts = baseMapper.statisticsOrderCounts(condition);

        if(orderCounts == null){
            orderCounts = new OrderStatisticsEntity();
        }
        //订单数
        int orderCount = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("is_old",0));
        orderCounts.setOrderCount(orderCount);
        //核算订单数
        int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("is_old",0).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
        orderCounts.setOrderCounts(completeCounts);
        //总金额
        String orderMoney = baseMapper.salesVolumeStatistics(condition);
        if(StringUtils.isNotBlank(orderMoney)){
            orderCounts.setOrderMoney(new BigDecimal(orderMoney));
        }
        //退货数
        int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("is_old",0).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
        orderCounts.setReturnCounts(returnCounts);
        Map<String, Object> map = new HashMap<>(2);
        map.put("page",pageUtils);
        map.put("orderCounts",orderCounts);
        return map;
    }
    @Override
    public Map<String, Object> queryOldMyPage(Map<String, Object> params, Long userId) {
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
                .eq("user_id",userId)
                .eq("is_old",1)
                .orderBy("buy_date",false);

        Page<OrderEntity> page = this.selectPage(
                new Query<OrderEntity>(params).getPage(),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        Map<String, Object> condition = new HashMap<>(2);
        condition.put("userId",userId);
        condition.put("isOld",1);
        OrderStatisticsEntity orderCounts = baseMapper.statisticsOrderCounts(condition);
        if(orderCounts == null){
            orderCounts = new OrderStatisticsEntity();
        }
        //订单数
        int orderCount = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("is_old",1));
        orderCounts.setOrderCount(orderCount);
        //核算订单数
        int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("is_old",1).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
        orderCounts.setOrderCounts(completeCounts);
        //总金额
        String orderMoney = baseMapper.salesVolumeStatistics(condition);
        if(StringUtils.isNotBlank(orderMoney)){
            orderCounts.setOrderMoney(new BigDecimal(orderMoney));
        }
        //退货数
        int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("user_id",userId).eq("is_old",1).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
        orderCounts.setReturnCounts(returnCounts);
        Map<String, Object> map = new HashMap<>(2);
        map.put("page",pageUtils);
        map.put("orderCounts",orderCounts);
        return map;
    }
    @Override
    public Map<String, Object> queryOldAllPage(Map<String, Object> params, Long deptId) {
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
                    .eq(StringUtils.isNotBlank(qDeptId),"dept_id",qDeptId)
                    .eq("is_old",1)
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
                    .eq("is_old",1)
                    .orderBy("buy_date",false);
        }


        Page<OrderEntity> page = this.selectPage(
                new Query<OrderEntity>(params).getPage(),
                wrapper
        );
        List<OrderEntity> orderEntityList = page.getRecords();
        for(OrderEntity orderEntity : orderEntityList){
            SysUserEntity sysUserEntity = userService.selectById(orderEntity.getUserId());
            SysDeptEntity sysDeptEntity = deptService.selectById(orderEntity.getDeptId());
            if(sysDeptEntity != null){
                orderEntity.setDeptName(sysDeptEntity.getName());
            }
            if(sysUserEntity != null){
                orderEntity.setUserName(sysUserEntity.getDisplayName());
            }
        }
        PageUtils pageUtils = new PageUtils(page);
        OrderStatisticsEntity orderCounts = new OrderStatisticsEntity();
        Map<String, Object> condition = new HashMap<>(2);
        condition.put("isOld",1);
        if(deptId == 1L){
            //总公司
            if(baseMapper.statisticsOrderCounts(condition) != null){
                orderCounts = baseMapper.statisticsOrderCounts(condition);
            }
            //订单数
            int orderCount = this.selectCount(new EntityWrapper<OrderEntity>().eq("is_old",1));
            orderCounts.setOrderCount(orderCount);
            //核算订单数
            int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("is_old",1).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
            orderCounts.setOrderCounts(completeCounts);
            //总金额
            String orderMoney = baseMapper.salesVolumeStatistics(condition);
            if(StringUtils.isNotBlank(orderMoney)){
                orderCounts.setOrderMoney(new BigDecimal(orderMoney));
            }
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("is_old",1).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
            orderCounts.setReturnCounts(returnCounts);
        }else{
            //加盟商
            condition.put("deptId",deptId);
            if(baseMapper.statisticsOrderCounts(condition) != null){
                orderCounts = baseMapper.statisticsOrderCounts(condition);
            }
            //订单数
            int orderCount = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("is_old",1));
            orderCounts.setOrderCount(orderCount);
            //核算订单数
            int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("is_old",1).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
            orderCounts.setOrderCounts(completeCounts);
            //总金额
            String orderMoney = baseMapper.salesVolumeStatistics(condition);
            if(StringUtils.isNotBlank(orderMoney)){
                orderCounts.setOrderMoney(new BigDecimal(orderMoney));
            }
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("is_old",1).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
            orderCounts.setReturnCounts(returnCounts);
        }

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
        EntityWrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
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
                    .eq("is_old",0)
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
                    .eq("is_old",0)
                    .orderBy("buy_date",false);
        }


        Page<OrderEntity> page = this.selectPage(
                new Query<OrderEntity>(params).getPage(),
                wrapper
        );
        List<OrderEntity> orderEntityList = page.getRecords();
        for(OrderEntity orderEntity : orderEntityList){
            SysUserEntity sysUserEntity = userService.selectById(orderEntity.getUserId());
            SysDeptEntity sysDeptEntity = deptService.selectById(orderEntity.getDeptId());
            if(sysDeptEntity != null){
                orderEntity.setDeptName(sysDeptEntity.getName());
            }
            if(sysUserEntity != null){
                orderEntity.setUserName(sysUserEntity.getDisplayName());
            }
        }
        PageUtils pageUtils = new PageUtils(page);
        OrderStatisticsEntity orderCounts = new OrderStatisticsEntity();
        Map<String, Object> condition = new HashMap<>(2);
        condition.put("isOld",0);
        if(deptId == 1L){
            //总公司
            if(baseMapper.statisticsOrderCounts(condition) != null){
                orderCounts = baseMapper.statisticsOrderCounts(condition);
            }
            //订单数
            int orderCount = this.selectCount(new EntityWrapper<OrderEntity>().eq("is_old",0));
            orderCounts.setOrderCount(orderCount);
            //核算订单数
            int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("is_old",0).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
            orderCounts.setOrderCounts(completeCounts);
            //总金额
            String orderMoney = baseMapper.salesVolumeStatistics(condition);
            if(StringUtils.isNotBlank(orderMoney)){
                orderCounts.setOrderMoney(new BigDecimal(orderMoney));
            }
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("is_old",0).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
            orderCounts.setReturnCounts(returnCounts);
        }else{
            //加盟商
            condition.put("deptId",deptId);
            if(baseMapper.statisticsOrderCounts(condition) != null){
                orderCounts = baseMapper.statisticsOrderCounts(condition);
            }
            //订单数
            int orderCount = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("is_old",0));
            orderCounts.setOrderCount(orderCount);
            //核算订单数
            int completeCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("is_old",0).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
            orderCounts.setOrderCounts(completeCounts);
            //总金额
            String orderMoney = baseMapper.salesVolumeStatistics(condition);
            if(StringUtils.isNotBlank(orderMoney)){
                orderCounts.setOrderMoney(new BigDecimal(orderMoney));
            }
            //退货数
            int returnCounts = this.selectCount(new EntityWrapper<OrderEntity>().eq("dept_id",deptId).eq("is_old",0).andNew().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_FINISH).or().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED));
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
        omsOrder.setOrder_sn(orderEntity.getAmazonOrderId());
        List<DomesticLogisticsEntity> domesticLogisticsEntitys = domesticLogisticsService.selectList(new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",orderId));
        StringBuffer deanname = new StringBuffer("");
        StringBuffer supplyexpressno = new StringBuffer("");
        for(int i = 0; i < domesticLogisticsEntitys.size(); i++){
            if(StringUtils.isNotBlank(domesticLogisticsEntitys.get(i).getLogisticsCompany())){
                deanname.append(domesticLogisticsEntitys.get(i).getLogisticsCompany());
                deanname.append(",");
            }
        }
        if(StringUtils.isNotBlank(deanname.toString())){
            omsOrder.setDelivery_deanname(deanname.substring(0,deanname.length()-1));
        }
        omsOrder.setOrder_currency(orderEntity.getRateCode());
        //设置时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS+08:00");
        omsOrder.setOrder_date(sdf.format(orderEntity.getBuyDate()));
        omsOrder.setOrder_memo(shipAddressEntity.getShipCountry());
        //推送--订单详情

        List<OmsOrderDetail> omsOrderDetails = new ArrayList<>();
        List<ProductOrderItemEntity> productOrderItemEntitys=productOrderItemService.selectList(new EntityWrapper<ProductOrderItemEntity>().eq("amazon_order_id",orderEntity.getAmazonOrderId()));
        List<Image> images=new ArrayList<>();
        for(ProductOrderItemEntity productOrderItemEntity:productOrderItemEntitys){
            OmsOrderDetail omsOrderDetail=new OmsOrderDetail();
            omsOrderDetail.setProduct_id(productOrderItemEntity.getProductSku());
            omsOrderDetail.setQuantity(productOrderItemEntity.getOrderItemNumber());
            DomesticLogisticsEntity domesticLogistics = domesticLogisticsService.selectOne(
                    new EntityWrapper<DomesticLogisticsEntity>().eq("order_id",orderId).eq("item_id",productOrderItemEntity.getItemId())
            );
            omsOrderDetail.setSupplyexpressno(domesticLogistics.getWaybill());
            omsOrderDetails.add(omsOrderDetail);
            Image image = new Image();
            image.setSellersku(productOrderItemEntity.getProductSku());
            image.setTitle(productOrderItemEntity.getProductTitle());
            image.setPic(productOrderItemEntity.getProductImageUrl());
            images.add(image);
        }

        //推送—收货人信息
        OmsShippingAddr omsShippingAddr = new OmsShippingAddr();
        omsShippingAddr.setAddress_line1(shipAddressEntity.getShipAddressLine1());
        omsShippingAddr.setAddress_line2(shipAddressEntity.getShipAddressLine2());
        omsShippingAddr.setAddress_line3(shipAddressEntity.getShipAddressLine3());
        omsShippingAddr.setCustaddress(shipAddressEntity.getShipAddressDetail());
        omsShippingAddr.setCustcity(shipAddressEntity.getShipCity());
        omsShippingAddr.setCustcountry(shipAddressEntity.getShipCountry());
        omsShippingAddr.setCustomer(shipAddressEntity.getShipName());
        omsShippingAddr.setCustcompany(shipAddressEntity.getShipName());
        omsShippingAddr.setCustphone(shipAddressEntity.getShipTel());
        omsShippingAddr.setCuststate(shipAddressEntity.getShipRegion());
        omsShippingAddr.setCustzipcode(shipAddressEntity.getShipZip());

        JSONObject orderDataJson = new JSONObject();
        JSONObject omsOrderJson = JSONObject.fromObject(omsOrder);
        JSONArray orderDetailListJson = JSONArray.fromObject(omsOrderDetails);
        JSONObject omsShippingAddrJson = JSONObject.fromObject(omsShippingAddr);
        JSONArray imageJson = JSONArray.fromObject(images);
        orderDataJson.put("order",omsOrderJson);
        orderDataJson.put("orderDetailList",orderDetailListJson);
        orderDataJson.put("address",omsShippingAddrJson);
        orderDataJson.put("image",imageJson);
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
                        //利润率
                        BigDecimal profitRate = orderProfit.divide(orderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
                        orderEntity.setProfitRate(profitRate);
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
                        //利润率
                        BigDecimal profitRate = orderProfit.divide(orderEntity.getOrderMoneyCny(),2,BigDecimal.ROUND_HALF_UP);
                        orderEntity.setProfitRate(profitRate);
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


    /**
     * 获取（更新）订单
     * @param orderModelList
     */
    @Override
    public void updateOrder(List<OrderModel> orderModelList) {
        for(OrderModel orderModel : orderModelList ){
            //获取亚马逊订单id 304-6754745-7864352
            String amazonOrderId = orderModel.getAmazonOrderId();
            if(StringUtils.isNotBlank(amazonOrderId)) {
                //判断该订单是否存在
                OrderEntity orderEntity = this.selectOne(new EntityWrapper<OrderEntity>().like("amazon_order_id", amazonOrderId));
                NewOrderEntity neworderEntity = newOrderService.selectOne(new EntityWrapper<NewOrderEntity>().like("amazon_order_id", amazonOrderId));
                //订单状态
                String modelStatus = orderModel.getOrderStatus();
                if (orderEntity == null && neworderEntity==null) {
                    //新增订单
                    if (!"Canceled".equals(modelStatus) && !"Shipped".equals(modelStatus)) {
                        //设置基本属性
                        orderEntity = new OrderEntity();
                        neworderEntity=new NewOrderEntity();
                        orderEntity.setAmazonOrderId(amazonOrderId);
                        neworderEntity.setAmazonOrderId(amazonOrderId);
                        //判断该订单的订单商品列表是否存在
                       List<OrderItemModel> orderItemModels=orderModel.getOrderItemModels();
                       if(orderItemModels!=null && orderItemModels.size()>0){
                           for (OrderItemModel orderItemModel:orderItemModels) {
                                //判断该商品是否存在
                              ProductOrderItemEntity productOrderItemEntity=productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id",orderItemModel.getOrderItemId()));
                              NewOrderItemEntity newOrderItemEntity=newOrderItemService.selectOne(new EntityWrapper<NewOrderItemEntity>().eq("order_item_id",orderItemModel.getOrderItemId()));
                               //如果不存在
                               if(productOrderItemEntity==null && newOrderItemEntity==null){
                                   productOrderItemEntity=new ProductOrderItemEntity();
                                   newOrderItemEntity=new NewOrderItemEntity();
                                   if(orderModel.getAmazonOrderId()!=null){
                                       productOrderItemEntity.setAmazonOrderId(orderItemModel.getAmazonOrderId());
                                       newOrderItemEntity.setAmazonOrderId(orderItemModel.getAmazonOrderId());
                                   }
                                   if(orderItemModel.getOrderItemId()!=null){
                                       productOrderItemEntity.setOrderItemId(orderItemModel.getOrderItemId());
                                       newOrderItemEntity.setOrderItemId(orderItemModel.getOrderItemId());
                                   }
                                   if(orderItemModel.getProductId()!=null){
                                       productOrderItemEntity.setProductId(orderItemModel.getProductId());
                                       newOrderItemEntity.setProductId(orderItemModel.getProductId());
                                   }
                                   if(orderItemModel.getProductTitle()!=null){
                                       productOrderItemEntity.setProductTitle(orderItemModel.getProductTitle());
                                       newOrderItemEntity.setProductTitle(orderItemModel.getProductTitle());
                                   }
                                   String productIdStr = "0";
                                   if(orderItemModel.getProductSku()!=null){
                                       productOrderItemEntity.setProductSku(orderItemModel.getProductSku());
                                       newOrderItemEntity.setProductSku(orderItemModel.getProductSku());
                                       String[] skuArray = orderItemModel.getProductSku().split("-");
                                       if(skuArray != null && skuArray.length > 2){
                                           productIdStr = skuArray[2];
                                           ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().eq("product_id", productIdStr));
                                           if (productsEntity != null) {
                                               productOrderItemEntity.setProductId(productsEntity.getProductId());
                                               newOrderItemEntity.setProductId(productsEntity.getProductId());
                                           }else{
                                               productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku", orderItemModel.getProductSku()));
                                               if (productsEntity != null) {
                                                   productOrderItemEntity.setProductId(productsEntity.getProductId());
                                                   newOrderItemEntity.setProductId(productsEntity.getProductId());
                                               }
                                           }
                                       }
                                   }
                                   if(orderItemModel.getProductAsin()!=null){
                                       productOrderItemEntity.setProductAsin(orderItemModel.getProductAsin());
                                       newOrderItemEntity.setProductAsin(orderItemModel.getProductAsin());
                                   }
                                   if(orderItemModel.getProductPrice()!=null){
                                       productOrderItemEntity.setProductPrice(orderItemModel.getProductPrice());
                                       newOrderItemEntity.setProductPrice(orderItemModel.getProductPrice());
                                   }
                                   if (StringUtils.isNotBlank(orderItemModel.getProductImageUrl())) {
                                       productOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                       newOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                       orderEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                       neworderEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                   }
                                   productOrderItemEntity.setOrderItemNumber(orderItemModel.getOrderItemNumber());
                                   productOrderItemEntity.setUpdatetime(orderItemModel.getUpdatetime());
                                   newOrderItemEntity.setOrderItemNumber(orderItemModel.getOrderItemNumber());
                                   newOrderItemEntity.setUpdatetime(orderItemModel.getUpdatetime());
                                   productOrderItemService.insert(productOrderItemEntity);
                                   newOrderItemService.insert(newOrderItemEntity);
                               }
                           }
                       }
//                        orderEntity.setOrderItemId(orderModel.getOrderItemId());
                        if (orderModel.getBuyDate() != null) {
                            orderEntity.setBuyDate(orderModel.getBuyDate());
                            neworderEntity.setBuyDate(orderModel.getBuyDate());
                        } else {
                            orderEntity.setBuyDate(new Date());
                            neworderEntity.setBuyDate(orderModel.getBuyDate());
                        }

                        if ("PendingAvailability".equals(modelStatus) || "Pending".equals(modelStatus)) {
                            //未付款
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_PENDING);
                            neworderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_PENDING);
                            orderEntity.setOrderState("待付款");
                            neworderEntity.setOrderState("待付款");
                        } else if ("Unshipped".equals(modelStatus) || "PartiallyShipped".equals(modelStatus)) {
                            //已付款
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_UNSHIPPED);
                            neworderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_UNSHIPPED);
                            orderEntity.setOrderState("已付款");
                            neworderEntity.setOrderState("已付款");
                        }/*else if("Shipped".equals(modelStatus)){
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_SHIPPED);
                            neworderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_SHIPPED);
                            orderEntity.setOrderState("虚发货");
                            neworderEntity.setOrderState("虚发货");
                        }*/
                        orderEntity.setCountryCode(orderModel.getCountry());
                        neworderEntity.setCountryCode(orderModel.getCountry());
                        orderEntity.setShopId(orderModel.getShopId());
                        neworderEntity.setShopId(orderModel.getShopId());
                        orderEntity.setShopName(orderModel.getShopName());
                        neworderEntity.setShopName(orderModel.getShopName());
//                        orderEntity.setProductTitle(orderModel.getTitlename());
//                        orderEntity.setProductSku(orderModel.getProductSku());
//                        orderEntity.setProductAsin(orderModel.getProductAsin());

                        orderEntity.setOrderNumber(orderModel.getOrderNumber());
                        neworderEntity.setOrderNumber(orderModel.getOrderNumber());
                        String rateCode = orderModel.getCurrencyCode();
                        orderEntity.setRateCode(rateCode);
                        neworderEntity.setRateCode(rateCode);
                        orderEntity.setUserId(orderModel.getUserId());
                        neworderEntity.setUserId(orderModel.getUserId());
                        orderEntity.setDeptId(orderModel.getDeptId());
                        neworderEntity.setDeptId(orderModel.getDeptId());
                        orderEntity.setUpdateTime(new Date());
                        neworderEntity.setUpdateTime(new Date());
                        //设置汇率
                        BigDecimal rate = new BigDecimal(0.00);
                        if (StringUtils.isNotBlank(rateCode)) {
                            rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code", rateCode)).getRate();
                        }
                        orderEntity.setMomentRate(rate);
                        neworderEntity.setMomentRate(rate);
                        //获取订单金额（外币）
                        BigDecimal orderMoney = orderModel.getOrderMoney();
                        System.out.println("店铺id：：：：：" + orderModel.getShopId());
                        System.out.println("订单id：：：：：" + orderModel.getAmazonOrderId());
                        System.out.println("金额为:::::::::" + orderMoney);
                        if (orderMoney.compareTo(new BigDecimal("0.00")) != 0) {
                            orderEntity.setOrderMoney(orderMoney);
                            neworderEntity.setOrderMoney(orderMoney);
                            orderEntity.setOrderMoneyCny(orderMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            neworderEntity.setOrderMoneyCny(orderMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            //获取Amazon佣金（外币）
                            BigDecimal amazonCommission = orderMoney.multiply(new BigDecimal(0.15).setScale(2, BigDecimal.ROUND_HALF_UP));
                            orderEntity.setAmazonCommission(amazonCommission);
                            orderEntity.setAmazonCommissionCny(amazonCommission.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            neworderEntity.setAmazonCommission(amazonCommission);
                            neworderEntity.setAmazonCommissionCny(amazonCommission.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            //到账金额
                            BigDecimal accountMoney = orderMoney.subtract(amazonCommission);
                            orderEntity.setAccountMoney(accountMoney);
                            orderEntity.setAccountMoneyCny(accountMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            neworderEntity.setAccountMoney(accountMoney);
                            neworderEntity.setAccountMoneyCny(accountMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        this.insert(orderEntity);
                        newOrderService.insert(neworderEntity);
                        //新增收货人信息
                        ProductShipAddressEntity productShipAddressEntity = orderModel.getProductShipAddressEntity();
                        if (productShipAddressEntity != null) {//判断返回值是否有收件人信
                            productShipAddressEntity.setOrderId(orderEntity.getOrderId());
                            productShipAddressService.insert(productShipAddressEntity);
                        }
                    }
                } else {
                    if(neworderEntity == null){
                        //新增订单
                        if (!"Canceled".equals(modelStatus) && !"Shipped".equals(modelStatus)) {
                            //设置基本属性
                            neworderEntity=new NewOrderEntity();
                            neworderEntity.setAmazonOrderId(amazonOrderId);
                            //判断该订单的订单商品列表是否存在
                            List<OrderItemModel> orderItemModels=orderModel.getOrderItemModels();
                            if(orderItemModels!=null && orderItemModels.size()>0){
                                for (OrderItemModel orderItemModel:orderItemModels) {
                                    //判断该商品是否存在
                                    NewOrderItemEntity newOrderItemEntity=newOrderItemService.selectOne(new EntityWrapper<NewOrderItemEntity>().eq("order_item_id",orderItemModel.getOrderItemId()));
                                    //如果不存在
                                    if(newOrderItemEntity==null){
                                        newOrderItemEntity=new NewOrderItemEntity();
                                        if(orderModel.getAmazonOrderId()!=null){
                                            newOrderItemEntity.setAmazonOrderId(orderItemModel.getAmazonOrderId());
                                        }
                                        if(orderItemModel.getOrderItemId()!=null){
                                            newOrderItemEntity.setOrderItemId(orderItemModel.getOrderItemId());
                                        }
                                        if(orderItemModel.getProductId()!=null){
                                            newOrderItemEntity.setProductId(orderItemModel.getProductId());
                                        }
                                        if(orderItemModel.getProductTitle()!=null){
                                            newOrderItemEntity.setProductTitle(orderItemModel.getProductTitle());
                                        }
                                        String productIdStr = "0";
                                        if(orderItemModel.getProductSku()!=null){
                                            newOrderItemEntity.setProductSku(orderItemModel.getProductSku());
                                            String[] skuArray = orderItemModel.getProductSku().split("-");
                                            if(skuArray != null && skuArray.length > 2){
                                                productIdStr = skuArray[2];
                                                ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().eq("product_id", productIdStr));
                                                if (productsEntity != null) {
                                                    newOrderItemEntity.setProductId(productsEntity.getProductId());
                                                }else{
                                                    productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku", orderItemModel.getProductSku()));
                                                    if (productsEntity != null) {
                                                        newOrderItemEntity.setProductId(productsEntity.getProductId());
                                                    }
                                                }
                                            }
                                        }
                                        if(orderItemModel.getProductAsin()!=null){
                                            newOrderItemEntity.setProductAsin(orderItemModel.getProductAsin());
                                        }
                                        if(orderItemModel.getProductPrice()!=null){
                                            newOrderItemEntity.setProductPrice(orderItemModel.getProductPrice());
                                        }
                                        if (StringUtils.isNotBlank(orderItemModel.getProductImageUrl())) {
                                            newOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                            neworderEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                        }
                                        newOrderItemEntity.setOrderItemNumber(orderItemModel.getOrderItemNumber());
                                        newOrderItemEntity.setUpdatetime(orderItemModel.getUpdatetime());
                                        newOrderItemService.insert(newOrderItemEntity);
                                    }
                                }
                            }
                            if (orderModel.getBuyDate() != null) {
                                neworderEntity.setBuyDate(orderModel.getBuyDate());
                            } else {
                                neworderEntity.setBuyDate(orderModel.getBuyDate());
                            }

                            if ("PendingAvailability".equals(modelStatus) || "Pending".equals(modelStatus)) {
                                //未付款
                                neworderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_PENDING);
                                neworderEntity.setOrderState("待付款");
                            } else if ("Unshipped".equals(modelStatus) || "PartiallyShipped".equals(modelStatus)) {
                                //已付款
                                neworderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_UNSHIPPED);
                                neworderEntity.setOrderState("已付款");
                            }
                            neworderEntity.setCountryCode(orderModel.getCountry());
                            neworderEntity.setShopId(orderModel.getShopId());
                            neworderEntity.setShopName(orderModel.getShopName());

                            neworderEntity.setOrderNumber(orderModel.getOrderNumber());
                            String rateCode = orderModel.getCurrencyCode();
                            neworderEntity.setRateCode(rateCode);
                            neworderEntity.setUserId(orderModel.getUserId());
                            neworderEntity.setDeptId(orderModel.getDeptId());
                            neworderEntity.setUpdateTime(new Date());
                            //设置汇率
                            BigDecimal rate = new BigDecimal(0.00);
                            if (StringUtils.isNotBlank(rateCode)) {
                                rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code", rateCode)).getRate();
                            }
                            neworderEntity.setMomentRate(rate);
                            //获取订单金额（外币）
                            BigDecimal orderMoney = orderModel.getOrderMoney();
                            if (orderMoney.compareTo(new BigDecimal("0.00")) != 0) {
                                neworderEntity.setOrderMoney(orderMoney);
                                neworderEntity.setOrderMoneyCny(orderMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                                //获取Amazon佣金（外币）
                                BigDecimal amazonCommission = orderMoney.multiply(new BigDecimal(0.15).setScale(2, BigDecimal.ROUND_HALF_UP));
                                neworderEntity.setAmazonCommission(amazonCommission);
                                neworderEntity.setAmazonCommissionCny(amazonCommission.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                                //到账金额
                                BigDecimal accountMoney = orderMoney.subtract(amazonCommission);
                                neworderEntity.setAccountMoney(accountMoney);
                                neworderEntity.setAccountMoneyCny(accountMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                            newOrderService.insert(neworderEntity);
                        }
                    }
                    if ((!"Canceled".equals(orderModel.getOrderStatus()) && (!"Shipped".equals(orderModel.getOrderStatus())))) {
                        List<OrderItemModel> orderItemModels = orderModel.getOrderItemModels();
                        if (orderItemModels != null && orderItemModels.size() > 0) {
                            for (OrderItemModel orderItemModel : orderItemModels) {

                                //判断该商品是否存在
                                ProductOrderItemEntity productOrderItemEntity = productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id", orderItemModel.getOrderItemId()));
                                NewOrderItemEntity newOrderItemEntity=newOrderItemService.selectOne(new EntityWrapper<NewOrderItemEntity>().eq("order_item_id", orderItemModel.getOrderItemId()));
                                if(newOrderItemEntity == null){
                                    newOrderItemEntity = new NewOrderItemEntity();
                                }
                                //存在更新
                                if (StringUtils.isNotBlank(orderItemModel.getProductImageUrl())) {
                                    productOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                    newOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                    orderEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                    neworderEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                }
                                String productIdStr = "0";
                                if(orderItemModel.getProductSku()!=null){
                                    String[] skuArray = orderItemModel.getProductSku().split("-");
                                    if(skuArray != null && skuArray.length > 2){
                                        productIdStr = skuArray[2];
                                        ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().eq("product_id", productIdStr));
                                        if(productsEntity != null){
                                            productOrderItemEntity.setProductId(productsEntity.getProductId());
                                            newOrderItemEntity.setProductId(productsEntity.getProductId());
                                        }else{
                                            productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku", orderItemModel.getProductSku()));
                                            if (productsEntity != null) {
                                                productOrderItemEntity.setProductId(productsEntity.getProductId());
                                                newOrderItemEntity.setProductId(productsEntity.getProductId());

                                            }
                                        }
                                    }
                                }
                                //更新订单商品
                                orderEntity.setProductTitle(orderItemModel.getProductTitle());
                                orderEntity.setCountryCode(orderModel.getCountry());
                                neworderEntity.setProductTitle(orderItemModel.getProductTitle());
                                neworderEntity.setCountryCode(orderModel.getCountry());
                                productOrderItemEntity.setProductTitle(orderItemModel.getProductTitle());
                                productOrderItemEntity.setProductSku(orderItemModel.getProductSku());
                                productOrderItemEntity.setProductAsin(orderItemModel.getProductAsin());
                                productOrderItemEntity.setProductPrice(orderItemModel.getProductPrice());
                                productOrderItemEntity.setUpdatetime(new Date());
                                newOrderItemEntity.setProductTitle(orderItemModel.getProductTitle());
                                newOrderItemEntity.setProductSku(orderItemModel.getProductSku());
                                newOrderItemEntity.setProductAsin(orderItemModel.getProductAsin());
                                newOrderItemEntity.setProductPrice(orderItemModel.getProductPrice());
                                newOrderItemEntity.setUpdatetime(new Date());
                                productOrderItemService.updateById(productOrderItemEntity);
                                newOrderItemService.insertOrUpdate(newOrderItemEntity);
                            }

                        }
                        //更新订单
                        //设置汇率
                        BigDecimal rate = new BigDecimal(0.00);
                        String rateCode = orderModel.getCurrencyCode();
                        if (StringUtils.isNotBlank(rateCode)) {
                            rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code", rateCode)).getRate();
                            orderEntity.setRateCode(rateCode);
                            orderEntity.setMomentRate(rate);
                            neworderEntity.setRateCode(rateCode);
                            neworderEntity.setMomentRate(rate);
                        }
                        BigDecimal orderMoney = orderModel.getOrderMoney();
                        if (orderMoney.compareTo(new BigDecimal("0.00")) != 0) {
                            orderEntity.setOrderMoney(orderMoney);
                            orderEntity.setOrderMoneyCny(orderMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            neworderEntity.setOrderMoney(orderMoney);
                            neworderEntity.setOrderMoneyCny(orderMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            //获取Amazon佣金（外币）
                            BigDecimal amazonCommission = orderMoney.multiply(new BigDecimal(0.15).setScale(2, BigDecimal.ROUND_HALF_UP));
                            orderEntity.setAmazonCommission(amazonCommission);
                            orderEntity.setAmazonCommissionCny(amazonCommission.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            neworderEntity.setAmazonCommission(amazonCommission);
                            neworderEntity.setAmazonCommissionCny(amazonCommission.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            //到账金额
                            BigDecimal accountMoney = orderMoney.subtract(amazonCommission);
                            orderEntity.setAccountMoney(accountMoney);
                            orderEntity.setAccountMoneyCny(accountMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            neworderEntity.setAccountMoney(accountMoney);
                            neworderEntity.setAccountMoneyCny(accountMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        //获取状态判断是否为取消
                        if (ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_CANCELED.equals(modelStatus)) {
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED);
                            orderEntity.setOrderState("取消");
                            neworderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.NEW_ORDER_STATE_CANCELED);
                            neworderEntity.setOrderState("取消");
                        } else {
                            String orderStatus = orderEntity.getOrderStatus();
                            //获取当前订单状态判断是否为待付款、已付款、虚发货
                            List newamazonStateList = Arrays.asList(ConstantDictionary.OrderStateCode.NEW_AMAZON_ORDER_STATE);
                            if ( newamazonStateList.contains(orderStatus)) {
                                //获取返回状态判断是否为待付款、已付款、虚发货
                                if (newamazonStateList.contains(modelStatus)) {
                                    //判断两个状态不想等时更改状态
                                    if (!modelStatus.equals(orderStatus)) {
                                        orderEntity.setOrderStatus(modelStatus);
                                        neworderEntity.setOrderStatus(modelStatus);
                                        String orderState = dataDictionaryService.selectOne(
                                                new EntityWrapper<DataDictionaryEntity>()
                                                        .eq("data_type", "AMAZON_ORDER_STATE")
                                                        .eq("data_number", modelStatus)
                                        ).getDataContent();
                                        String neworderState = dataDictionaryService.selectOne(
                                                new EntityWrapper<DataDictionaryEntity>()
                                                        .eq("data_type", "NEW_AMAZON_ORDER_STATE")
                                                        .eq("data_number", modelStatus)
                                        ).getDataContent();

                                        orderEntity.setOrderState(orderState);
                                        neworderEntity.setOrderState(neworderState);
                                        this.updateById(orderEntity);
                                        newOrderService.insertOrUpdate(neworderEntity);
                                    }
                                    //新增/修改收货人信息
                                    ProductShipAddressEntity productShipAddressEntity = orderModel.getProductShipAddressEntity();
                                    if (productShipAddressEntity != null) {
                                        //判断返回值是否有收件人信息
                                        ProductShipAddressEntity shipAddress = productShipAddressService.selectOne(
                                                new EntityWrapper<ProductShipAddressEntity>().eq("amazon_order_id", productShipAddressEntity.getAmazonOrderId())
                                        );
                                        if (shipAddress == null) {
                                            productShipAddressEntity.setOrderId(neworderEntity.getOrderId());
                                            productShipAddressService.insert(productShipAddressEntity);
                                        } else {
                                            productShipAddressEntity.setShipAddressId(shipAddress.getShipAddressId());
                                            productShipAddressService.updateById(productShipAddressEntity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            }
    }
    /**
     * 获取以往订单
     * @param orderModelList
     */
    @Override
    public void updateOldOrder(List<OrderModel> orderModelList) {
        System.out.println("进入保存方法，保存以往订单");
        for(OrderModel orderModel : orderModelList ){
            //获取亚马逊订单id
            String amazonOrderId = orderModel.getAmazonOrderId();
            if(StringUtils.isNotBlank(amazonOrderId)){
                //判断该订单是否存在
                OrderEntity orderEntity = this.selectOne(new EntityWrapper<OrderEntity>().eq("amazon_order_id",amazonOrderId));
                //订单状态
                String modelStatus = orderModel.getOrderStatus();
                if(orderEntity == null){
                    //新增订单
                    if(!"Canceled".equals(modelStatus)){
                        //设置基本属性
                        orderEntity = new OrderEntity();

                        orderEntity.setAmazonOrderId(amazonOrderId);
                        if(orderModel.getBuyDate() != null){
                            orderEntity.setBuyDate(orderModel.getBuyDate());
                        }else{
                            orderEntity.setBuyDate(new Date());
                        }

                        if("PendingAvailability".equals(modelStatus) || "Pending".equals(modelStatus)){
                            //未付款
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_PENDING);
                            orderEntity.setOrderState("待付款");
                        }else if("Unshipped".equals(modelStatus) || "PartiallyShipped".equals(modelStatus)){
                            //已付款
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_UNSHIPPED);
                            orderEntity.setOrderState("已付款");
                        }else if("Shipped".equals(modelStatus)){
                            orderEntity.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_SHIPPED);
                            orderEntity.setOrderState("虚发货");
                        }
                        //判断该订单的订单商品列表是否存在
                        List<OrderItemModel> orderItemModels=orderModel.getOrderItemModels();
                        if(orderItemModels!=null && orderItemModels.size()>0){
                            for (OrderItemModel orderItemModel:orderItemModels) {
                                //判断该商品是否存在
                                ProductOrderItemEntity productOrderItemEntity=productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id",orderItemModel.getOrderItemId()));
                                //如果不存在
                                if(productOrderItemEntity==null){
                                    productOrderItemEntity=new ProductOrderItemEntity();
                                    if(orderModel.getAmazonOrderId()!=null){
                                        productOrderItemEntity.setAmazonOrderId(orderItemModel.getAmazonOrderId());
                                    }
                                    if(orderItemModel.getOrderItemId()!=null){
                                        productOrderItemEntity.setOrderItemId(orderItemModel.getOrderItemId());
                                    }
                                    if(orderItemModel.getProductId()!=null){
                                        productOrderItemEntity.setProductId(orderItemModel.getProductId());
                                    }
                                    if(orderItemModel.getProductTitle()!=null){
                                        productOrderItemEntity.setProductTitle(orderItemModel.getProductTitle());
                                    }
                                    if(orderItemModel.getProductSku()!=null){
                                        productOrderItemEntity.setProductSku(orderItemModel.getProductSku());
                                    }
                                    if(orderItemModel.getProductAsin()!=null){
                                        productOrderItemEntity.setProductAsin(orderItemModel.getProductAsin());
                                    }
                                    if(orderItemModel.getProductPrice()!=null){
                                        productOrderItemEntity.setProductPrice(orderItemModel.getProductPrice());
                                    }
                                    ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku", orderItemModel.getProductSku()));
                                    if (StringUtils.isNotBlank(orderItemModel.getProductImageUrl())) {
                                        productOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                        orderEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                    } else if (productsEntity != null) {
                                        productOrderItemEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                                        orderEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                                    }
                                    if (productsEntity != null) {
                                        productOrderItemEntity.setProductId(productsEntity.getProductId());
                                        productOrderItemEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                                        orderEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                                    }
                                    productOrderItemEntity.setOrderItemNumber(orderItemModel.getOrderItemNumber());
                                    productOrderItemEntity.setUpdatetime(orderItemModel.getUpdatetime());
                                    productOrderItemService.insert(productOrderItemEntity);
                                }
                            }
                        }

                        orderEntity.setCountryCode(orderModel.getCountry());
                        orderEntity.setShopId(orderModel.getShopId());
                        orderEntity.setShopName(orderModel.getShopName());
                        /*orderEntity.setProductTitle(orderModel.getTitlename());
                        orderEntity.setProductSku(orderModel.getProductSku());
                        orderEntity.setProductAsin(orderModel.getProductAsin());
                        ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku",orderModel.getProductSku()));
                        if(StringUtils.isNotBlank(orderModel.getProductImageUrl())){
                            orderEntity.setProductImageUrl(orderModel.getProductImageUrl());
                        }else if(productsEntity != null){
                            orderEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                        }
                        if(productsEntity != null){
                            orderEntity.setProductId(productsEntity.getProductId());
                            orderEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                        }*/
                        orderEntity.setOrderNumber(orderModel.getOrderNumber());
                        String rateCode = orderModel.getCurrencyCode();
                        orderEntity.setRateCode(rateCode);
                        orderEntity.setUserId(orderModel.getUserId());
                        orderEntity.setDeptId(orderModel.getDeptId());
                        orderEntity.setUpdateTime(new Date());
                        //设置汇率
                        BigDecimal rate = new BigDecimal(0.00);
                        if(StringUtils.isNotBlank(rateCode)){
                            rate = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code",rateCode)).getRate();
                        }
                        orderEntity.setMomentRate(rate);
                        //获取订单金额（外币）
                        BigDecimal orderMoney = orderModel.getOrderMoney();
                        System.out.println("店铺id：：：：：" + orderModel.getShopId());
                        System.out.println("订单id：：：：：" + orderModel.getAmazonOrderId());
                        System.out.println("金额为:::::::::" + orderMoney);
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
                        orderEntity.setIsOld(1);
                        this.insert(orderEntity);
                        //新增收货人信息
                        ProductShipAddressEntity productShipAddressEntity = orderModel.getProductShipAddressEntity();
                        if(productShipAddressEntity != null){//判断返回值是否有收件人信息
                            ProductShipAddressEntity shipAddress = productShipAddressService.selectOne(
                                    new EntityWrapper<ProductShipAddressEntity>().eq("order_id",orderEntity.getOrderId())
                            );
                            if(shipAddress == null){
                                productShipAddressEntity.setOrderId(orderEntity.getOrderId());
                                productShipAddressService.insert(productShipAddressEntity);
                            }
                        }
                    }
                }else{
                    if(!"Canceled".equals(orderModel.getOrderStatus())){
                    List<OrderItemModel> orderItemModels=orderModel.getOrderItemModels();
                    if(orderItemModels!=null && orderItemModels.size()>0) {
                        for (OrderItemModel orderItemModel : orderItemModels) {
                            //判断该商品是否存在
                            ProductOrderItemEntity productOrderItemEntity = productOrderItemService.selectOne(new EntityWrapper<ProductOrderItemEntity>().eq("order_item_id", orderItemModel.getOrderItemId()));
                            //存在更新
                            ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku", orderItemModel.getProductSku()));
                            System.out.println("###"+orderItemModel.getProductImageUrl()+"###");
                            if (StringUtils.isNotBlank(orderItemModel.getProductImageUrl())) {
                                productOrderItemEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                                orderEntity.setProductImageUrl(orderItemModel.getProductImageUrl());
                            } else if (productsEntity != null) {
                                productOrderItemEntity.setProductImageUrl(productsEntity.getMainImageUrl());
                                orderEntity.setProductImageUrl(productsEntity.getMainImageUrl());
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
                    //更新订单
//                    ProductsEntity productsEntity = productsService.selectOne(new EntityWrapper<ProductsEntity>().like("product_sku",orderModel.getProductSku()));
                   /* if(StringUtils.isNotBlank(orderModel.getProductImageUrl())){
                        orderEntity.setProductImageUrl(orderModel.getProductImageUrl());
                    }*/
//                    else if(productsEntity != null){
//                        orderEntity.setProductImageUrl(productsEntity.getMainImageUrl());
//                    }
                    orderEntity.setBuyDate(orderModel.getBuyDate());
//                    orderEntity.setCountryCode(orderModel.getCountry());
//                    orderEntity.setProductTitle(orderModel.getTitlename());
                    this.updateById(orderEntity);
                    //设置汇率
                    /*BigDecimal rate = new BigDecimal(0.00);
                    String rateCode = orderModel.getCurrencyCode();
                    orderEntity.setRateCode(rateCode);
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
                    }*/
                    //获取状态判断是否为取消
                    /*if(ConstantDictionary.OrderStateCode.ORDER_STATE_CANCELED.equals(modelStatus)){
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
                                    this.updateById(orderEntity);
                                    //新增/修改收货人信息
                                    *//*ProductShipAddressEntity productShipAddressEntity = orderModel.getProductShipAddressEntity();
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
                                    }*//*
                                }
                            }
                        }
                    }*/
                }
                }
            }
        }
    }
    @Override
    public void internationalShipments(OrderEntity order) {
        order.setOrderStatus(ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED);
        order.setOrderState("国际已发货");
    }

    /**
     * 扣款
     * @param order
     */
    @Override
    public void deduction(OrderEntity order){
        List<ConsumeEntity> consumeList = consumeService.selectList(new EntityWrapper<ConsumeEntity>().eq("order_id",order.getOrderId()));
        if(consumeList == null || consumeList.size() == 0){
            /*//扣款
            SysDeptEntity dept = deptService.selectById(order.getDeptId());
            //原来余额
            dept.setBalance(dept.getBalance().subtract(order.getInterFreight()).subtract(order.getPlatformCommissions()));
            deptService.updateById(dept);*/
            //生成运费记录
            ConsumeEntity consumeEntity1 = new ConsumeEntity();
            consumeEntity1.setAmazonOrderId(order.getAmazonOrderId());
            consumeEntity1.setDeptId(order.getDeptId());
            consumeEntity1.setDeptName(deptService.selectById(order.getDeptId()).getName());
            consumeEntity1.setUserId(order.getUserId());
            if(userService.selectById(order.getUserId()) != null){
                consumeEntity1.setUserName(userService.selectById(order.getUserId()).getDisplayName());
            }
            consumeEntity1.setType("物流费");
            consumeEntity1.setOrderId(order.getOrderId());
            consumeEntity1.setMoney(order.getInterFreight());
            consumeEntity1.setAbroadWaybill(order.getAbroadWaybill());
            consumeEntity1.setCreateTime(new Date());
            consumeService.insert(consumeEntity1);
            //生成服务费记录
            ConsumeEntity consumeEntity2 = new ConsumeEntity();
            consumeEntity2.setAmazonOrderId(order.getAmazonOrderId());
            consumeEntity2.setDeptId(order.getDeptId());
            consumeEntity2.setDeptName(deptService.selectById(order.getDeptId()).getName());
            consumeEntity2.setUserId(order.getUserId());
            if(userService.selectById(order.getUserId()) != null){
                consumeEntity2.setUserName(userService.selectById(order.getUserId()).getDisplayName());
            }
            consumeEntity2.setType("服务费");
            consumeEntity2.setOrderId(order.getOrderId());
            consumeEntity2.setMoney(order.getPlatformCommissions());
            consumeEntity2.setCreateTime(new Date());
            consumeService.insert(consumeEntity2);
        }
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

    //根据订单id进行更新订单Amazon状态
    @Override
    @Async("taskExecutor")
    public OrderModel updateOrderAmazonStatus(String AmazonOrderId, OrderEntity orderEntity) {
        AmazonGrantShopEntity amazonGrantShopEntity = amazonGrantShopService.selectOne(new EntityWrapper<AmazonGrantShopEntity>().eq("user_id",orderEntity.getUserId()).eq("country_code",orderEntity.getCountryCode()));
        if(amazonGrantShopEntity != null && amazonGrantShopEntity.getGrantId() != null){
            AmazonGrantEntity grant = amazonGrantService.selectById(amazonGrantShopEntity.getGrantId());
            String sellerId = grant.getMerchantId();//获得商家id
            String mwsAuthToken = grant.getGrantToken();//获得授权Token
            MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
            MarketplaceWebServiceOrdersAsyncClient client = new MarketplaceWebServiceOrdersAsyncClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                    "mws_test", "1.0", config, null);
            List<GetOrderRequest> requestList = new ArrayList<GetOrderRequest>();
            GetOrderRequest request = new GetOrderRequest();
            request.setSellerId(sellerId);
            request.setMWSAuthToken(mwsAuthToken);
            List<String> amazonOrderIds = new ArrayList<String>();
            amazonOrderIds.add(AmazonOrderId);
            request.setAmazonOrderId(amazonOrderIds);
            List<Object> responselist = OrderTimer.invokeGetOrder(client, requestList);
            Boolean isSuccess = false;
            List<ListOrdersResponseDto> listOrdersResponseDtos = new ArrayList<>();
            ListOrdersResponseDto listOrdersResponseDto = null;
            for (Object tempResponse : responselist) {
                // Object 转换 ListOrdersResponse 还是 MarketplaceWebServiceOrdersException
                String className = tempResponse.getClass().getName();
                if ((MarketplaceWebServiceOrdersException.class.getName()).equals(className) == true) {
                    System.out.println("responseList 类型是 MarketplaceWebServiceOrdersException。");
                    isSuccess = false;
                    break;
                } else if ((ListOrdersResponse.class.getName()).equals(className) == true) {
                    System.out.println("responseList 类型是 ListOrdersResponse。");
                    ListOrdersResponse response = (ListOrdersResponse) tempResponse;
                    listOrdersResponseDto = analysisListOrdersResponse(response.toXML());
                    isSuccess = true;
                }
            }
            listOrdersResponseDtos.add(listOrdersResponseDto);//封装解析出来的
            {
                if(listOrdersResponseDtos.size() > 0){
                    //循环输出
                    for (int i = 0; i < listOrdersResponseDtos.size(); i++) {
                        //循环输出订单
                        if(listOrdersResponseDtos.get(i) != null && listOrdersResponseDtos.get(i).getOrders() != null && listOrdersResponseDtos.get(i).getOrders().size() >0){
                            for (int j = 0; j < listOrdersResponseDtos.get(i).getOrders().size(); j++) {
                                List<ListOrderItemsRequest> ListOrderItemsRequestRequests = new ArrayList<ListOrderItemsRequest>();
                                ListOrderItemsRequest ListOrderItemsRequest = new ListOrderItemsRequest();
                                ListOrderItemsRequest.setAmazonOrderId(AmazonOrderId);
                                ListOrderItemsRequest.setSellerId(sellerId);
                                ListOrderItemsRequest.setMWSAuthToken(mwsAuthToken);
                                ListOrderItemsRequestRequests.add(ListOrderItemsRequest);
                                List<Object> responseList3 = invokeListOrderItems(client, ListOrderItemsRequestRequests);
                                List<ListOrderItemsByNextTokenResponseDto> orderItemResponseDtos = new ArrayList<>();
                                ListOrderItemsByNextTokenResponseDto orderItemResponseDto = null;
                                for (Object tempResponse : responseList3) {
                                    // Object 转换 listOrdersByNextTokenResponseDto 还是 MarketplaceWebServiceOrdersException
                                    String className = tempResponse.getClass().getName();
                                    if ((ListOrderItemsResponse.class.getName()).equals(className) == true) {
                                        System.out.println("responseList3 类型是 ListOrderItemsByNextTokenResponse。");
                                        ListOrderItemsResponse response = (ListOrderItemsResponse) tempResponse;
                                        orderItemResponseDto = analysisListOrderItemsByNextTokenResponse(response.toXML());
                                    }
                                }
                                orderItemResponseDtos.add(orderItemResponseDto);
                                for (int k = 0; k < orderItemResponseDtos.size(); k++) {
                                    for (int m = 0; m < orderItemResponseDtos.get(k).getOrderItems().size(); m++) {
                                        String orderStatus = listOrdersResponseDtos.get(i).getOrders().get(j).getOrderStatus();
                                        ProductShipAddressEntity addressEntity = new ProductShipAddressEntity();
                                        String shipname = listOrdersResponseDtos.get(i).getOrders().get(j).getName();
                                        String shipaddress = listOrdersResponseDtos.get(i).getOrders().get(j).getAddressLine1();
                                        String shipaddress2 = listOrdersResponseDtos.get(i).getOrders().get(j).getAddressLine2();
                                        String shipcity = listOrdersResponseDtos.get(i).getOrders().get(j).getCity();
                                        String shipCountry = listOrdersResponseDtos.get(i).getOrders().get(j).getCounty();
                                        String shipdistrict = listOrdersResponseDtos.get(i).getOrders().get(j).getDistrict();
                                        String shipregion = listOrdersResponseDtos.get(i).getOrders().get(j).getStateOrRegion();
                                        String shiptel = listOrdersResponseDtos.get(i).getOrders().get(j).getPhone();
                                        String shipzip = listOrdersResponseDtos.get(i).getOrders().get(j).getPostalCode();
                                        //获得订单商品sku
                                        //进行数据库表查询根据AmazonOrderId,进行更新
                                        Map map = new HashMap();
                                        map.put("AmazonOrderId", AmazonOrderId);
                                        List<OrderModel> orderModels = this.selectByMap(map);
                                        if (orderModels != null && orderModels.size() > 0) {
                                            OrderModel orderModel = orderModels.get(0);
                                            if (orderStatus != null) {
                                                orderModel.setOrderStatus(orderStatus);
                                            } else {
                                                orderModel.setOrderStatus("");
                                            }
                                            if (shipname != null) {
                                                addressEntity.setShipName(shipname);
                                            } else {
                                                addressEntity.setShipName("");
                                            }
                                            if (shipaddress != null) {
                                                addressEntity.setShipAddressLine1(shipaddress);
                                            } else if (shipaddress2 != null) {
                                                addressEntity.setShipAddressLine1(shipaddress2);
                                            } else {
                                                addressEntity.setShipAddressLine1("");
                                            }
                                            if (shipcity != null) {
                                                addressEntity.setShipCity(shipcity);

                                            } else {
                                                addressEntity.setShipCity("");

                                            }
                                            if (shipCountry != null) {
                                                addressEntity.setShipCounty(shipCountry);
                                            } else {
                                                addressEntity.setShipCounty("");
                                            }
                                            if (shipdistrict != null) {
                                                addressEntity.setShipDistrict(shipdistrict);

                                            } else {
                                                addressEntity.setShipDistrict("");

                                            }
                                            if (shipregion != null) {
                                                addressEntity.setShipRegion(shipregion);
                                            } else {
                                                addressEntity.setShipRegion("");
                                            }
                                            if (shiptel != null) {
                                                addressEntity.setShipTel(shiptel);
                                            } else {
                                                addressEntity.setShipTel("");
                                            }
                                            if (shipzip != null) {
                                                addressEntity.setShipZip(shipzip);
                                            } else {
                                                addressEntity.setShipZip("");
                                            }
                                            orderModel.setProductShipAddressEntity(addressEntity);
                                            return orderModel;
                                        }

                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 上传国际物流信息到amazon
     * @param sendDataMoedl
     */
    @Override
    @Async("taskExecutor")
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
        //进行数据上传(步骤一)
        String feedSubmissionId = submitLogisticsService.submitFeed(serviceURL.get(0),sellerId,mwsAuthToken,feedType,filePath,accessKey,secretKey);
            AbroadLogisticsEntity abroadLogisticsEntity = abroadLogisticsService.selectOne(new EntityWrapper<AbroadLogisticsEntity>().eq("order_id",orderId));
            //判读亚马逊后台订单的状态
            if(StringUtils.isNotBlank(feedSubmissionId)){
                try {
                    Thread.sleep(3 * 60 * 1000);
                    //获取亚马逊后台订单状态
                    String amazonOrderId=orderService.selectById(orderId).getAmazonOrderId();
                    String orderStatus="";
                    MarketplaceWebServiceOrdersConfig config=new MarketplaceWebServiceOrdersConfig();
                    config.setServiceURL(serviceURL.get(0));
                    MarketplaceWebServiceOrdersAsyncClient client=new MarketplaceWebServiceOrdersAsyncClient(accessKey,secretKey,"my_test","1.0",config,null);
                    List<GetOrderRequest> requestList=new ArrayList<>();
                    GetOrderRequest request=new GetOrderRequest();
                    request.setSellerId(sellerId);
                    request.setMWSAuthToken(mwsAuthToken);
                    List<String> amazonOrderIds=new ArrayList<>();
                    amazonOrderIds.add(amazonOrderId);
                    request.setAmazonOrderId(amazonOrderIds);
                    requestList.add(request);
                    List<Object> responseList=invokeGetOrder(client,requestList);
                    Boolean isSuccess=false;
                    GetOrderResponse getOrderResponse=null;
                    for(Object tempResponse:responseList){
                        String className=tempResponse.getClass().getName();
                        if((GetOrderResponse.class.getName()).equals(className) ==true){
                            System.out.println("responseList类型是GetOrderResponse.");
                            GetOrderResponse response=(GetOrderResponse) tempResponse;
                            orderStatus=response.toXML();
                            if(orderStatus.contains("<OrderStatus>")){
                                orderStatus=orderStatus.substring(orderStatus.indexOf("<OrderStatus>"),orderStatus.indexOf("</OrderStatus>")).replace("<OrderStatus>","");
                            }
                            isSuccess=true;
                        }else{
                            System.out.println("responseList类型是MarketplaceWebServiceOrderException.");
                            isSuccess=false;
                            continue;
                        }

                    }
                    if("Shipped".equals(orderStatus)){
                        abroadLogisticsEntity.setIsSynchronization(1);//表示同步成功
                        abroadLogisticsService.insertOrUpdate(abroadLogisticsEntity);
                    }else {
                        Thread.sleep(3 * 60 * 1000);
                        newamazonUpdateLogistics(sendDataMoedl, orderId);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Thread.sleep(3*60*1000);
                    amazonUpdateLogistics(sendDataMoedl,orderId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }


    /**
     * 对接新物流上传国际物流信息到amazon
     * @param sendDataMoedl
     */
    @Override
    @Async("taskExecutor")
    public void newamazonUpdateLogistics(SendDataMoedl sendDataMoedl, Long orderId){
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
        //进行数据上传(步骤一)
        String feedSubmissionId = submitLogisticsService.submitFeed(serviceURL.get(0),sellerId,mwsAuthToken,feedType,filePath,accessKey,secretKey);
        NewOrderAbroadLogisticsEntity newAbroadLogisticsEntity = newOrderAbroadLogisticsService.selectOne(new EntityWrapper<NewOrderAbroadLogisticsEntity>().eq("order_id",orderId));
        //判空
        if(StringUtils.isNotBlank(feedSubmissionId)){
            try {
                Thread.sleep(3 * 60 * 1000);
                //获取亚马逊后台订单状态
                String amazonOrderId=newOrderService.selectById(orderId).getAmazonOrderId();
                String orderStatus="";
                MarketplaceWebServiceOrdersConfig config=new MarketplaceWebServiceOrdersConfig();
                config.setServiceURL(serviceURL.get(0));
                MarketplaceWebServiceOrdersAsyncClient client=new MarketplaceWebServiceOrdersAsyncClient(accessKey,secretKey,"my_test","1.0",config,null);
                List<GetOrderRequest> requestList=new ArrayList<>();
                GetOrderRequest request=new GetOrderRequest();
                request.setSellerId(sellerId);
                request.setMWSAuthToken(mwsAuthToken);
                List<String> amazonOrderIds=new ArrayList<>();
                amazonOrderIds.add(amazonOrderId);
                request.setAmazonOrderId(amazonOrderIds);
                requestList.add(request);
                List<Object> responseList=invokeGetOrder(client,requestList);
                Boolean isSuccess=false;
                GetOrderResponse getOrderResponse=null;
                for(Object tempResponse:responseList){
                    String className=tempResponse.getClass().getName();
                    if((GetOrderResponse.class.getName()).equals(className) ==true){
                        System.out.println("responseList类型是GetOrderResponse.");
                        GetOrderResponse response=(GetOrderResponse) tempResponse;
                        orderStatus=response.toXML();
                        if(orderStatus.contains("<OrderStatus>")){
                            orderStatus=orderStatus.substring(orderStatus.indexOf("<OrderStatus>"),orderStatus.indexOf("</OrderStatus>")).replace("<OrderStatus>","");
                        }
                        isSuccess=true;
                    }else{
                        System.out.println("responseList类型是MarketplaceWebServiceOrderException.");
                        isSuccess=false;
                        continue;
                    }

                }
                if("Shipped".equals(orderStatus)){
                    newAbroadLogisticsEntity.setIsSynchronization(1);//表示同步成功
                    newOrderAbroadLogisticsService.insertOrUpdate(newAbroadLogisticsEntity);
                }else {
                    Thread.sleep(3 * 60 * 1000);
                    newamazonUpdateLogistics(sendDataMoedl, orderId);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Thread.sleep(3 * 60 * 1000);
                newamazonUpdateLogistics(sendDataMoedl, orderId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }




    @Override
    @Async("taskExecutor")
    public void RefreshAmazonState(OrderEntity orderEntity, OrderModel orderModel) {
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

    @Override
    @Async("taskExecutor")
    public void RefreshOrder(Long orderId) {
        try {
            Thread.sleep(5*60*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        List<ProductOrderItemEntity> productOrderItemEntities=productOrderItemService.selectList(new EntityWrapper<ProductOrderItemEntity>().eq("amazon_order_id",amazonOrderId));
        for (ProductOrderItemEntity productOrderItemEntity:productOrderItemEntities) {
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
     * 获取单个订单的方法
     * @param client
     * @param requestList
     * @return
     */
    public static List<Object> invokeGetOrder(MarketplaceWebServiceOrdersAsync client,List<GetOrderRequest> requestList){
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