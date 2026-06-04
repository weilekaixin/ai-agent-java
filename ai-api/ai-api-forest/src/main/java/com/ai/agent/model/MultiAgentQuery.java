package com.ai.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 多智能体协作对话请求体
 *
 * @author root 2026-06-04
 */
@Data
public class MultiAgentQuery {
    /**
     * 会话ID
     */
    @JsonProperty("session_id")
    private String sessionId;
    /**
     * 用户消息
     */
    private String message;
}
