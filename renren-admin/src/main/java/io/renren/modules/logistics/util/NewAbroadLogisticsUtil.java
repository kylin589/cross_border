package io.renren.modules.logistics.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.renren.modules.logistics.DTO.OrderWayBill;
import io.renren.modules.logistics.entity.LogisticsChannelEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 对接新的物流接口的工具类
 */
public class NewAbroadLogisticsUtil {
    //客户编号
    public final static String clientId="C88888";
    //用户密钥
    public final static String ApiSecret="NMt9f54gz9M=";

    //测试接口地址：
    public final static String test_url="http://120.76.102.19:8034/LMS.API/api/";
    //推送订单
    public final static String pushUrl = test_url+"WayBill/BatchAdd";
    //打印接口
    public final static String printUrl = "http://120.76.102.19:8035/API/PrintUrl";
    //删除订单
    public final static String deleteUrl = test_url+"WayBill/DeleteCoustomerOrderInfo";

    //正式环境接口地址（云途）
    public final static String xs_yt_url="http://api.yunexpress.com/LMS.API/api/";
    //正式环境客户编号
    public final static String xs_clientId = "C95057";
    //正式环境客户密钥
    public final static String xs_ApiSecret = "kEhItRLDWxk=";
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

    /**
     * 随机生成电话号码的方法
     * @return
     */
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
     * 生成token的方法(Base86编码)
     * @param clientId
     * @param ApiSecret
     * @return
     */
    public static String  getToken(String clientId,String ApiSecret){
        String data=clientId+'&'+ApiSecret;
        System.out.println("生成的token转码前："+data);
        String token = Base64Util.encode(data.getBytes());
        System.out.println("编码后生成的token："+token);
        return token;

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
   public static void pushOrder(){
       Map<String, String> map = new HashMap<>();
       OkHttpClient client = new OkHttpClient();
       MediaType mediaType = MediaType.parse("application/json");
       String token = getToken(clientId, ApiSecret);
       RequestBody body = RequestBody.create(mediaType, "[{\n" +
               "\t\"applicationType\": 0,\n" +
               "\t\"orderNumber\": \"303-7832993-5749169\",\n" +
               "\t\"applicationInfos\": [{\n" +
               "\t\t\"unitPrice\": 108.01,\n" +
               "\t\t\"unitWeight\": 1,\n" +
               "\t\t\"hsCode\": \"\",\n" +
               "\t\t\"qty\": 1,\n" +
               "\t\t\"pickingName\": \"\",\n" +
               "\t\t\"remark\": \"\",\n" +
               "\t\t\"productUrl\": \"\",\n" +
               "\t\t\"sku\": \"PU-0L1R-SX40\",\n" +
               "\t\t\"applicationName\": \"Risingmed Tragbarer Medizin-Kühlschrank und Insulin-Kühler für Auto, Reise, Zuhause - Mini Drogen konstante Temperatur Kühlschrank Drogenreefer 2-8 °C, kleine Reisebox für Medikamente, Weiß\"\n" +
               "\t}],\n" +
               "\t\"transactionNumber\": \"\",\n" +
               "\t\"length\": 0,\n" +
               "\t\"weight\": 1,\n" +
               "\t\"insuranceType\": 0,\n" +
               "\t\"sensitiveTypeID\": 0,\n" +
               "\t\"packageNumber\": 1,\n" +
               "\t\"sourceCode\": \"\",\n" +
               "\t\"insureAmount\": 0,\n" +
               "\t\"shippingMethodCode\": \"GBZXR\",\n" +
               "\t\"width\": 0,\n" +
               "\t\"senderInfos\": [],\n" +
               "\t\"enableTariffPrepay\": false,\n" +
               "\t\"shippingInfos\": [{\n" +
               "\t\t\"shippingCity\": \"Angermünde\",\n" +
               "\t\t\"shippingCompany\": \"\",\n" +
               "\t\t\"shippingLastName\": \"\",\n" +
               "\t\t\"shippingPhone\": \"\",\n" +
               "\t\t\"shippingAddress2\": \"\",\n" +
               "\t\t\"shippingAddress1\": \"\",\n" +
               "\t\t\"countryCode\": \"DE\",\n" +
               "\t\t\"shippingFirstName\": \"Seminarhaus Breitenteicher Mühle\",\n" +
               "\t\t\"shippingTaxId\": \"\",\n" +
               "\t\t\"shippingAddress\": \"DE--Angermünde-16278\",\n" +
               "\t\t\"shippingState\": \"\",\n" +
               "\t\t\"shippingZip\": \"16278\"\n" +
               "\t}],\n" +
               "\t\"trackingNumber\": \"\",\n" +
               "\t\"return\": false,\n" +
               "\t\"height\": 0\n" +
               "}]");
       Request request = new Request.Builder()
               .url("http://120.76.102.19:8034/LMS.API/api/WayBill/BatchAdd")
               .post(body)
               .addHeader("Content-Type", "application/json")
               .addHeader("Authorization", "Basic " + token)
               .addHeader("cache-control", "no-cache")
               .build();

       try {
           Response response = client.newCall(request).execute();
           String result = response.body().string();
           if(result.contains("Exception")){
               System.out.println("输出响应结果" + result);
               map.put("code", "false");
               map.put("msg", "接口异常");
           }else {
               System.out.println("输出响应结果" + result);
               map.put("result", result);
           }
       } catch (IOException e) {
           map.put("code", "false");
           map.put("msg", "网络故障，请重试。");
       }

   }

   public static Map<String,String> delOrder(String jsonStr){
       Map<String, String> map = new HashMap<>();
       OkHttpClient client = new OkHttpClient();
       MediaType mediaType = MediaType.parse("application/json");
       RequestBody body = RequestBody.create(mediaType, jsonStr);
       Request request = new Request.Builder()
               .url(xs_yt_deleteUrl)
               .post(body)
               .addHeader("Content-Type", "application/json")
               .addHeader("Authorization", "Basic " + getXsToken(xs_clientId,xs_ApiSecret))
               .addHeader("cache-control", "no-cache")
               .build();

       try {
           Response response = client.newCall(request).execute();
           String result = response.body().string();
           if(result.contains("Exception")){
               System.out.println("输出响应结果" + result);
               map.put("code", "false");
               map.put("msg", "接口异常");
           }else {
               System.out.println("输出响应结果" + result);
               {
                   JSONObject jsonObject = JSONObject.fromObject(result);
                   if(jsonObject.get("ResultCode").equals("5001")){
                      Iterator iterator=jsonObject.keys();
                      while (iterator.hasNext()){
                          String key=(String) iterator.next();
                          String value=jsonObject.getString(key);
                          if(key.equals("Rueslt") && !"5012".equals(value)){
                             if("ErrorMeesage".equals(key)){
                                 map.put("code","false");
                                 map.put("msg",jsonObject.getString(key));
                             }
                          }else{
                              if("ErrorMeesage".equals(key)) {
                                  map.put("code", "true");
                                  map.put("msg", jsonObject.getString(key));
                              }
                          }
                      }

                   }

               }

               map.put("result", result);
           }
       } catch (IOException e) {
           map.put("code", "false");
           map.put("msg", "网络故障，请重试。");
       }
         return map;
   }


    /**
     * 打印接口
     * @param
     * @param
     * @param orderID
     */
    public static void printOrder(String orderID){
        Map<String, String> map = new HashMap<>();
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, orderID);
        Request request = new Request.Builder()
                .url(xs_yt_printUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic " + getXsToken(xs_clientId,xs_ApiSecret))
                .addHeader("cache-control", "no-cache")
                .build();

        try {
                 Response response = client.newCall(request).execute();
                 String result = response.body().string();
                 System.out.println("输出响应结果" + result);
                  if(StringUtils.isNotBlank(result)){
                     JSONObject jsonObject = JSONObject.fromObject(result);
                     if(jsonObject.get("ResultCode").equals("0000")){
                         JSONArray jsonArray = jsonObject.getJSONArray("Item");
                         for (int i=0;i<jsonArray.size();i++) {
                             String str=jsonArray.get(i).toString();
                             if(str.contains("Url")){
                                 JSONObject jsonObject2 = JSONObject.fromObject(str);
                                 Iterator iterator = jsonObject2.keys();
                                 while(iterator.hasNext()){
                                     String key = (String) iterator.next();
                                     String value = jsonObject2.getString(key);
                                     if(key.equals("Url")){
                                         System.out.println(value);
                                         map.put("url",value);
                                         break;
                                     }
                                 }

                             }

                         }

                     }

                 }
        } catch (IOException e) {
                 map.put("code", "false");
                 map.put("msg", "网络故障，请重试。");
        }

    }
    /**
     * 订单详情
     * @param url
     * @param token
     * @param
     */
    public static Map<String,String>  getOrderInfo(String url,String token,String orderNumber){
        if("".equals(url) && "".equals(token)){
            url=xs_yt_getOrderInfo;
            token=getXsToken(xs_clientId,xs_ApiSecret);
        }
        Map<String, String> map = new HashMap<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+orderNumber)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic "+token)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            System.out.println("输出响应结果" + result);
            if(StringUtils.isNotBlank(result)){
                JSONObject jsonObject = JSONObject.fromObject(result);
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    if(key.equals("ResultCode")){
                        String value = jsonObject.getString(key);
                        if(value.equals("0000")){
                            System.out.println("提交成功");
                        }
                    }
                    if(key.equals("Item")){
                        String value = jsonObject.getString(key);
                        System.out.println(value);
                        JSONObject jsonObject2 = JSONObject.fromObject(value);
                        Iterator iterator2 = jsonObject2.keys();
                        while (iterator2.hasNext()){
                            String key2 = (String) iterator2.next();
                            if(key2.equals("WayBillNumber")){
                                map.put("WayBillNumber",jsonObject2.getString(key2));
                            }
                            if(key2.equals("TrackingNumber")){
                                map.put("TrackingNumber",jsonObject2.getString(key2));
                            }
                            if(key2.equals("OrderNumber")){
                                map.put("OrderNumber",jsonObject2.getString(key2));
                            }
                        }

                    }

                }
            }
        } catch (IOException e) {
            map.put("code", "false");
            map.put("msg", "网络故障，请重试。");
        }
      return map;
    }

    /**
     * 获取运费明细
     * @param url
     * @param token
     * @param
     */
    public static Map<String, String> getShippingFeeDetail(String url, String token, String orderNumber){
        Map<String, String> map = new HashMap<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+orderNumber)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic "+token)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            System.out.println("输出响应结果" + result);
            if(StringUtils.isNotBlank(result)){
                JSONObject jsonObject = JSONObject.fromObject(result);
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()){
                    String key=(String)iterator.next();
                    String value=jsonObject.getString(key);
                    if(key.equals("ResultCode")&& value.equals("1006")){
                        map.put("msg","未找到数据查无数据");
                        map.put("code", "false");
                    }if(key.equals("ResultCode")&& value.equals("0000")){
                        System.out.println("提交成功");
                    }
                    if(key.equals("Item")){
                        String value2 = jsonObject.getString(key);
                        JSONObject jsonObject2 = JSONObject.fromObject(value2);
                        Iterator iterator2=jsonObject2.keys();
                        while (iterator2.hasNext()){
                            String key2 = (String) iterator2.next();
                            if(key2.equals("CustomerOrderNumber")){//订单号
                                map.put("CustomerOrderNumber",jsonObject2.getString(key2));
                            }if(key2.equals("TrackingNumber")){//追踪号
                                map.put("TrackingNumber",jsonObject2.getString(key2));
                            }if(key2.equals("WayBillNumber")){//运单号
                                map.put("WayBillNumber",jsonObject2.getString(key2));
                            }if(key2.equals("ShippingMethodName")){
                                map.put("ShippingMethodName",jsonObject2.getString(key2));
                            } if(key2.equals("CountryCode")){
                                map.put("CountryCode",jsonObject2.getString(key2));
                            } if(key2.equals("CountryName")){
                                map.put("CountryName",jsonObject2.getString(key2));
                            } if(key2.equals("NetWeight")){
                                map.put("NetWeight",jsonObject2.getString(key2));
                            } if(key2.equals("ChargeWeight")){//计费重
                                map.put("ChargeWeight",jsonObject2.getString(key2));
                            }if(key2.equals("OccurrenceTime")){
                                map.put("OccurrenceTime",jsonObject2.getString(key2));
                            }if(key2.equals("Freight")){//运费
                                map.put("Freight",jsonObject2.getString(key2));
                            }if(key2.equals("FuelSurcharge")){//燃油费
                                map.put("FuelSurcharge",jsonObject2.getString(key2));
                            }if(key2.equals("RegistrationFee")){//挂号费
                                map.put("RegistrationFee",jsonObject2.getString(key2));
                            }if(key2.equals("ProcessingFee")){//处理费
                                map.put("ProcessingFee",jsonObject2.getString(key2));
                            }if(key2.equals("OtherFee")){//其他杂费
                                map.put("OtherFee",jsonObject2.getString(key2));
                            }if(key2.equals("TotalFee")){//总费用
                                map.put("TotalFee",jsonObject2.getString(key2));
                            }

                        }

                    }
                }
            }

        } catch (IOException e) {
            map.put("code", "false");
            map.put("msg", "网络故障，请重试。");
        }
        return map;
    }

    /**
     * 修改订单预报重量
     * @param url
     * @param token
     * @param
     */
    public static void updateOrderWeight(String url,String token,String data){
        Map<String, String> map = new HashMap<>();
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic " + token)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            System.out.println("输出响应结果" + result);
        } catch (IOException e) {
            map.put("code", "false");
            map.put("msg", "网络故障，请重试。");
        }

    }


    /**
     * 推送订单接口
     * @param orderdata
     * @return
     */
    public static Map<String,String> pushOrder(String orderdata) {
        System.out.println("orderdata:" + orderdata);
        Map<String, String> map = new HashMap<>();
        OkHttpClient client = new OkHttpClient();
        String token = getXsToken(xs_clientId, xs_ApiSecret);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, orderdata);
        Request request = new Request.Builder()
                .url(xs_yt_pushUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic " + token)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                System.out.println("输出响应结果" + result);
                if(StringUtils.isNotBlank(result)){
                    JSONObject jsonObject = JSONObject.fromObject(result);
                    Iterator iterator = jsonObject.keys();
                    while(iterator.hasNext()) {
                        String key=(String)iterator.next();
                        String value=jsonObject.getString(key);
                        if (key.equals("ResultCode") && !"0000".equals(value)) {
                            map.put("code", "false");
                        }
                        if (key.equals("ResultCode") && "0000".equals(value)) {
                            System.out.println("提交成功");
                        }
                        if(key.equals("Item")){
                            String value2 = jsonObject.getString(key);
                            System.out.println(value2);
                            Type type=new TypeToken<List<OrderWayBill>>(){}.getType();
                            List<OrderWayBill> list=new Gson().fromJson(value2,type);
                            if(list != null && list.size() >0){
                                if(list.get(0) != null){
                                    map.put("msg",list.get(0).getFeedback());
                                    map.put("Status",list.get(0).getStatus());//运单请求 1-请求成功，0-请求失败
//                                map.put("TrackStatus",orderWayBill.getTrackStatus());//获得追踪号状态 1-已产生跟踪号 2-等待后续更新跟踪号 3-不需要跟踪号
                                    map.put("wayBillNumber",list.get(0).getWayBillNumber());//获得运单号
                                    if(list.get(0).getTrackStatus().equals("1")) {
                                        map.put("TrackNumber", list.get(0).getTrackingNumber());
                                    }else{
                                        map.put("TrackNumber",null);
                                    }
                                }else{
                                    map.put("Status","0");//运单请求 1-请求成功，0-请求失败

                                }

                            }

                        }
                    }
                }

        } catch (IOException e) {
            map.put("code", "false");
            map.put("msg", "网络故障，请重试。");
        }
        return  map;
    }



    public static void main(String[] args) {
//        pushOrder();
//          printOrder();
//        getOrderInfo("","","303-7832993-5749169");

    }

}
