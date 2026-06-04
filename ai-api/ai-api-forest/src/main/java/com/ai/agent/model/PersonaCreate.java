package com.ai.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 创建 Persona 请求体
 *
 * @author root 2026-06-04
 */
@Data
public class PersonaCreate {
    /**
     * 角色名称
     */
    private String name;
    /**
     * 系统提示词（会被注入为首条 SystemMessage）
     */
    @JsonProperty("system_prompt")
    private String systemPrompt;
    /**
     * 角色描述（可选）
     */
    private String description;
    /**
     * 头像 URL（可选）
     */
    private String avatar;
}
