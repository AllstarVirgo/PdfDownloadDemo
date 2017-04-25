package com.post;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AllstarVirgo on 2017/4/17.
 */
public class HttpclientPost {
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();
    /**
     * post方式提交表单（模拟用户登录请求）
     */
    public static void postForm() {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://www.cninfo.com.cn/cninfo-new/announcement/query");
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<>();
        formparams.add(new BasicNameValuePair("column", "szse"));
        formparams.add(new BasicNameValuePair("columnTitle","%E5%8E%86%E5%8F%B2%E5%85%AC%E5%91%8A%E6%9F%A5%E8%AF%A2"));
        formparams.add(new BasicNameValuePair("pageNum","1"));
        formparams.add(new BasicNameValuePair("pageSize","30"));
        formparams.add(new BasicNameValuePair("tabName","fulltext"));
        formparams.add(new BasicNameValuePair("seDate","%E8%AF%B7%E9%80%89%E6%8B%A9%E6%97%A5%E6%9C%9F"));


        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            //设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    private static String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public static void main(String[] args) {

        String result=sendHttpPost("http://www.cninfo.com.cn/cninfo-new/announcement/query","stock=&searchkey=&plate=&category=category_pg_szsh%3B&trade=&column=szse&columnTitle=%E5%8E%86%E5%8F%B2%E5%85%AC%E5%91%8A%E6%9F%A5%E8%AF%A2&pageNum=1&pageSize=30&tabName=fulltext&sortName=&sortType=&limit=&showTitle=category_pg_szsh%2Fcategory%2F%E9%85%8D%E8%82%A1&seDate=%E8%AF%B7%E9%80%89%E6%8B%A9%E6%97%A5%E6%9C%9F");
        System.out.println(result);
    }

}
