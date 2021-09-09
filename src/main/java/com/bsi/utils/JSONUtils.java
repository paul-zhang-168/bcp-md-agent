package com.bsi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
    public static String toJson(Object obj){
        if(obj instanceof String){
            return obj.toString();
        }
        return JSON.toJSONString( obj );
    }

    /**
     * json字符串转数组
     * @param text
     * @return
     */
    public static JSONArray parseArray(String text){
        return JSON.parseArray(text);
    }

    /**
     * json字符串转数组
     * @param text
     * @return
     */
    public static JSONObject parseObject(String text){
        return JSON.parseObject(text);
    }

    /**
     * json转map
     * @param json
     * @return
     */
    public static Map<String,Object> toMap(String json){
        return JSON.parseObject(json,Map.class);
    }

}
