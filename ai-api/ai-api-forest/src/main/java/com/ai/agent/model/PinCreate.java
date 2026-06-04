package com.ai.agent.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 消息置顶请求体（备注可选）
 */
@Data
public class PinCreate {

    @Size(max = 500, message = "note 最多 500 字符")
    private String note;
}
