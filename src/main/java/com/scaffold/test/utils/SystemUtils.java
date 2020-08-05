package com.scaffold.test.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统工具类
 *
 * @author alex
 */

public class SystemUtils {

    /**
     * 是否是mac
     *
     * @return true
     */
    public static boolean isMac() {
        return System.getProperty("os.name").contains("Mac");
    }

    /**
     * 是否是window
     *
     * @return true
     */
    public static boolean isWindow() {
        return System.getProperty("os.name").contains("Window");
    }

    /**
     * 获取距离今天多少天的日期
     *
     * @param days       相差天数，可以为负值
     * @param formatType 日期格式比如：yyyy-MM-dd
     * @return date
     */
    public static String getDateFromToday(int days, String formatType) {
        if (formatType.equals(null) || formatType.length() == 0) {
            formatType = "yyyy-MM-dd";
        }
        Date date = new Date();
        date.setDate(date.getDate() + days);
        return formatDate(date, formatType);
    }

    public static String getDateFromToday(int days) {
        String formatType = "yyyy-MM-dd";
        Date date = new Date();
        date.setDate(date.getDate() + days);
        return formatDate(date, formatType);
    }

    /**
     * 格式化日期
     *
     * @param date       日期对象
     * @param formatType 日期格式比如：yyyy-MM-dd
     * @return string
     */
    public static String formatDate(Date date, String formatType) {
        return new SimpleDateFormat(formatType).format(date);
    }

    /**
     * 获取星期几
     *
     * @param dateStr 日期
     * @return week
     */
    public static String getWeek(String dateStr) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        try {
            Date date = fmt.parse(dateStr);
            int day = date.getDay();
            return weeks[day];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前时刻
     * @return hour
     */
    public static int getCurrentHour(){
        Date date = new Date();
        return date.getHours();
    }


    public static void main(String[] args) {
        System.out.println(getDateFromToday(60));
        System.out.println(getWeek("2020-08-01"));
        System.out.println(getCurrentHour());
    }
}
