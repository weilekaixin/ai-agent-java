package com.ai.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 恢复暂停的 Agent 请求体（人工确认同意/拒绝）
 *
 * @author root 2026-05-16 16:04
 */
@Data
public class ResumeQuery {
    /**
     * 会话ID
     */
    @JsonProperty("session_id")
    private String sessionId;

    /**
     * true = 同意执行 false = 拒绝、取消
     */
    private Boolean approved;
}
