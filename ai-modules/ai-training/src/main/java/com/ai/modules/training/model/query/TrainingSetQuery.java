package com.ai.modules.training.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 训练组
 * 新增/编辑
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class TrainingSetQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "训练会话ID不能为空！")
    private Long sessionId;

    @NotNull(message = "动作ID不能为空！")
    private Long exerciseId;

    private Integer setOrder;

    private Integer reps;

    private BigDecimal weightKg;

    private Integer durationSec;

    private Boolean done;

    private String notes;
}
