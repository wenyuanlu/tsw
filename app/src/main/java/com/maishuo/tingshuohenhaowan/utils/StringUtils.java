package com.maishuo.tingshuohenhaowan.utils;

import android.text.TextUtils;

import com.qichuang.commonlibs.utils.LoggerUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * author ：Seven
 * date : 3/16/21
 * description :字符串转换工具类
 */
public class StringUtils {
    /**
     * 字符串 千位符
     *
     * @param num
     * @return
     */
    public static String num2thousand(int num) {
        return num2thousand(String.valueOf(num));
    }

    public static String num2thousand(String num) {
        String numStr = "";
        if (isEmpty(num)) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            numStr = df.format(nf.parse(num));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }

    /**
     * 字符串 千位符  保留两位小数点后两位
     *
     * @param num
     * @return
     */
    public static String num2thousand00(String num) {
        String numStr = "";
        if (isEmpty(num)) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            numStr = df.format(nf.parse(num));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    //判断是否是数字
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]||-?[0-9]+.*[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static Integer valueDoubleToInteger(Object value) {
        try {
            if (null == value) {
                return null;
            }

            String stringValue = String.valueOf(value);

            if (TextUtils.isEmpty(stringValue) || "null".equals(stringValue)) {
                return null;
            }

            Double doubleValue = Double.valueOf(stringValue);
            return doubleValue.intValue();
        } catch (Exception e) {
            LoggerUtils.INSTANCE.e(e.toString());
            return null;
        }
    }
}
