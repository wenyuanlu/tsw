package com.maishuo.tingshuohenhaowan.utils;


import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * EXPLAIN: 获取当前的时间秒数
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

    public static String getTime (long time) {
        return format1.format(time);
    }

    static SimpleDateFormat format1 = new SimpleDateFormat("m:ss");

    private final static long minute = 60;// 1分钟
    private final static long hour   = 60 * minute;// 1小时
    private final static long day    = 24 * hour;// 1天
    private final static long month  = 31 * day;// 月
    private final static long year   = 12 * month;// 年

    private final static long minute2 = 60 * 1000;// 1分钟
    private final static long hour2   = 60 * minute2;// 1小时
    private final static long day2    = 24 * hour2;// 1天
    private final static long month2  = 31 * day2;// 月
    private final static long year2   = 12 * month2;// 年

    // 备注:如果使用大写HH标识使用24小时显示格式,如果使用小写hh就表示使用12小时制格式。
    public static String DATE_TO_STRING_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    private static SimpleDateFormat simpleDateFormat;

    private static SimpleDateFormat getSimpleDateFormat (String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat;
    }

    //时间格式化
    public static String dateFormat(Date date,String patten){
        if(null == date || null == patten){
            return null;
        }

        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(patten);
        return simpleDateFormat.format(date);
    }

    //返回文字描述的日期
    public static String getTimeFormatText (long time) {
        long r = 0;
        if (time > year) {
            r = (time / year);
            return r + "年前";
        }
        if (time > month) {
            r = (time / month);
            return r + "个月前";
        }
        if (time > day) {
            r = (time / day);
            return r + "天前";
        }
        if (time > hour) {
            r = (time / hour);
            return r + "小时前";
        }
        if (time > minute) {
            r = (time / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    //返回文字描述的日期
    public static String getTimeFormatText2 (long playTime) {
        long nowTime = new Date().getTime();
        long diff    = nowTime - playTime;
        long r       = 0;
        if (diff > year2) {
            r = (diff / year2);
            return r + "年前";
        }
        if (diff > month2) {
            r = (diff / month2);
            return r + "个月前";
        }
        if (diff > day2) {
            r = (diff / day2);
            return r + "天前";
        }
        if (diff > hour2) {
            r = (diff / hour2);
            return r + "小时前";
        }
        if (diff > minute2) {
            r = (diff / minute2);
            return r + "分钟前";
        }
        return "刚刚";
    }

    //将时间戳转换成字符串
    public static String longToString (Long data, String pattern) {
        Date             date             = new Date(data);
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    //根据秒数转化为时分秒   00:00:00
    public static String getHSTime (int second) {
        if (second < 10) {
            return "00:0" + second;
        }
        if (second < 60) {
            return "00:" + second;
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }
            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }
        int hour   = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + minute + ":0" + second;
            }
            return "0" + hour + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour + minute + ":0" + second;
        }
        return hour + minute + ":" + second;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws android.net.ParseException
     */
    public static boolean IsToday(long day) {

        Calendar pre     = Calendar.getInstance();
        Date     predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new Date(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws android.net.ParseException
     */
    public static boolean IsYesterday(long day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new Date(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }


    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<>();

}
