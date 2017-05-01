package com.potato.util;



import com.potato.demo.*;
import com.potato.tags.Constant;
import com.potato.tags.SinglePdf;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by AllstarVirgo on 2017/4/25.
 */
public class PdfDownloader implements Runnable{
    private BlockingQueue<String>queue;

    private Map<String,SinglePdf>pdfMap;



    public PdfDownloader(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public PdfDownloader(BlockingQueue<String> queue, Map<String, SinglePdf> pdfMap) {
        this.queue = queue;
        this.pdfMap = pdfMap;
    }

    @Override
    public void run() {
        boolean done=false;
        while (!done){
            try {
                String url = queue.take();
                if(url== Constant.flag){
                    queue.put(url);
                }else download(url);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void download(String iniURL) {
        try {
            String url=URLHandler(iniURL);
            URL a_url=new URL(url);
            /*通过pdf得到其他相关信息*/
            SinglePdf pdfInfo=pdfMap.get(iniURL);
            String name=pdfInfo.toString();
            HttpURLConnection httpURLConnection = (HttpURLConnection) a_url.openConnection();
            httpURLConnection.setConnectTimeout(5 * 1000); // 5000 毫秒内没有连接上 则放弃连接
            httpURLConnection.connect(); // 连接
            System.out.println("连接 URL 成功~");

            int fileLenght = httpURLConnection.getContentLength();
            System.out.println("文件大小：" + (fileLenght / 1024.0) + " KB");
            System.out.println("开始下载...");
            try (DataInputStream dis = new DataInputStream(httpURLConnection.getInputStream());
                 FileOutputStream fos = new FileOutputStream("data\\hsqitarongzi\\"+name+".pdf")) {
                byte[] buf = new byte[10240]; // 根据实际情况可以 增大 buf 大小
                for (int readSize; (readSize = dis.read(buf)) > 0;) {
                    fos.write(buf, 0, readSize);
                }
                System.out.println("下载完毕~");
            } catch (IOException ex) {
                System.out.println("下载时出错");
            }

            httpURLConnection.disconnect();
        } catch (IOException ex) {
            System.out.println("URL 不存在或者连接超时");
        }
    }

    private String URLHandler(String url){
        String[] array=url.split("/");
        return Constant.absURL+array[2]+"?announceTime="+array[1];
    }

    public static void main(String[] args) {
        final int URL_QUEUE_SIZE=10;
        final int DOWNLOAD_THREADS=100;

        BlockingQueue<String>queue=new ArrayBlockingQueue<String>(URL_QUEUE_SIZE);

        Map<String,SinglePdf>mapPdf=new ConcurrentHashMap<>();

        UrlFactory urlFactory=new UrlFactory(queue,mapPdf);

        new Thread(urlFactory).start();
        for (int i=1;i<=DOWNLOAD_THREADS;i++){
            new Thread(new PdfDownloader(queue,mapPdf)).start();
        }
    }
}
