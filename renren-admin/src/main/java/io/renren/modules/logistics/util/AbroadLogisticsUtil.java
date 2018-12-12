package io.renren.modules.logistics.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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

    public final static String key = "5c0d54e1d5184c36921d7f08fa10dcae";

    public final static Base64.Decoder decoder = Base64.getDecoder();

    public static void main(String[] args) {
        pottingData();
    }

    public static void pottingData(){
        Map<String, String> paramList = new HashMap<>();
        paramList.put("userdean", String.valueOf(userdean));
        paramList.put("timestamp", String.valueOf(getTimestampByLocalTimeToTotalSeconds()));
        paramList.put("oofayOrderData", "{\"order\":{\"order_sn\":\"11\",\"order_currency\":\"USD\",\"order_date\":\"2018-11-12T17:01:47.0948075+08:00\",\"profitamount\":null,\"costamount\":null,\"feedamount\":null,\"delivery_address\":\"222-11\",\"order_memo\":\"US\",\"memo\":null,\"saleamount\":111111.0},\"orderDetailList\":[{\"product_id\":\"SKU\",\"price\":null,\"quantity\":1,\"cost\":null,\"profit\":null,\"supplyorderno\":null,\"supplyexpressno\":\"xxxxxxxx\",\"saleamount\":null,\"product_date\":null,\"is_electriferous\":false,\"is_liquid\":false}],\"address\":{\"customer\":\"aa\",\"custcompany\":\"bb\",\"custcountry\":\"UK\",\"custstate\":\"ggg\",\"custcity\":\"aa\",\"custzipcode\":\"1234342\",\"custphone\":\"1231\",\"custaddress\":\"aaadddddddddd\",\"address_line1\":\"aaa\",\"address_line2\":null,\"address_line3\":null}}");
        String sign = null;
        JSONObject result = null;
        //参数根据ASCII码排序并加密得到签名
        try {
            sign = createSign(paramList);
            if(sign != null && sign != ""){
                paramList.put("sign",sign);
                //开始推送数据
                result = doPost(pushUrl,paramList);
                System.out.println("result:" + result);
                String data = result.getString("data");
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
        return getSecondTimestampTwo(new Date()) - getSecondTimestampTwo(glDate);
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
    /**
     * @Author : wangdh
     * @Description :根据签名算法得出签名---参数按照参数名ASCII码从小到大排序
     * @Date : 10:55 2018/12/12
     **/
    public static String createSign(Map<String, String> parameters) throws Exception {
        StringBuffer sb = new StringBuffer();
        //所有参与传参的参数按照accsii排序（升序）
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if (null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        System.out.println("parameters:" + parameters.toString());
        System.out.println("sb:" + sb.toString());
        //MD5加密,结果转换为大写字符
        String sign = md5(sb.toString(), key);
        return sign;

    }
    /**
     * MD5加密方法
     *
     * @param text 明文
     * @param key 密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key) throws Exception {
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(text + key);
        System.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        return encodeStr;
    }
    /**
     * post请求（用于请求json格式的参数）
     * @param url
     * @param paramsMap
     * @return
     */
    public static JSONObject doPost(String url, Map<String,String> paramsMap){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONObject jsonObject = null;
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        //设置参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Iterator iterator = paramsMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
        }
        CloseableHttpResponse response = null;
        String jsonString = null;
        try {
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charSet);
                httpPost.setEntity(entity);
            }
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            System.out.println("状态码：" + status.getStatusCode());
            HttpEntity responseEntity = response.getEntity();
            jsonString = EntityUtils.toString(responseEntity);

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jsonObject = JSONObject.parseObject(jsonString);
        return jsonObject;
    }

}
