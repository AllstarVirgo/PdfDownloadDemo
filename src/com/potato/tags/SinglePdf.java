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

    private String adjunctUrl;

    private String tag=Constant.PEIGU;

    private Date date;

    private String formatDate;

    public SinglePdf(String secCode, String secName, String announcementTitle, String adjunctUrl) {
        this.secCode = secCode;
        this.secName = secName;
        this.announcementTitle = announcementTitle;
        this.adjunctUrl = adjunctUrl;
        date = new Date(Long.parseLong(announcementTitle));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        formatDate = simpleDateFormat.format(date);
    }

    public String getAdjunctUrl() {
        return adjunctUrl;
    }
}
