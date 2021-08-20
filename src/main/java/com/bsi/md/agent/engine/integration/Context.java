package com.bsi.md.agent.engine.integration;

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

    public Object get(String key){
        return env.get(key);
    }
}
