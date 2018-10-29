package com.maybe.sys.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtil {
    /**
     * get请求（用于请求json格式的参数）
     * @param url 接口地址
     * @return
     */
    public static String doGet(String url) {
        HttpGet httpGet = new HttpGet(url);// 创建httpGet
        return handelRequest(httpGet);
    }
    /**
     * post请求（用于请求json格式的参数）
     * @param url 接口地址
     * @param params 接口参数
     * @return
     */
    public static String doPost(String url, String params) {
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        if(params != null && !"".equals(params)){
            StringEntity entity = new StringEntity(params, "UTF-8");
            httpPost.setEntity(entity);
        }
        return handelRequest(httpPost);
    }

    private static String handelRequest(HttpUriRequest http){
        CloseableHttpClient client = HttpClients.createDefault();
        http.setHeader("Accept", "application/json");    //接收报文类型
        http.setHeader("Content-Type", "application/json");   //发送报文类型
        CloseableHttpResponse response = null;
        try {
            response = client.execute(http);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity,"UTF-8");
                return jsonString;
            }
            else{
                System.out.println(state);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) response.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
