package io.renren.modules.product.component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.sys.entity.AgentIpEntity;
import io.renren.modules.sys.service.AgentIpService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: wdh
 * @Date: 2018/12/23 23:35
 * @Description:
 */
@Component("agentTimer")
public class agentTimer{
    private static String url = "https://www.xicidaili.com/nn/1/"; //API链接

    public void rateUpdate() {
        //覆盖到ip.properties文件
        try {
            List<String[]> strings = kuaidailiCom(url);
            FileWriter writer = new FileWriter("ip.properties");
            FileReader reader = new FileReader("ip.properties");
            Properties p = new Properties();
            p.load(reader);
            if(strings.size() > 0 ){
                for(String[] strs : strings){
                    p.setProperty(strs[0], strs[1]);
                }
                p.store(writer, "更新信息");
            }
            reader.close();
            writer.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<String[]> kuaidailiCom(String baseUrl) {
        String ipReg = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} \\d{1,6}";
        Pattern ipPtn = Pattern.compile(ipReg);
        List<String[]> strings = new ArrayList<String[]>();
        try {
            Document doc = Jsoup.connect(baseUrl)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate, sdch")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                    .header("Cache-Control", "max-age=0")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36")
                    .header("Host", "www.kuaidaili.com")
                    .header("Referer", "http://www.kuaidaili.com/free/outha/")
                    .timeout(30 * 1000)
                    .get();
            Matcher m = ipPtn.matcher(doc.text());
            while (m.find()) {
                String[] strs = m.group().split(" ");
                if (checkProxy(strs[0], Integer.parseInt(strs[1]))) {
                    strings.add(strs);
                    System.out.println(strings.size());
                }
                if(strings.size()>20){
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strings;
    }

    private static boolean checkProxy(String ip, Integer port) {
        try {
            //http://1212.ip138.com/ic.asp 可以换成任何比较快的网页
            Jsoup.connect("https://translate.google.cn/")
                    .timeout(2 * 1000)
                    .proxy(ip, port)
                    .get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
/*    private String getBeforeTime(){
        Date now = new Date();
        Date now_10 = new Date(now.getTime() - 600000); //10分钟前的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        String nowTime_10 = dateFormat.format(now_10);
        return nowTime_10;
    }*/

}
