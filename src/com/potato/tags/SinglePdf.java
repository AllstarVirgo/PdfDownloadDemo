package com.potato.tags;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AllstarVirgo on 2017/4/24.
 */
public class SinglePdf {
    private String secCode;

    private String secName;

    private String announcementTitle;

    private String announcementTime;

    private String adjunctUrl;

    private String tag=Constant.PEIGU;

    private Date date;

    private String formatDate;

    public SinglePdf(String secCode, String secName, String announcementTitle,String announcementTime, String adjunctUrl) {
        this.secCode = secCode;
        this.secName = secName;
        this.announcementTitle = announcementTitle;
        this.announcementTime=announcementTime;
        this.adjunctUrl = adjunctUrl;
        date = new Date(Long.parseLong(announcementTime));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        formatDate = simpleDateFormat.format(date);
    }

    @Override
    public String toString() {
        return secCode+"-"+tag+"-"+"-"+formatDate+"-"+announcementTitle;
    }

    public String getAdjunctUrl() {
        return adjunctUrl;
    }
}
