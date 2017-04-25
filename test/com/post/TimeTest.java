package com.post;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AllstarVirgo on 2017/4/24.
 */
public class TimeTest {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(sdf.format(Long.parseLong("1492358400000")));

        Date date=new Date(Long.parseLong("1492358400000"));

        System.out.println(sdf.format(date));
    }
}
