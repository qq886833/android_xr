package com.bsoft.baselib.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/31.
 */
public class NumUtil {


    public static String formatTwo(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try {
            String p = decimalFormat.format(price);
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.00";
    }

    public static String formatTwo(float price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try {
            String p = decimalFormat.format(price);
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.00";
    }

    /**
     * 最多保留2位
     *
     * @param value
     * @return
     */
    public static String numberFormat(double value) {
        return numberFormat(value, 2, false);
    }

    /**
     * 最多保留小数位数
     *
     * @param value
     * @param num
     * @return
     */
    public static String numberFormat(double value, int num) {
        return numberFormat(value, num, false);
    }

    /**
     * 最多保留小数,百分数位数
     *
     * @param value
     * @param num       小数位数
     * @param isPercent 是否是百分数
     * @return
     */
    public static String numberFormat(double value, int num, boolean isPercent) {
        if (isPercent) {
            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确度
            nt.setMaximumFractionDigits(num);
            return nt.format(value);
        } else {
            NumberFormat nf = NumberFormat.getNumberInstance();
            //double精度
            nf.setMaximumFractionDigits(num);
            return nf.format(value);
        }
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 大于9，返回9+
     *
     * @param num
     * @return
     */
    public static String getNum9(int num) {
        if (num > 9) {
            return "9+";
        } else {
            return num + "";
        }
    }

    /**
     * 大于99，返回99+
     *
     * @param num
     * @return
     */
    public static String getNum99(int num) {
        if (num > 99) {
            return "99+";
        } else {
            return num + "";
        }
    }

    /**
     * 大于999，返回999+
     *
     * @param num
     * @return
     */
    public static String getNum999(int num) {
        if (num > 999) {
            return "999+";
        } else {
            return num + "";
        }
    }
}
