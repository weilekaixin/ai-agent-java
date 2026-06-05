package com.ai.modules.tracker.mq.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饮食记录事件（从 ai-nutrition 消费）
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class MealLoggedEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long mealId;
    private Long userId;
    private Long foodId;
    private BigDecimal calories;
    private LocalDate mealDate;
    private String mealType;
}
