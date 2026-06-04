package com.ai.agent.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 会话标签创建请求体
 */
@Data
public class TagCreate {

    @NotBlank(message = "tag 不能为空")
    @Size(max = 100, message = "tag 最多 100 字符")
    private String tag;
}
