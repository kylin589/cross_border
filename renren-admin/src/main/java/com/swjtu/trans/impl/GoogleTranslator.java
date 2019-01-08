package com.swjtu.trans.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swjtu.http.JDBC;
import com.swjtu.lang.LANG;
import com.swjtu.trans.AbstractTranslator;
import com.swjtu.util.Util;
import io.renren.modules.sys.entity.AgentIpEntity;
import io.renren.modules.sys.service.AgentIpService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

@RestController
public final class GoogleTranslator extends AbstractTranslator {

    private static final String url = "https://translate.google.cn/translate_a/single";
    private String ip;
    private int port;
    public GoogleTranslator(){
        super(url);
        Map<String, Object> map = JDBC.getOne();
        ip = map.get("ip").toString();
        port = Integer.parseInt(map.get("port").toString());
    }

    @Override
    public void setLangSupport() {
        langMap.put(LANG.ZH, "zh-CN");
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.JP, "ja");
        langMap.put(LANG.KOR, "ko");
        langMap.put(LANG.FRA, "fr");
        langMap.put(LANG.RU, "ru");
        langMap.put(LANG.DE, "de");
        langMap.put(LANG.IT, "it");
        langMap.put(LANG.SPA, "es");
    }

    @Override
    public void setFormData(LANG from, LANG to, String text) {
        formData.put("client", "t");
        formData.put("sl", langMap.get(from));
        formData.put("tl", langMap.get(to));
        formData.put("hl", "zh-CN");
        formData.put("dt", "at");
        formData.put("dt", "bd");
        formData.put("dt", "ex");
        formData.put("dt", "ld");
        formData.put("dt", "md");
        formData.put("dt", "qca");
        formData.put("dt", "rw");
        formData.put("dt", "rm");
        formData.put("dt", "ss");
        formData.put("dt", "t");
        formData.put("ie", "UTF-8");
        formData.put("oe", "UTF-8");
        formData.put("source", "btn");
        formData.put("ssel", "0");
        formData.put("tsel", "0");
        formData.put("kc", "0");
        formData.put("tk", token(text));
        formData.put("q", text);
    }

    @Override
    public String query() throws Exception {
        URIBuilder uri = new URIBuilder(url);
        for (String key : formData.keySet()) {
            String value = formData.get(key);
            uri.addParameter(key, value);
        }
//        HttpUriRequest request = new HttpGet(uri.toString());
        HttpGet request = new HttpGet(uri.toString());
        //Map<String, Object> map = JDBC.getOne();

        // 初始化线程池
        CloseableHttpClient httpClient = getHttpClient();
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(2000)
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000)
                .build();
        System.out.println("ip:" + ip);
        System.out.println("port:" + port);
        RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setProxy(new HttpHost(ip,port))
                .build();
        request.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity, "utf-8");

        EntityUtils.consume(entity);
        response.getEntity().getContent().close();
        response.close();
        return result;
    }
    @Override
    public String parses(String text) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuffer strbuf = new StringBuffer();
        for(int i=0;i<mapper.readTree(text).get(0).size();i++){
            strbuf.append(mapper.readTree(text).get(0).get(i).get(0).toString());
        }
        return strbuf.toString().replace("\"", "");
    }

    private String token(String text) {
        String tk = "";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
//            FileReader reader = new FileReader("D://tk/Google.js");
            FileReader reader = new FileReader("/usr/tk/Google.js");
            engine.eval(reader);

            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable)engine;
                tk = String.valueOf(invoke.invokeFunction("token", text));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("token:" + text);
        return tk;
    }
}
