package com.potato.tags;

import org.apache.http.client.config.RequestConfig;

/**
 * Created by AllstarVirgo on 2017/4/24.
 */
public class Constant {
    public static final String PEIGU = "配股";

    public static final String ZENGFA = "增发";

    public static final String QITA = "其他融资";

    public final static String ENCODE = "utf-8";

    public static String flag = "flag";

    public static final String requestURL = "http://www.cninfo.com.cn/cninfo-new/announcement/query";

    public static final RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();

}
