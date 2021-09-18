package com.ultikits.ultitools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    private Date date = Calendar.getInstance(Locale.getDefault()).getTime();
    private SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");

    private String dateString;
    private long dateChuo;

    public long getTimeStamp() {                               //获取时间戳
        dateString = dateFormat1.format(date);
        try {
            dateChuo = dateFormat1.parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateChuo;
    }

    public String getTimeWithDate() {
        dateString = dateFormat1.format(date);
        return dateString;
    }

    public String getTimeWithHm() {                         //获取时间 格式 HH:mm
        dateString = dateFormat2.format(date);
        return dateString;
    }

}
