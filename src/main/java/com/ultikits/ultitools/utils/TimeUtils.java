package com.ultikits.ultitools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Shpries
 */
public class TimeUtils {
    private final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     *获取时间戳
     *
     * @return 当前时间戳
     */
    public long getTimeStamp() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String dateString = dateFormat1.format(calendar.getTime());
        long timeStamp = 0;
        try {
            timeStamp = dateFormat1.parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    /**
     * 以格式"yyyy-MM-dd HH:mm"获取时间
     *
     * @return  "yyyy-MM-dd HH:mm"格式的时间
     */
    public String getTimeWithDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String dateString = dateFormat1.format(calendar.getTime());
        return dateString;
    }

    /**
     * 同时获取当前日期时间和设定天数后的日期时间
     *
     * @param days 需要在当前日期后加的天数
     * @return 一个含有两个元素的数组，分别是"yyyy-MM-dd HH:mm"格式的当前日期和days日后的同格式日期
     */
    public String[] getTimeAndAdd(int days) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String[] time = new String[2];
        String dateString = dateFormat1.format(calendar.getTime());
        calendar.add(Calendar.DATE,days);
        String dateStringAfterAdd = dateFormat1.format(calendar.getTime());
        time[0] = dateString;
        time[1] = dateStringAfterAdd;
        return time;
    }

    /**
     * 判断time1是否在time2之后
     *
     * @param time1 第一个时间，必须是"yyyy-MM-dd HH:mm"格式
     * @param time2 第二个时间，必须是"yyyy-MM-dd HH:mm"格式
     * @return 如果time1是否在time2之后，则返回true，反之false
     */
    public boolean isTimeAfter(String time1,String time2) {
        Date date1 = null,date2 = null;
        try {
            date1 = dateFormat1.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            date2 = dateFormat1.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date1.after(date2)) {
            return true;
        }
        return false;
    }
}
