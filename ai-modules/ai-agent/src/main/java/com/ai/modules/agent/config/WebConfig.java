package com.ai.modules.agent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局 CORS 配置
 *
 * <p>allowedMethods 包含 PATCH 以支持 RESTful 部分更新操作。
 * exposedHeaders 添加 X-Response-Time 和 X-Request-Id 以便前端读取监控数据。
 *
 * @author root 2026-05-16 16:04
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
            .allowedHeaders("*")
            .exposedHeaders("X-Request-Id", "X-Response-Time")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
