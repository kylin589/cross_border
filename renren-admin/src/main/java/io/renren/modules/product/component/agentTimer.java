package io.renren.modules.product.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.sys.entity.AgentIpEntity;
import io.renren.modules.sys.service.AgentIpService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: wdh
 * @Date: 2018/12/23 23:35
 * @Description:
 */
@Component("agentTimer")
public class agentTimer{

    @Autowired
    private AgentIpService agentIpService;
//    private static String url = "https://www.xicidaili.com/nn/1/"; //API链接
    private static String url = "https://h.wandouip.com/get/ip-list?pack=1192&num=20&xy=1&type=2&lb=&mr=1&"; //API链接
    public void getIp(){
        List<AgentIpEntity> agentIpEntityList = wanDouIp(url);
        if(agentIpEntityList != null && agentIpEntityList.size()>0){
            agentIpService.insertBatch(agentIpEntityList);
            agentIpService.delete(new EntityWrapper<AgentIpEntity>().lt("update_time",getBeforeTime()));
        }
    }

    private static List<AgentIpEntity> wanDouIp(String baseUrl) {
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();
        HttpGet httpGet = new HttpGet(baseUrl);
        httpGet.setHeader("Accept","application/json, text/plain, */*");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        httpGet.setHeader("Content-Type", "application / json;charset = UTF-8");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Host", "h.wandouip.com");
        httpGet.setConfig(requestConfig);
        String srtResult = "";
        try {
            HttpResponse httpResponse = httpCilent.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                srtResult = EntityUtils.toString(httpResponse.getEntity());
                System.out.println("返回结果：" + srtResult);
                JSONObject result = JSONObject.parseObject(srtResult);
                JSONArray data = result.getJSONArray("data");
                if(data.size()>0) {
                    List<AgentIpEntity> agentIpEntityList = new ArrayList<AgentIpEntity>();
                    for (int i = 0; i < data.size(); i++) {
                        // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        JSONObject job = data.getJSONObject(i);
                        if(checkProxy(job.getString("ip"),job.getIntValue("port"))){
                            System.out.println("第" + i + "个");
                            AgentIpEntity agentIpEntity = new AgentIpEntity();
                            agentIpEntity.setIp(job.getString("ip"));
                            agentIpEntity.setPort(job.getIntValue("port"));
                            agentIpEntity.setUpdateTime(new Date());
                            agentIpEntityList.add(agentIpEntity);
                        }
                    }
                    return agentIpEntityList;
                }
            }else if(httpResponse.getStatusLine().getStatusCode() == 400){
                //..........
            }else if(httpResponse.getStatusLine().getStatusCode() == 500){
                //.............
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpCilent.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static boolean checkProxy(String ip, Integer port) {
        try {
            //http://1212.ip138.com/ic.asp 可以换成任何比较快的网页
            Jsoup.connect("http://myip.ipip.net  ")
                    .timeout(3 * 1000)
                    .proxy(ip, port)
                    .get();
            System.out.println("成功");
            return true;
        } catch (Exception e) {
            System.out.println("失败");
            return false;
        }
    }
    private String getBeforeTime(){
        Date now = new Date();
        Date now_10 = new Date(now.getTime() - 300000); //5分钟前的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        String nowTime_10 = dateFormat.format(now_10);
        return nowTime_10;
    }
    public static String replaceAccessTokenReg(String url, String name, String accessToken) {
        if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(accessToken)) {
            url = url.replaceAll("(" + name + "=[^&]*)", name + "=" + accessToken);
        }
        return url;
    }
//    public void rateUpdate() {
//        //覆盖到ip.properties文件
//        try {
//            List<String[]> strings = kuaidailiCom(url);
//            FileWriter writer = new FileWriter("ip.properties");
//            FileReader reader = new FileReader("ip.properties");
//            Properties p = new Properties();
//            p.load(reader);
//            if(strings.size() > 0 ){
//                for(String[] strs : strings){
//                    p.setProperty(strs[0], strs[1]);
//                }
//                p.store(writer, "更新信息");
//            }
//            reader.close();
//            writer.close();
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    private List<String[]> kuaidailiCom(String baseUrl) {
//        String ipReg = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} \\d{1,6}";
//        Pattern ipPtn = Pattern.compile(ipReg);
//        List<String[]> strings = new ArrayList<String[]>();
//        try {
//            Document doc = Jsoup.connect(baseUrl)
//                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                    .header("Accept-Encoding", "gzip, deflate, sdch")
//                    .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
//                    .header("Cache-Control", "max-age=0")
//                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36")
//                    .header("Host", "www.kuaidaili.com")
//                    .header("Referer", "http://www.kuaidaili.com/free/outha/")
//                    .timeout(30 * 1000)
//                    .get();
//            Matcher m = ipPtn.matcher(doc.text());
//            while (m.find()) {
//                String[] strs = m.group().split(" ");
//                if (checkProxy(strs[0], Integer.parseInt(strs[1]))) {
//                    strings.add(strs);
//                    System.out.println(strings.size());
//                }
//                if(strings.size()>20){
//                    break;
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return strings;
//    }

}
