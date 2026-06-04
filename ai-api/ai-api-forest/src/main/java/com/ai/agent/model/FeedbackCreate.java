package com.ai.agent.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 消息反馈请求体
 * rating: 1 表示点赞，-1 表示踩
 */
@Data
public class FeedbackCreate {

    @NotNull(message = "rating 不能为空，1 表示点赞，-1 表示踩")
    private Integer rating;

    @Size(max = 1000, message = "comment 最多 1000 字符")
    private String comment;
}
