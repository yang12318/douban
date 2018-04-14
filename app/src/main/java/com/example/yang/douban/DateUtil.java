package com.example.yang.douban;
import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ouyangshen on 2016/9/24.
 */
public class DateUtil {
    @SuppressLint("SimpleDateFormat")
    public static String getNowDateTime(String formatStr) {
        String format = formatStr;
        if (format==null || format.length()<=0) {
            format = "yyyyMMddHHmmss";
        }
        SimpleDateFormat s_format = new SimpleDateFormat(format);
        return s_format.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss");
        return s_format.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNowTimeDetail() {
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss.SSS");
        return s_format.format(new Date());
    }

    public static long getDeltaDate(String str) {
        SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMddHHmmss");
        String str2 = getNowDateTime("yyyyMMddHHmmss");
        Date old_date = null, new_date = null;
        try {
            old_date = s_format.parse(str);
            new_date = s_format.parse(str2);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long old_time = old_date.getTime();
        long new_time = new_date.getTime();
        long diff_time = new_time - old_time;
        long diff_days = diff_time / 24 / 60 / 60 / 1000;
        //long diff_time = day_num * 24 * 60 * 60 * 1000;
//		LogUtil.debug(TAG, "day_num="+day_num+", diff_time="+diff_time);
        /*time += diff_time;
        Date new_date = new Date(time);
        String s_date = s_format.format(new_date);
        return s_date;*/
        return diff_days;
    }

}
