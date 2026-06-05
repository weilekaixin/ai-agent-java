package com.ai.modules.training.model.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 训练计划
 * 新增/编辑
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class TrainingPlanQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "计划名称不能为空！")
    private String name;

    private String description;

    private Integer weeklyFrequency;

    /** 状态：active / inactive */
    private String status;
}
