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

    /** 目标肌肉群（逗号分隔，如 chest,back） */
    private String targetMuscles;

    /** 预计时长（分钟） */
    private Integer estimatedMin;

    /** 难度：beginner/intermediate/advanced */
    private String difficulty;

    private String remark;
}
