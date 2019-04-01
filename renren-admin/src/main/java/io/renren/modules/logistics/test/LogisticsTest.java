package io.renren.modules.logistics.test;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.logistics.DTO.ApplicationInfos;
import io.renren.modules.logistics.DTO.OrderRequestData;
import io.renren.modules.logistics.DTO.SenderInfo;
import io.renren.modules.logistics.DTO.ShippingInfo;
import io.renren.modules.logistics.entity.DomesticLogisticsEntity;
import io.renren.modules.logistics.service.DomesticLogisticsService;
import io.renren.modules.logistics.util.Base64Util;

import io.renren.modules.logistics.util.NewAbroadLogisticsUtil;
import io.renren.modules.order.entity.NewProductShipAddressEntity;
import io.renren.modules.order.entity.ProductShipAddressEntity;
import io.renren.modules.order.service.NewProductShipAddressService;
import io.renren.modules.order.service.ProductShipAddressService;
import io.renren.modules.product.entity.NewOrderEntity;
import io.renren.modules.product.entity.NewOrderItemEntity;
import io.renren.modules.product.service.NewOrderItemService;
import io.renren.modules.product.service.NewOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogisticsTest {
    @Autowired
    private ProductShipAddressService productShipAddressService;
    @Autowired
    private NewProductShipAddressService newProductShipAddressService;
    @Autowired
    private DomesticLogisticsService domesticLogisticsService;
    @Autowired
    private NewOrderService newOrderService;
    @Autowired
    private NewOrderItemService newOrderItemService;
    //测试环境客户编号
    public final static String clientId = "C88888";
    //测试环境用户密钥
    public final static String ApiSecret = "NMt9f54gz9M=";

    //测试接口地址：
    public final static String test_url="http://120.76.102.19:8034/LMS.API/api/";
    //推送订单
    public final static String pushUrl = test_url+"WayBill/BatchAdd";
    //打印接口
    public final static String printUrl = "http://120.76.102.19:8035/API/PrintUrl";
    //删除订单
    public final static String deleteUrl = test_url+"WayBill/DeleteCoustomerOrderInfo";
    //C63065
    //dCa0A/ChqZM=
    //正式环境客户编号
    public final static String xs_clientId = "C95057";
    //正式环境客户密钥
    public final static String xs_ApiSecret = "kEhItRLDWxk=";
    //正式环境接口地址（云途）
    public final static String xs_yt_url="http://api.yunexpress.com/LMS.API/api/";
    //正式环境接口地址（三态）
//    public final static String xs_st_url="https://www.sfcservice.com/LMS.API/api/";

    //线上推送订单
    public final static String xs_yt_pushUrl = xs_yt_url+"WayBill/BatchAdd";
    //线上打印接口
    public final static String xs_yt_printUrl = "http://api.yunexpress.com/LMS.API.Lable/Api/PrintUrl";
    //线上删除订单
    public final static String xs_yt_deleteUrl = xs_yt_url+"WayBill/DeleteCoustomerOrderInfo";
    //线上查询获取踪号
    public final static String xs_yt_getTrackNumber=xs_yt_url+"WayBill/GetTrackNumber";
    //线上修改订单重量
    public final static String xs_yt_updateWeight=xs_yt_url+"WayBill/UpdateWeight";
    //线上查看推送订单详情
    public final static String xs_yt_getOrderInfo=xs_yt_url+"WayBill/GetWayBill?wayBillNumber=";
    //线上获取运费明细
    public final static String xs_yt_getShippingFeeDetail=xs_yt_url+"WayBill/GetShippingFeeDetail?wayBillNumber=";





//    //线上推送订单
//    public final static String xs_st_pushUrl = xs_st_url+"WayBill/BatchAdd";
//    //线上打印接口
//    public final static String xs_st_printUrl = "http://api.yunexpress.com/LMS.API.Lable/Api/PrintUrl";
//    //线上删除订单
//    public final static String xs_st_deleteUrl = xs_st_url+"WayBill/DeleteCoustomerOrderInfo";
//    //线上查询获取踪号
//    public final static String xs_st_getprice=xs_st_url+"WayBill/GetTrackNumber";


    /**
     * 测试生成token的方法(Base64编码)
     *
     * @param clientId
     * @param ApiSecret
     * @return
     */
    public static String getToken(String clientId, String ApiSecret) {
        String data = clientId + '&' + ApiSecret;
        System.out.println("生成的token转码前：" + data);
        String token = Base64Util.encode(data.getBytes());
        System.out.println("编码后生成的token：" + token);
        return token;

    }

    public static String getTel(){
        for (int i = 0; i < 9; i++) {//9代表循环九次，产生九个随机号码
            String number = "139";//定义电话号码以139开头
            Random random = new Random();//定义random，产生随机数
            for (int j = 0; j < 8; j++) { //生成0~9 随机数
                number += random.nextInt(9);
            }
            return number;
        }
        return null;
    }


    /**
     * 线上生成token的方法(Base64编码)
     *
     * @param xs_clientId
     * @param xs_ApiSecret
     * @return
     */
    public static String getXsToken(String xs_clientId, String xs_ApiSecret) {
        String data = xs_clientId + '&' + xs_ApiSecret;
        System.out.println("生成的token转码前：" + data);
        String token = Base64Util.encode(data.getBytes());
        System.out.println("编码后生成的token：" + token);
        return token;

    }
    /**
     * 删除订单接口
     */
    public void del(String amazonOrderId){
//        String token=getToken(clientId,ApiSecret);
        NewAbroadLogisticsUtil.delOrder(amazonOrderId);

    }
    /**
     * 打印接口
     * @param amazonOrderId
     */
    public void print(String amazonOrderId){
        String token=getToken(xs_clientId,xs_ApiSecret);
        NewAbroadLogisticsUtil.printOrder(amazonOrderId);

    }

    /**
     * 修改预报订单的重量接口
     * @param
     */
    public void updateWeight(String requestdata){
        String token=getToken(xs_clientId,xs_ApiSecret);
        NewAbroadLogisticsUtil.updateOrderWeight(xs_yt_updateWeight,token,requestdata);

    }

    /**
     * 获取订单详情
     * @param orderNumber
     */
    public void getOrderInfo(String orderNumber){
        String token=getToken(xs_clientId,xs_ApiSecret);
        NewAbroadLogisticsUtil.getOrderInfo(xs_yt_getOrderInfo,token,orderNumber);
    }

    /**
     * 获取运费明细接口
     * @param orderNumber
     */
    public void getShippingFeeDetail(String orderNumber){
        String token=getToken(xs_clientId,xs_ApiSecret);
        NewAbroadLogisticsUtil.getShippingFeeDetail(xs_yt_getShippingFeeDetail,token,orderNumber);
    }
    /**
     * 推送接口
     * @param amazonOrderId
     */
    public void pushOrder(String amazonOrderId) {
        NewOrderEntity neworderEntity = newOrderService.selectOne(new EntityWrapper<NewOrderEntity>().eq("amazon_order_id", amazonOrderId));
        NewProductShipAddressEntity newProductShipAddressEntity = newProductShipAddressService.selectOne(new EntityWrapper<NewProductShipAddressEntity>().eq("amazon_order_id", amazonOrderId));

        ProductShipAddressEntity shipAddressEntity = productShipAddressService.selectOne(new EntityWrapper<ProductShipAddressEntity>().eq("order_id", newProductShipAddressEntity.getOrderId()));

        //推送--订单基本信息
        OrderRequestData omsOrder = new OrderRequestData();
        omsOrder.setOrderNumber(amazonOrderId);
        List<DomesticLogisticsEntity> domesticLogisticsEntitys = domesticLogisticsService.selectList(new EntityWrapper<DomesticLogisticsEntity>().eq("order_id", neworderEntity.getOrderId()));
        StringBuffer deanname = new StringBuffer("");
        StringBuffer supplyexpressno = new StringBuffer("");
        for (int i = 0; i < domesticLogisticsEntitys.size(); i++) {
            if (StringUtils.isNotBlank(domesticLogisticsEntitys.get(i).getLogisticsCompany())) {
                deanname.append(domesticLogisticsEntitys.get(i).getLogisticsCompany());
                deanname.append(",");
                supplyexpressno.append(domesticLogisticsEntitys.get(i).getWaybill());
                supplyexpressno.append(",");
            }
        }
        if (StringUtils.isNotBlank(deanname.toString())) {
            omsOrder.setShippingMethodCode(deanname.substring(0, deanname.length() - 1));//测试用的
        } else {
            omsOrder.setShippingMethodCode("SGRDGM");
        }
        omsOrder.setPackageNumber(neworderEntity.getOrderNumber());
        BigDecimal weight = new BigDecimal(1.0);
        omsOrder.setWeight(weight);
        omsOrder.setTrackingNumber(supplyexpressno.toString());
//        //设置时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS+08:00");
//        omsOrder.setOrder_date(sdf.format(orderEntity.getBuyDate()));
//        omsOrder.setOrder_memo(shipAddressEntity.getShipCountry());
        //推送--订单详情

        List<ApplicationInfos> omsOrderDetails = new ArrayList<>();
        List<NewOrderItemEntity> productOrderItemEntitys = newOrderItemService.selectList(new EntityWrapper<NewOrderItemEntity>().eq("amazon_order_id", neworderEntity.getAmazonOrderId()));
        for (NewOrderItemEntity productOrderItemEntity : productOrderItemEntitys) {
            ApplicationInfos omsOrderDetail = new ApplicationInfos();
            omsOrderDetail.setApplicationName(productOrderItemEntity.getProductTitle().length()>120? productOrderItemEntity.getProductTitle().substring(100):productOrderItemEntity.getProductTitle());
            omsOrderDetail.setQty(productOrderItemEntity.getOrderItemNumber());
            omsOrderDetail.setUnitPrice(productOrderItemEntity.getProductPrice());
            BigDecimal unitweight = new BigDecimal(1.0);//测试用
            omsOrderDetail.setUnitWeight(unitweight);
            omsOrderDetail.setProductUrl(productOrderItemEntity.getProductImageUrl());
            omsOrderDetail.setSku(productOrderItemEntity.getProductSku());
            omsOrderDetails.add(omsOrderDetail);

        }

        //推送—收货人信息
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setCountryCode(shipAddressEntity.getShipCountry());
        shippingInfo.setShippingFirstName(shipAddressEntity.getShipName());
        shippingInfo.setShippingAddress(shipAddressEntity.getShipAddressDetail());
        shippingInfo.setShippingCity(shipAddressEntity.getShipCity());
        shippingInfo.setShippingState(shipAddressEntity.getShipRegion());
        shippingInfo.setShippingZip(shipAddressEntity.getShipZip());
        shippingInfo.setShippingPhone(shipAddressEntity.getShipTel()==null?getTel():shipAddressEntity.getShipTel());
        ApplicationInfos[] applicationInfos = new ApplicationInfos[omsOrderDetails.size()];
        omsOrder.setShippingInfo(shippingInfo);
        omsOrder.setApplicationInfos(omsOrderDetails.toArray(applicationInfos));
        SenderInfo senderInfo = new SenderInfo();//默认空值，不是必填参数
        omsOrder.setSenderInfo(senderInfo);
        JSONArray omsOrderJson = JSONArray.fromObject(omsOrder);
        NewAbroadLogisticsUtil.pushOrder(omsOrderJson.toString());
    }

    @Test
    public  void test() {
        StringJoiner sj=new StringJoiner("","[\\\"","\\\"]");

//        Map<String,String> map=new HashMap<>();
        String amazon_order_id="{\"Type\":\"2\",\"OrderNumber\":\"028-8222473-1579520\"}";
        String str="304-5925957-4473168";
        sj.add(str);
        System.out.println(sj.toString());
//        pushOrder(str);
//          print("[\"YT1908821205006502\"]");
//          del(amazon_order_id);
//        updateWeight("{\"OrderNumber\":\"028-8222473-1579520\",\"Weight\":\"2.88\"}");
//         getOrderInfo("303-7832993-5749169");
//        getShippingFeeDetail("203-7095491-7680367");
    }


}
