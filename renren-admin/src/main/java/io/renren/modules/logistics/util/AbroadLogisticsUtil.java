package io.renren.modules.logistics.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.swjtu.lang.LANG;
import io.renren.common.utils.R;
import io.renren.modules.logistics.DTO.ReceiveOofayData;
import io.renren.modules.logistics.DTO.SalePriceDetail;
import io.renren.modules.util.TranslateUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: wdh
 * @Date: 2018/12/12 10:02
 * @Description:
 */
public class AbroadLogisticsUtil {
    //用户标识
    public final static Long userdean = 238370684928L;

    //测试接口地址：http://test.oofay.trylose.cn:60254/
    //正式接口地址：http://oofay.trylose.cn:60254/

    public final static String pushUrl = "http://oofay.trylose.cn:60254/api/ExpressAccess/GetOrderDataFromOtherPlatform";

    public final static String detailUrl = "http://oofay.trylose.cn:60254/api/ExpressAccess/GetOrderDetailByOrderSn";

    public final static String updateUrl = "http://oofay.trylose.cn:60254/api/ExpressAccess/UpdateOrderDetailStatusByOrderSn";

    public final static String salePriceUrl = "http://oofay.trylose.cn:60254/api/ExpressAccess/CalculateAllCountryPrice";

    public final static String updateWaybillUrl = "http://oofay.trylose.cn:60254/api/ExpressAccess/UpdateOrderDetailSupplyExpressNoByOrderSn";

    public final static String updateRemarkUrl = "http://oofay.trylose.cn:60254/api/ExpressAccess/UpdateOrderRemarkBySn";

    public final static String trackWaybillUrl = "http://oofay.trylose.cn:60254/api/ExpressAccess/GetTrackNumberByOrderSn";

    public final static String testUrl = "http://oofay.trylose.cn:60254/renren-api/api/test";

    //测试key
//    public final static String key = "5c0d54e1d5184c36921d7f08fa10dcae";
    //正式key
    public final static String key = "8e44b19aeb154ab0befce731e08eb469";

    public final static Base64.Decoder decoder = Base64.getDecoder();

    private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;

    public static void main(String[] args) {
        //推送订单
//        pushOrder("1");
        //获取订单国际物流状态
        getOrderDetail("408-8886970-1570715");
        //获取物流价格
//        getSaleDetail(1L);
        //修改订单国际物流状态
//        updateOrder(20181214L,4);
        //修改订单国内跟踪号
//        updateOrderWaybill("302-7660577-9242718","666666,88888888");
        //修改订单备注
//        updateOrderRemark("302-7660577-9242718","订单中途遇到了问题1");
        //获取转单号
//        getTrackWaybill("305-1302253-62275471");
//        String orderData = "{\"order\":{\"order_sn\":\"20181214\" ,\"order_currency\":\"null\",\"order_date\":\"null\",\"profitamount\":null,\"costamount\":null,\"feedamount\":null,\"delivery_address\":\"222-11\",\"order_memo\":\"US\",\"memo\":null,\"saleamount\":111111.0},\"orderDetailList\":[{\"product_id\":\"SKU\",\"price\":null,\"quantity\":1,\"cost\":null,\"profit\":null,\"supplyorderno\":null,\"supplyexpressno\":\"xxxxxxxx\",\"saleamount\":null,\"product_date\":null,\"is_electriferous\":false,\"is_liquid\":false}],\"address\":{\"customer\":\"aa\",\"custcompany\":\"bb\",\"custcountry\":\"UK\",\"custstate\":\"ggg\",\"custcity\":\"aa\",\"custzipcode\":\"1234342\",\"custphone\":\"1231\",\"custaddress\":\"aaadddddddddd\",\"address_line1\":\"aaa\",\"address_line2\":null,\"address_line3\":null}}";
//        System.out.println(orderData);
    }
    public static Map<String,String> pushOrder(String orderData){
        Map<String,String> map = new HashMap<>();
        //传输参数
        Map<String, String> paramList = new HashMap<>();
        //获取秘钥
        Map<String, String> paramsMap = new HashMap<>();
        paramList.put("userdean", String.valueOf(userdean));
        paramsMap.put("userdean", String.valueOf(userdean));
        paramList.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
        paramsMap.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
//        String orderData = "{\"order\":{\"order_sn\":\"20181214\",\"order_currency\":\"USD\",\"order_date\":\"2018-11-12T17:01:47.0948075+08:00\",\"profitamount\":null,\"costamount\":null,\"feedamount\":null,\"delivery_address\":\"222-11\",\"order_memo\":\"US\",\"memo\":null,\"saleamount\":111111.0},\"orderDetailList\":[{\"product_id\":\"SKU\",\"price\":null,\"quantity\":1,\"cost\":null,\"profit\":null,\"supplyorderno\":null,\"supplyexpressno\":\"xxxxxxxx\",\"saleamount\":null,\"product_date\":null,\"is_electriferous\":false,\"is_liquid\":false}],\"address\":{\"customer\":\"aa\",\"custcompany\":\"bb\",\"custcountry\":\"UK\",\"custstate\":\"ggg\",\"custcity\":\"aa\",\"custzipcode\":\"1234342\",\"custphone\":\"1231\",\"custaddress\":\"aaadddddddddd\",\"address_line1\":\"aaa\",\"address_line2\":null,\"address_line3\":null}}";
//        orderData = "{\"order\":{\"order_currency\":\"USD\",\"order_sn\":\"2018122203\",\"order_date\":\"2018-12-12T15:44:29.0000000+08:00\",\"profitamount\":0,\"costamount\":\"\",\"feedamount\":0,\"delivery_address\":\"\",\"order_memo\":\"US\",\"memo\":\"\",\"saleamount\":0},\"orderDetailList\":[{\"product_id\":\"LKJHGFDSA3\",\"price\":0,\"quantity\":1,\"cost\":0,\"profit\":0,\"supplyorderno\":\"\",\"supplyexpressno\":\"803216550758084158\",\"saleamount\":0,\"product_date\":null,\"is_electriferous\":false,\"is_liquid\":false}],\"address\":{\"customer\":\"wangdh\",\"custcompany\":\"sdf\",\"custcountry\":\"US\",\"custstate\":\"aaa\",\"custcity\":\"nuyork\",\"custzipcode\":\"030000\",\"custphone\":\"18334784662\",\"custaddress\":\"gfhd1gfhd2gfhd3\",\"address_line1\":\"gfhd1\",\"address_line2\":\"gfhd2\",\"address_line3\":\"gfhd3\"}}";

        paramsMap.put("oofayOrderData",orderData);
        String oofayOrderData = null;
        try {
            oofayOrderData = URLEncoder.encode(orderData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        paramList.put("oofayOrderData", oofayOrderData);
        String asin = null;
        String result = null;
        //参数根据ASCII码排序并加密得到签名
        try {
            asin = getSign(paramsMap);
            if(asin != null && asin != ""){
                paramList.put("asin",asin);
                String params = proData(paramList);
                //开始推送数据
                result = sendPost(pushUrl,params);
                System.out.println("result:" + result);
                JSONObject obj = JSONObject.parseObject(result);
                String data = obj.getString("data");
                if(data != null && data != ""){
                    String data1 = new String(decoder.decode(data), "UTF-8");
                    System.out.println("data1:" + data1);
                    JSONObject j = JSONObject.parseObject(data1);
                    map.put("code",j.getString("isSuccess"));
                    map.put("msg",j.getString("errorMsg"));
                }else{
                    map.put("code","false");
                    map.put("msg",obj.getString("Message"));
                }
            }
        } catch (Exception e) {
            map.put("code","false");
            map.put("msg","网络故障，请重试。");
        }
        return map;
    }

    public static Map<String,Object> getOrderDetail(String amazonOrderId){
        Map<String,Object> map = new HashMap<>();
        //传输参数
        Map<String, String> paramList = new HashMap<>();
        paramList.put("userdean", String.valueOf(userdean));
        paramList.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
        paramList.put("orderSn",amazonOrderId);
        String asin = null;
        String result = null;
        //参数根据ASCII码排序并加密得到签名
        try {
            asin = getSign(paramList);
            if(asin != null && asin != ""){
                paramList.put("asin",asin);
                String params = proData(paramList);
                //开始推送数据
                result = sendPost(detailUrl,params);
                System.out.println("result:" + result);
                JSONObject obj = JSONObject.parseObject(result);
                String data = obj.getString("data");
                String data1 = new String(decoder.decode(data), "UTF-8");
                System.out.println("data1:" + data1);
                JSONObject a = JSONObject.parseObject(data1);
                ReceiveOofayData receiveOofayData = new ReceiveOofayData();
                JSONObject omsOrder = a.getJSONObject("oms_order");

                JSONObject omsOrderDetailext = a.getJSONArray("OmsOrderDetailext").getJSONObject(0);
//                System.out.println("omsOrder:" + omsOrder.toString());
//                System.out.println("omsOrderDetailext:" + omsOrderDetailext.toString());
                JSONArray omsExpressList = a.getJSONArray("oms_Express");
//                System.out.println("oms_Express:" + omsExpressList);
                //获取状态码
                String status = omsOrderDetailext.getString("status");
                receiveOofayData.setStatusStr(omsOrderDetailext.getString("status"));
                //获取运费
                String feedamount = omsOrder.getString("feedamount");
//                System.out.println("运费：" + feedamount);
                receiveOofayData.setInterFreight(omsOrder.getString("feedamount"));

                //获取是否有仓库已入库信息
                JSONArray recordList = omsOrderDetailext.getJSONArray("warehousing_record_list");
                if(recordList.size() > 0 && recordList.getJSONObject(0).getString("storage_time") != null){
                    receiveOofayData.setWarehousing(true);
                }else{
                    receiveOofayData.setWarehousing(false);
                }
                JSONObject destInfo = a.getJSONObject("ChannelQueryInfo");
//                System.out.println("destInfo:" + destInfo.toString());
                if(destInfo != null){
                    //国际物流公司
                    receiveOofayData.setDestTransportCompany(destInfo.getString("dest_transport_company"));
                    //物流方式
                    receiveOofayData.setDestChannel(destInfo.getString("dest_channel"));
                    //目的地查询网址
                    receiveOofayData.setDestQueryUrl(destInfo.getString("dest_query_url"));
                    //服务查询网址
                    receiveOofayData.setServiceQueryUrl(destInfo.getString("service_query_url"));
                    //联系电话
                    receiveOofayData.setMobile(destInfo.getString("mobile"));
                    //获取国内跟踪号
                    receiveOofayData.setDomesticTrackWaybill(omsOrderDetailext.getString("deliveryexpressno"));
                }
                if(omsExpressList.size() >0 ){
                    //发货时间
                    receiveOofayData.setShipTime(omsExpressList.getJSONObject(0).getString("expressdate"));
                }
                map.put("code","true");
                map.put("receiveOofayData",receiveOofayData);
                System.out.println("receiveOofayData:" + receiveOofayData);
            }
        } catch (Exception e) {
            map.put("code","false");
            map.put("msg","网络故障，请重试。");
        }
        return map;
    }

    /**
     *
     * @param orderId
     * @param status
     * 1备货；2，缺货；4.问题；3发货；5退款；6妥投；10物流问题
     */
    public static void updateOrder(Long orderId, int status){
        //传输参数
        Map<String, String> paramList = new HashMap<>();
        paramList.put("userdean", String.valueOf(userdean));
        paramList.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
        paramList.put("orderSn",String.valueOf(orderId));
        paramList.put("status",String.valueOf(status));
        String asin = null;
        String result = null;
        //参数根据ASCII码排序并加密得到签名
        try {
            asin = getSign(paramList);
            if(asin != null && asin != ""){
                paramList.put("asin",asin);
                String params = proData(paramList);
                //开始推送数据
                result = sendPost(updateUrl,params);
                System.out.println("result:" + result);
                JSONObject obj = JSONObject.parseObject(result);
                String data = obj.getString("data");
                String data1 = new String(decoder.decode(data), "UTF-8");
                System.out.println("data1:" + data1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param amazonOrderId
     * @param waybill
     * 修改订单国内跟踪号
     */
    public static void updateOrderWaybill(String amazonOrderId, String waybill){
        //传输参数
        Map<String, String> paramList = new HashMap<>();
        paramList.put("userdean", String.valueOf(userdean));
        paramList.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
        paramList.put("orderSn",amazonOrderId);
        paramList.put("supplyExpressNo",waybill);
        String asin = null;
        String result = null;
        //参数根据ASCII码排序并加密得到签名
        try {
            asin = getSign(paramList);
            if(asin != null && asin != ""){
                paramList.put("asin",asin);
                String params = proData(paramList);
                //开始推送数据
                result = sendPost(updateWaybillUrl,params);
                System.out.println("result:" + result);
                JSONObject obj = JSONObject.parseObject(result);
                String data = obj.getString("data");
                String data1 = new String(decoder.decode(data), "UTF-8");
                System.out.println("data1:" + data1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param amazonOrderId
     * @param remark
     * 修改订单备注
     */
    public static void updateOrderRemark(String amazonOrderId, String remark){
        //传输参数
        Map<String, String> paramList = new HashMap<>();
        paramList.put("userdean", String.valueOf(userdean));
        paramList.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
        paramList.put("orderSn",amazonOrderId);
        paramList.put("memo",remark);
        String asin = null;
        String result = null;
        //参数根据ASCII码排序并加密得到签名
        try {
            asin = getSign(paramList);
            if(asin != null && asin != ""){
                paramList.put("asin",asin);
                String params = proData(paramList);
                //开始推送数据
                result = sendPost(updateRemarkUrl,params);
                System.out.println("result:" + result);
                JSONObject obj = JSONObject.parseObject(result);
                String data = obj.getString("data");
                String data1 = new String(decoder.decode(data), "UTF-8");
                System.out.println("data1:" + data1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> getSaleDetail(BigDecimal weight){
        Map<String,String> map = new HashMap<>();
        //传输参数
        Map<String, String> paramList = new HashMap<>();
        paramList.put("userdean", String.valueOf(userdean));
        paramList.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
        SalePriceDetail salePriceDetail = new SalePriceDetail();
        salePriceDetail.setItem_weight(weight);
        net.sf.json.JSONObject salePriceJson = net.sf.json.JSONObject.fromObject(salePriceDetail);
        System.out.println("1111111" + salePriceJson.toString());
        paramList.put("priceParam",salePriceJson.toString());
        String asin = null;
        String result = null;
        //参数根据ASCII码排序并加密得到签名
        try {
            asin = getSign(paramList);
            if(asin != null && asin != ""){
                paramList.put("asin",asin);
                String params = proData(paramList);
                //开始推送数据
                result = sendPost(salePriceUrl,params);
                System.out.println("result:" + result);
                JSONObject obj = JSONObject.parseObject(result);
                String data = obj.getString("data");
                String data1 = new String(decoder.decode(data), "UTF-8");
                JSONObject a = JSONObject.parseObject(data1);
                JSONArray array = a.getJSONArray("otherData");
                for(int i = 0; i < array.size(); i++){
                    JSONObject o = array.getJSONObject(i);
                    String country = o.getString("CountryName");
                    String freight = o.getString("DeliveryCost");
                    switch (country){
                        case "美国":
                            map.put("americanFreight",freight);
                            break;
                        case "加拿大":
                            map.put("canadaFreight",freight);
                            break;
                        case "墨西哥":
                            map.put("mexicoFreight",freight);
                            break;
                        case "英国":
                            map.put("britainFreight",freight);
                            break;
                        case "法国":
                            map.put("franceFreight",freight);
                            break;
                        case "德国":
                            map.put("germanyFreight",freight);
                            break;
                        case "意大利":
                            map.put("italyFreight",freight);
                            break;
                        case "西班牙":
                            map.put("spainFreight",freight);
                            break;
                        case "日本":
                            map.put("japanFreight",freight);
                            break;
                        case "澳大利亚":
                            map.put("australiaFreight",freight);
                            break;
                        default:
                            break;
                    }
                }
                map.put("code","true");
            }
        } catch (Exception e) {
            map.put("code","false");
            map.put("msg","网络故障，请重试。");
        }
        return map;
    }

    public static Map<String,String> getTrackWaybill(String amazonOrderId){
        Map<String,String> map = new HashMap<>();
        //传输参数
        Map<String, String> paramList = new HashMap<>();
        paramList.put("userdean", String.valueOf(userdean));
        paramList.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
        paramList.put("orderSn",amazonOrderId);
        String asin = null;
        String result = null;
        //参数根据ASCII码排序并加密得到签名
        try {
            asin = getSign(paramList);
            if(asin != null && asin != ""){
                paramList.put("asin",asin);
                String params = proData(paramList);
                //开始推送数据
                result = sendPost(trackWaybillUrl,params);
                System.out.println("result:" + result);
                JSONObject obj = JSONObject.parseObject(result);
                String data = obj.getString("data");
                String data1 = new String(decoder.decode(data), "UTF-8");
                System.out.println("data1:" + data1);
                JSONObject a = JSONObject.parseObject(data1);
                String trackWaybill = a.getString("otherData");
                if(StringUtils.isNotBlank(trackWaybill)){
                    map.put("code","true");
                    map.put("trackWaybill",trackWaybill);
                }else{
                    map.put("code","false");
                    map.put("msg","没有获取到转单号");
                }
            }
        } catch (Exception e) {
            map.put("code","false");
            map.put("msg","网络故障，请重试。");
        }
        return map;
    }

    /**
     * 获取UTC时间戳
     * @return
     */
    public static long getTimestampByLocalTimeToTotalSeconds() {
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date glDate = null;
        try {
            glDate = dateFormat1.parse("1970-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getSecondTimestampTwo(getUTCTime()) - getSecondTimestampTwo(glDate);
    }
    /**
     * 获取精确到秒的时间戳
     * @param date
     * @return
     */
    public static Long getSecondTimestampTwo(Date date){
        if (null == date) {
            return 0L;
        }
        String timestamp = String.valueOf(date.getTime()/1000);
        return Long.valueOf(timestamp);
    }
    public static Date getUTCTime() {
        StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance() ;
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day) ;
        UTCTimeBuffer.append(" ").append(hour).append(":").append(minute) ;
        Date date = null;
        try{
            System.out.println("UTCTimeBuffer:::" + UTCTimeBuffer);
            date = format.parse(UTCTimeBuffer.toString()) ;
            return date;
        }catch(ParseException e)
        {
            e.printStackTrace() ;
        }
        return null ;
    }
    /**
     * @Author : wangdh
     * @Description :根据签名算法得出签名---参数按照参数名ASCII码从小到大排序
     * @Date : 10:55 2018/12/12
     **/
    public static String getSign(Map<String, String> map) {
        String result = "";
        try {
            List<Map.Entry<String, String>> params = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(params, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : params) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }
            }
            sb.append("key=" + key);
            System.out.println("加密数据：" + sb.toString());
            result = DigestUtils.md5Hex(sb.toString());
            System.out.println("加密后数据：" + result);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * 封装参数
     */
    public static String proData(Map<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        //所有参与传参的参数按照accsii排序（升序）
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.println("推送数据：" + sb.toString());
        return sb.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}