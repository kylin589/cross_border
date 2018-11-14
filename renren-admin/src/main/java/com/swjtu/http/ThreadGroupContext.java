package com.swjtu.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
/**
 * @Auther: wdh
 * @Date: 2018/11/6 11:33
 * @Description:
 */
public class ThreadGroupContext extends Thread {
    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private final HttpGet httpget;

    public ThreadGroupContext(CloseableHttpClient httpClient, HttpGet httpget) {
        this.httpClient = httpClient;
        this.context = HttpClientContext.create();
        this.httpget = httpget;
    }

    @Override
    public void run() {
        System.out.println("执行开始、、、、、、、、、、、");
        try {
            CloseableHttpResponse response = httpClient.execute(httpget,context);
            try {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "utf-8");

                EntityUtils.consume(entity);
                System.out.println("结果为：" + result);
            } finally {
                response.getEntity().getContent().close();
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
