package io.renren.modules.product.component;

import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.product.entity.AmazonRateEntity;
import io.renren.modules.product.service.AmazonRateService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Auther: wdh
 * @Date: 2018/12/9 10:40
 * @Description:
 */

@Component("rateTimer")
public class RateTimer {

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY ="4bfd1ee66fc20f5840c254ba5f638454";

    @Autowired
    private AmazonRateService amazonRateService;

    public void rateUpdate() {
        List<AmazonRateEntity> list = getRateList();
        amazonRateService.insertOrUpdateBatch(list);
    }



    //1.常用汇率查询
    public static JSONObject getRequest1(){
        String result =null;
        String url ="http://op.juhe.cn/onebox/exchange/query";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        JSONObject object = null;
        try {
            result =net(url, params, "GET");
            object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println(object.get("result"));
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    //2.货币列表
    public static JSONObject getRequest2(){
        String result =null;
        String url ="http://op.juhe.cn/onebox/exchange/list";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        JSONObject object = null;
        try {
            result =net(url, params, "GET");
            object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println(object.get("result"));
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 实时汇率查询换算
     * @param form 来源币种
     * @param to 目标币种
     */
    public static JSONObject getRequest3(String form, String to){
        String result =null;
        String url ="http://op.juhe.cn/onebox/exchange/currency";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("from",form);//转换汇率前的货币代码
        params.put("to",to);//转换汇率成的货币代码
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        JSONObject object = null;
        try {
            result =net(url, params, "GET");
            object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println(object.get("result"));
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static List<AmazonRateEntity> getRateList(){
        JSONObject object = getRequest1();
        JSONArray list = object.getJSONObject("result").getJSONArray("list");
        List<AmazonRateEntity> amazonRateList = new ArrayList<AmazonRateEntity>();
        //澳大利亚元
        AmazonRateEntity rateEntityAU = new AmazonRateEntity();
        rateEntityAU.setRateId(1L);
        rateEntityAU.setRateCode(ConstantDictionary.RateCode.AU_CODE);
        String rateAUStr = list.getJSONArray(0).getString(2);
        BigDecimal rateAU = new BigDecimal(rateAUStr);
        rateAU = rateAU.divide(new BigDecimal(100),4,1);
        rateEntityAU.setRate(rateAU);
        rateEntityAU.setUpdateTime(new Date());
        amazonRateList.add(rateEntityAU);
        //加拿大元
        AmazonRateEntity rateEntityCA = new AmazonRateEntity();
        rateEntityCA.setRateId(2L);
        rateEntityCA.setRateCode(ConstantDictionary.RateCode.CA_CODE);
        String rateCAStr = list.getJSONArray(4).getString(2);
        BigDecimal rateCA = new BigDecimal(rateCAStr);
        rateCA = rateCA.divide(new BigDecimal(100),4,1);
        rateEntityCA.setRate(rateCA);
        rateEntityCA.setUpdateTime(new Date());
        amazonRateList.add(rateEntityCA);
        //美元
        AmazonRateEntity rateEntityUS = new AmazonRateEntity();
        rateEntityUS.setRateId(3L);
        rateEntityUS.setRateCode(ConstantDictionary.RateCode.US_CODE);
        String rateUSStr = list.getJSONArray(5).getString(2);
        BigDecimal rateUS = new BigDecimal(rateUSStr);
        rateUS = rateUS.divide(new BigDecimal(100),4,1);
        rateEntityUS.setRate(rateUS);
        rateEntityUS.setUpdateTime(new Date());
        amazonRateList.add(rateEntityUS);
        //欧元
        AmazonRateEntity rateEntityEU = new AmazonRateEntity();
        rateEntityEU.setRateId(4L);
        rateEntityEU.setRateCode(ConstantDictionary.RateCode.EU_CODE);
        String rateEUStr = list.getJSONArray(6).getString(2);
        BigDecimal rateEU = new BigDecimal(rateEUStr);
        rateEU = rateEU.divide(new BigDecimal(100),4,1);
        rateEntityEU.setRate(rateEU);
        rateEntityEU.setUpdateTime(new Date());
        amazonRateList.add(rateEntityEU);
        //日元
        AmazonRateEntity rateEntityJP = new AmazonRateEntity();
        rateEntityJP.setRateId(5L);
        rateEntityJP.setRateCode(ConstantDictionary.RateCode.JP_CODE);
        String rateJPStr = list.getJSONArray(7).getString(2);
        BigDecimal rateJP = new BigDecimal(rateJPStr);
        rateJP = rateJP.divide(new BigDecimal(100),4,1);
        rateEntityJP.setRate(rateJP);
        rateEntityJP.setUpdateTime(new Date());
        amazonRateList.add(rateEntityJP);
        //英镑
        AmazonRateEntity rateEntityUK = new AmazonRateEntity();
        rateEntityUK.setRateId(6L);
        rateEntityUK.setRateCode(ConstantDictionary.RateCode.UK_CODE);
        String rateUKStr = list.getJSONArray(8).getString(2);
        BigDecimal rateUK = new BigDecimal(rateUKStr);
        rateUK = rateUK.divide(new BigDecimal(100),4,1);
        rateEntityUK.setRate(rateUK);
        rateEntityUK.setUpdateTime(new Date());
        amazonRateList.add(rateEntityUK);
        //墨西哥单独获取
        JSONObject object1 = getRequest3(ConstantDictionary.RateCode.MXG_CODE,ConstantDictionary.RateCode.ZH_CODE);
        String rateMXGStr = object1.getJSONArray("result").getJSONObject(0).getString("result");
        AmazonRateEntity rateEntityMXG = new AmazonRateEntity();
        rateEntityMXG.setRateId(7L);
        rateEntityMXG.setRateCode(ConstantDictionary.RateCode.MXG_CODE);
        rateEntityMXG.setRate(new BigDecimal(rateMXGStr));
        rateEntityMXG.setUpdateTime(new Date());
        amazonRateList.add(rateEntityMXG);
        return amazonRateList;
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    System.out.println("发生错误：" + e);
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
