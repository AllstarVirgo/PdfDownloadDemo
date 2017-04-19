package com.potato.tags;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by AllstarVirgo on 2017/4/19.
 */
public class PostParams {
    private String stock;
    private String searchkey;
    private String plate;
    private String category;
    private String trade;
    private String column;
    private String columnTitle;
    private String pageNum;
    private String pageSize;
    private String tabName;
    private String sortName;
    private String sortType;
    private String limit;
    private String showTitle;
    private String seDate;

    @Override
    public String toString() {
        StringBuffer postParams = new StringBuffer();
        postParams.append("stock=" + decode(stock) + "&");
        postParams.append("searchkey=" + decode(searchkey) + "&");
        postParams.append("plate=" + decode(plate) + "&");
        postParams.append("category=" + decode(category) + "&");
        postParams.append("trade=" + decode(trade) + "&");
        postParams.append("column=" + decode(column) + "&");
        postParams.append("columnTitle=" + decode(columnTitle) + "&");
        postParams.append("pageNum=" + decode(pageNum) + "&");
        postParams.append("pageSize=" + decode(pageSize) + "&");
        postParams.append("tabName=" + decode(tabName) + "&");
        postParams.append("sortName=" + decode(sortName) + "&");
        postParams.append("sortType=" + decode(sortType) + "&");
        postParams.append("limit=" + decode(limit) + "&");
        postParams.append("showTitle=" + decode(showTitle) + "&");
        postParams.append("seDate=" + decode(seDate) );
        return postParams.toString();
    }

    private String decode(String str) {
        String result = "";
        if (str != null) {
            try {
                result = URLEncoder.encode(str, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
