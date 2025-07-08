package com.bsi.utils;

import com.bsi.md.agent.datasource.AgDatasourceContainer;
import com.bsi.md.agent.datasource.AgMqttTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttUtils {

    /**
     * 根据主题查询数据
     * @param dataSourceId 数据源id
     * @param topic 主题
     */
    public static void send(String dataSourceId,String topic,String msg,int qos){
        AgMqttTemplate template = AgDatasourceContainer.getMqttDataSource(dataSourceId);
        template.publish(topic,msg,qos);
    }
}