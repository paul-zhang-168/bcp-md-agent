package com.bsi.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 * @author fish
 */
public class DateUtils {

    /**
     * 获取指定格式的当前时间字符串
     * @param pattern
     * @return String
     */
    public static String nowDate(String pattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format( LocalDate.now() );
    }
}
