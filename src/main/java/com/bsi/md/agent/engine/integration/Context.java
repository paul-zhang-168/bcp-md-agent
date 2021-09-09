package com.bsi.md.agent.engine.integration;

import com.bsi.md.agent.constant.AgConstant;

import java.util.Map;

/**
 * 上下文
 * @author fish
 */
public class Context {

    public Context(){

    }
    private Map env = null;

    public void setEnv(Map map){
        this.env = map;
    }

    public Map getEnv(){
        return this.env;
    }

    public void put(String key,Object value){
        env.put(key,value);
    }

    public Object getParams(){
        return env.get("config");
    }

    public Object getData(){
        return env.get(AgConstant.AG_DATA);
    }

    public Object get(String key){
        return env.get(key);
    }
}
