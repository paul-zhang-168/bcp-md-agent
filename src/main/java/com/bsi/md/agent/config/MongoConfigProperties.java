package com.bsi.md.agent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * mongdb配置类
 */
@Configuration
@ConfigurationProperties(prefix = "mongodb")
public class MongoConfigProperties {

    private boolean enabled = false;

    // 静态字段，用于静态方法访问
    private static MongoConfigProperties INSTANCE;

    // 提供静态方法获取值
    public static boolean isEnabled() {
        return INSTANCE != null && INSTANCE.enabled;
    }

    // 初始化时由 Spring 调用，设置静态实例
    @PostConstruct
    public void registerInstance() {
        INSTANCE = this;
    }

    // getter / setter
    public boolean isEnabledInstance() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}