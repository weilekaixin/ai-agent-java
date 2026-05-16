package com.ai.modules.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 聊天配置
 *
 * @author root 2026-05-16 16:04
 */
@Data
@Component
@ConfigurationProperties(prefix = "agent.chat")
public class AgentProperties {
    /**
     * SSE 超时时间（毫秒）
     */
    private Long timeout = 300_000L;
}
