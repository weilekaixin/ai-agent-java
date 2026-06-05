package com.ai.modules.nutrition.model.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 食物库
 * 新增/编辑
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class FoodQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 食物ID（编辑时传入） */
    private Long id;

    @NotBlank(message = "食物名称不能为空！")
    private String name;

    @NotNull(message = "食物分类不能为空！")
    private Long categoryId;

    @NotNull(message = "热量不能为空！")
    private BigDecimal calories;

    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbs;
    private BigDecimal fiber;

    /** 计量单位（g/ml/个） */
    private String unit;
}
