package com.bsi.md.agent.engine.integration.transform;

import com.bsi.md.agent.engine.integration.Context;
import com.bsi.md.agent.engine.script.AgScriptEngine;

/**
 * 转换接口
 * @author fish
 */
public interface AgTransform {
    /**
     * 数据转换
     * @param context
     * @return
     */
    Object transform(Context context) throws Exception;

    /**
     * 设置执行脚本
     * @param script
     * @return
     */
    String setScript(String script);

    /**
     * 设置执行引擎
     * @param engine
     * @return
     */
    void setEngine(AgScriptEngine engine);
}
