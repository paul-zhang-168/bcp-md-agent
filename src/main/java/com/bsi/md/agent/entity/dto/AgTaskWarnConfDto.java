package com.bsi.md.agent.entity.dto;

import lombok.Data;

/**
 * 任务告警参数传输对象
 * @author fish
 */
@Data
public class AgTaskWarnConfDto {
    private String taskId;
    private String warnMethod;
}
