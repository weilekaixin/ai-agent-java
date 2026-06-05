package com.ai.modules.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饮食记录写入事件
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
