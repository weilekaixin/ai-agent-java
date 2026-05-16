package com.ai.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 放行白名单配置
 *
 * @author root 2026-05-16 16:04
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreWhiteProperties {

    /**
     * 白名单配置（网关不校验此配置的路径）
     */
    private java.util.List<String> whites = new java.util.ArrayList<>();

}
