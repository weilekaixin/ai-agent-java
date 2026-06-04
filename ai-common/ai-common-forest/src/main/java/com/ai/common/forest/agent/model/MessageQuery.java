package com.ai.common.forest.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 聊天请求体
 *
 * @author root 2026-05-16 16:04
 */
@Data
public class MessageQuery {
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
