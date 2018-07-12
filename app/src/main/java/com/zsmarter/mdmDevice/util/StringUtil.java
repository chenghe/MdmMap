package com.zsmarter.mdmDevice.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.SimpleFormatter;

/**
 * Created by hecheng on 2018/7/2
 */
public class StringUtil {

    public static final String AM = "AM";
    public static final String NOON = "NOON";
    public static final String PM = "PM";


    public static String[] addressFenceSplit(String addressFence) {
        if (TextUtils.isEmpty(addressFence)) {
            return null;
        } else {
            String[] a = addressFence.split(",");
            return a;
        }
    }

    public static String refectTime(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nows = null;
        try {
            nows = (Date) sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        nows.getHours();
        Log.i("hcb","nows.getHours()====="+nows.getHours());
        String time = "";
        if (nows!=null){
            time =  getDuringDay(nows.getHours());
        }
        return time;
    }

    public static String getDuringDay(int hour) {
        if (hour >= 0 && hour < 11) {
            return AM;
        }
        if (hour >= 11 && hour <= 14) {
            return NOON;
        }
        if (hour >= 15 && hour <= 24) {
            return PM;
        }
        return null;
    }
    }
