package io.renren.modules.logistics.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.renren.modules.logistics.DTO.ReceiveOofayData;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
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

    public final static String pushUrl = "http://test.oofay.trylose.cn:60254/api/ExpressAccess/GetOrderDataFromOtherPlatform";

    public final static String detailUrl = "http://test.oofay.trylose.cn:60254/api/ExpressAccess/GetOrderDetailByOrderSn";

    public final static String updateUrl = "http://test.oofay.trylose.cn:60254/api/ExpressAccess/UpdateOrderDetailStatusByOrderSn";

    public final static String testUrl = "http://127.0.0.1:8081/renren-api/api/test";

    public final static String key = "5c0d54e1d5184c36921d7f08fa10dcae";

    public final static Base64.Decoder decoder = Base64.getDecoder();

    private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;

    public static void main(String[] args) {
        //推送订单
//        pushOrder("1");
        //获取订单国际物流状态
        getOrderDetail("20181214");
        //修改订单国际物流状态
//        updateOrder(20181214L,4);
//        String orderData = "{\"order\":{\"order_sn\":\"20181214\",\"order_currency\":\"null\",\"order_date\":\"null\",\"profitamount\":null,\"costamount\":null,\"feedamount\":null,\"delivery_address\":\"222-11\",\"order_memo\":\"US\",\"memo\":null,\"saleamount\":111111.0},\"orderDetailList\":[{\"product_id\":\"SKU\",\"price\":null,\"quantity\":1,\"cost\":null,\"profit\":null,\"supplyorderno\":null,\"supplyexpressno\":\"xxxxxxxx\",\"saleamount\":null,\"product_date\":null,\"is_electriferous\":false,\"is_liquid\":false}],\"address\":{\"customer\":\"aa\",\"custcompany\":\"bb\",\"custcountry\":\"UK\",\"custstate\":\"ggg\",\"custcity\":\"aa\",\"custzipcode\":\"1234342\",\"custphone\":\"1231\",\"custaddress\":\"aaadddddddddd\",\"address_line1\":\"aaa\",\"address_line2\":null,\"address_line3\":null}}";
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
                    //System.out.println("data1:" + data1);
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
                JSONObject detail = a.getJSONArray("OmsOrderDetailext").getJSONObject(0);
                //获取状态码
                receiveOofayData.setStatusStr(detail.getString("status"));
                //获取跟踪号
                receiveOofayData.setTrackWaybill(detail.getString("supply_express_no"));
                //获取运费(feedamount)
                receiveOofayData.setInterFreight(detail.getString("feedamount"));
                //获取是否有入库信息
                JSONArray recordList = detail.getJSONArray("warehousing_record_list");
                if(recordList.size() > 0 && recordList.getJSONObject(0).getString("storage_time") != null){
                    receiveOofayData.setWarehousing(true);
                }else{
                    receiveOofayData.setWarehousing(false);
                }
                JSONObject destInfo = detail.getJSONObject("ChannelQueryInfo");
                if(destInfo != null){
                    receiveOofayData.setDestTransportCompany(destInfo.getString("dest_transport_company"));
                    receiveOofayData.setDestChannel(destInfo.getString("dest_channel"));
                }
                map.put("code","true");
                map.put("receiveOofayData",receiveOofayData);
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