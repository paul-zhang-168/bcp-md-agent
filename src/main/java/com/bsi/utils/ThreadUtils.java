package com.bsi.utils;

import com.bsi.framework.core.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程工具类
 */
public class ThreadUtils {
    private static final Logger info_log = LoggerFactory.getLogger("TASK_INFO_LOG");
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            info_log.info("线程暂停出现问题，错误信息:{}", ExceptionUtils.getFullStackTrace(e));
        }
    }
}
