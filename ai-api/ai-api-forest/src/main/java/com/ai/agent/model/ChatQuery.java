package com.ai.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 聊天请求体
 *
 * @author root 2026-05-16 16:04
 */
@Data
public class ChatQuery {
    /**
     * 会话ID
     */
    @JsonProperty("session_id")
    private String sessionId;
    /**
     * 用户消息
     */
    @NotBlank(message = "message 不能为空")
    private String message;
    /**
     * 自定义 AI 角色 ID（可选，注入 system_prompt）
     */
    @Size(max = 36, message = "persona_id 最多 36 字符")
    @JsonProperty("persona_id")
    private String personaId;
}
