package com.ai.modules.nutrition.model.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饮食记录
 * 新增/编辑
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class MealQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 记录ID（编辑时传入） */
    private Long id;

    @NotNull(message = "食物不能为空！")
    private Long foodId;

    @NotNull(message = "食用量不能为空！")
    private BigDecimal amount;

    @NotNull(message = "记录日期不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate mealDate;

    @NotNull(message = "餐次不能为空！")
    private String mealType;

    private String remark;
}
