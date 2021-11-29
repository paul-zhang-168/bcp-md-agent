package com.bsi.md.agent.aop;

import com.alibaba.fastjson.JSON;
import com.bsi.framework.core.vo.resp.Resp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class AgConfigControllerAop {
    @AfterReturning(value = "execution(* com.bsi.md.agent.controller.AgConfigController.*(..))",returning = "resp")
    public void printReturn(Resp resp){
        log.info("AgConfigController接口返回值:{}", JSON.toJSONString(resp));
    }
}
