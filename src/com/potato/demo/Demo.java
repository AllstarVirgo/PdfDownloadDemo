package com.potato.demo;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by AllstarVirgo on 2017/3/25.
 */
public class Demo {
    public static void main(String[] args) {
        final int URL_QUEUE_SIZE=10;

        final int DOWNLOAD_THREADS=100;

        BlockingQueue<String> queue=new ArrayBlockingQueue<String>(URL_QUEUE_SIZE);

        UrlFactory urlFactory=new UrlFactory(queue);

        new Thread(urlFactory).start();

        for(int i=0;i<20;i++){
            new Thread(new PdfDownload(queue)).start();
        }
    }
}

/**
 * This factory will product urls
 */

class UrlFactory implements Runnable{
    public static String flag="flag";

    private BlockingQueue<String>queue;

    public UrlFactory(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            addUrl();
            queue.put(flag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * http://www.cninfo.com.cn/cninfo-new/disclosure/szse/download/1203194872?announceTime=2017-03-25
     * @throws InterruptedException
     */

    public void addUrl() throws InterruptedException {
        int startIndex=208;
        /*
        first对象指向http:www.cinfi...这个初始网站，startIndex的值虽然发生了变化，但first的指向并没有发生变化，因此不会改变。
         */
//        String first="http://www.cninfo.com.cn/cninfo-new/disclosure/szse/download/1203197"+startIndex+"?announceTime=2017-03-25";

        for(int i=0;i<20;i++){
            String first="http://www.cninfo.com.cn/cninfo-new/disclosure/szse/download/1203197"+startIndex+"?announceTime=2017-03-25";
            queue.put(first);
            startIndex++;
        }
    }
}

class PdfDownload implements Runnable{
    private BlockingQueue<String>queue;

    private final static String ENCODE="utf-8";

    public PdfDownload(BlockingQueue<String> queue) {
        this.queue=queue;
    }

    @Override
    public void run() {
        boolean done=false;
        while (!done){
            try {
                String url = queue.take();
                if(url==UrlFactory.flag){
                    queue.put(url);
                }else download(url);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    private void download(String url) {
        try {
            URL a_url=new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) a_url.openConnection();
            httpURLConnection.setConnectTimeout(5 * 1000); // 5000 毫秒内没有连接上 则放弃连接
            httpURLConnection.connect(); // 连接
            System.out.println("连接 URL 成功~");

            int fileLenght = httpURLConnection.getContentLength();
            System.out.println("文件大小：" + (fileLenght / 1024.0) + " KB");

            String str=httpURLConnection.getHeaderField("Content-disposition");

            String name=strProduct();

            if (str.contains("filename=")){
                int index=str.indexOf("\"");

                int endIndex=str.lastIndexOf(".");

//                String filenameWithEnd=str.substring(index+1,str.length()-1);

                String nameBytes=str.substring(index+1,endIndex);

                name=getURLDecoderString(nameBytes);
            }
            System.out.println("开始下载...");
            try (DataInputStream dis = new DataInputStream(httpURLConnection.getInputStream());
                 FileOutputStream fos = new FileOutputStream("data\\"+name+".pdf")) {
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

    public static String strProduct(){
        String str="abcdefghijklmnopqrstuvwxyz1234567890";
        char[] randomChars=new char[4];
        for(int i=0;i<4;i++){
            randomChars[i]=str.charAt((int)(Math.random()*30));
        }
        String name=new String(randomChars);
        return name;
    }


}
