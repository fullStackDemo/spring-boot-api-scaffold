package com.scaffold.test.utils;

/**
 * 系统工具类
 * @author alex
 */

public class SystemUtils {

    /**
     * 是否是mac
     * @return true
     */
    public static boolean isMac(){
        return System.getProperty("os.name").contains("Mac");
    }

    /**
     * 是否是window
     * @return true
     */
    public static boolean isWindow(){
        return System.getProperty("os.name").contains("Window");
    }
}
