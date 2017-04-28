package com.potato.util;

import com.potato.tags.Constant;
import com.potato.tags.PostParams;
import com.potato.tags.SinglePdf;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by AllstarVirgo on 2017/4/25.
 */
public class UrlFactory implements Runnable {
    private BlockingQueue<String> queue;

    private Map<String,SinglePdf>pdfMap;

    private CloseableHttpClient closeableHttpClient;

    private HttpPost httpPost;

    private boolean hasNextPage;

    private PostParams postParams;


    public UrlFactory(BlockingQueue<String> queue,Map<String,SinglePdf>pdfMap) {
        closeableHttpClient = HttpClients.createDefault();
        httpPost = new HttpPost(Constant.requestURL);
        httpPost.setConfig(Constant.requestConfig);
        hasNextPage=true;
        this.pdfMap=pdfMap;
        this.queue = queue;
        postParams=new PostParams(Constant._ZENGFA,"szse",
                "历史公告查询","1","30","fulltext","请选择日期");
    }

    /**
     * 初始化post相关参数
     *
     * @param params 待发送表单
     * @return
     */
    public HttpPost initializePost(String params) {
        StringEntity stringEntity = new StringEntity(params, Constant.ENCODE);
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);
        return httpPost;
    }

    @Override
    public void run() {
        try {
            addUrl(postParams);
            queue.put(Constant.flag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addUrl(PostParams postParams) throws InterruptedException {
        int i=1;
        while (hasNextPage){
            postParams.setPageNum(new Integer(i).toString());

            String post=postParams.toString();

            initializePost(post);

            getSinglePageUrl();

            i++;
        }
    }



    private void getSinglePageUrl() throws InterruptedException {
        try {
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String jasonObj = EntityUtils.toString(entity, Constant.ENCODE);
            /*{"classifiedAnnouncements":null,"totalSecurities":0,"totalAnnouncement":2735,"totalRecordNum":2735,"announcements":[...]*
            ,"categoryList":null,"hasMore":true,"totalpages":0}*/
            /*将jason字符串转化为jason对象*/
            JSONObject jsonObject = JSONObject.fromObject(jasonObj);
            /*得到jason字符串格式的数组*/
            String contentJason=jsonObject.getString("announcements");
            /*是否有更多*/
            String hasMore=jsonObject.getString("hasMore");
            if(hasMore.equals("true"))hasNextPage=true;
            else hasNextPage=false;
            /*转换为jason数组*/
            JSONArray jsonArray=JSONArray.fromObject(contentJason);
            /*每个索引对应一个Jason文本，需要生成jason对象,获取其中数据*/
            for(int i=0;i<jsonArray.size();i++){
                String announcement=jsonArray.getString(i);
                SinglePdf singlePdf=getPdfInfo(announcement);
                queue.put(singlePdf.getAdjunctUrl());
                pdfMap.put(singlePdf.getAdjunctUrl(),singlePdf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SinglePdf getPdfInfo(String jasonAnnouncement){
        JSONObject jsonObject=JSONObject.fromObject(jasonAnnouncement);
        return new SinglePdf(jsonObject.getString("secCode"),jsonObject.getString("secName"),jsonObject.getString("announcementTitle")
        ,jsonObject.getString("announcementTime"),jsonObject.getString("adjunctUrl"));
    }


}
