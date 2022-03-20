package com.bsi.md.agent.engine.integration.output;

import com.bsi.md.agent.engine.integration.Context;
import com.bsi.md.agent.engine.script.AgScriptEngine;

/**
 * 输出接口
 * @author fish
 */
public interface AgOutput {
    /**
     * 写入数据
     * @param context
     * @return
     */
    Object write(Context context) throws Exception;
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
