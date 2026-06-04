package com.ai.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 更新 Persona 请求体（全字段可选，只更新非 null 字段）
 *
 * @author root 2026-06-04
 */
@Data
public class PersonaUpdate {
    private String name;
    @JsonProperty("system_prompt")
    private String systemPrompt;
    private String description;
    private String avatar;
    /**
     * 启用/禁用 Persona
     */
    @JsonProperty("is_active")
    private Boolean isActive;
}
