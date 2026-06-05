package com.ai.modules.training.model.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 运动动作库
 * 新增/编辑
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class ExerciseQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "动作名称不能为空！")
    private String name;

    @NotBlank(message = "肌肉群不能为空！")
    private String muscleGroup;

    private String equipmentType;

    private Integer caloriesPerMin;

    private String description;
}
