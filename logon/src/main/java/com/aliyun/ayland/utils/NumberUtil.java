package com.aliyun.ayland.utils;

import java.text.DecimalFormat;

/**
 * Created by root on 2016/7/26.
 */
public class NumberUtil {
    public static String formatFloat(float value, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(value);
    }

    public static String formatLong(long value, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(value);
    }

    public static String formatInt(int value, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(value);
    }

    public static String formatFloatToMoney(float value) {
        return formatFloat(value, "#.##");
    }

    public static String formatFloatToFullMoney(float value) {
        return formatFloat(value, "0.00");
    }
}
