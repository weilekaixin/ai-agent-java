package com.ai.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "name 不能为空")
    @Size(max = 100, message = "name 最多 100 字符")
    private String name;
    /**
     * 系统提示词（会被注入为首条 SystemMessage）
     */
    @NotBlank(message = "system_prompt 不能为空")
    @JsonProperty("system_prompt")
    private String systemPrompt;
    /**
     * 角色描述（可选）
     */
    @Size(max = 500, message = "description 最多 500 字符")
    private String description;
    /**
     * 头像 URL（可选）
     */
    @Size(max = 200, message = "avatar 最多 200 字符")
    private String avatar;
}
