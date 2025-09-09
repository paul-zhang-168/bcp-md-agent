package com.bsi.utils;

import java.util.UUID;

/**
 * 用来生成uuid的工具类
 */
public class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    public static String getShortUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
