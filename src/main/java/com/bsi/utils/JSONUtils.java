package com.bsi.utils;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * json工具类
 * @author fish
 */
public class JSONUtils {

    /**
     * 对象转json
     * @param obj
     * @return String
     */
    private static String toJson(Object obj){
        return JSON.toJSONString( obj );
    }

    /**
     * json转map
     * @param json
     * @return
     */
    private static Map<String,Object> toMap(String json){
        return JSON.parseObject(json,Map.class);
    }

}
