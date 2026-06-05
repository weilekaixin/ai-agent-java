package com.ai.modules.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 训练会话完成事件（发往 ai-tracker 聚合消耗热量）
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionCompletedEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long sessionId;
    private Long userId;
    private LocalDate trainDate;
    /** 本次消耗热量（kcal） */
    private BigDecimal caloriesBurned;
    /** 训练时长（分钟） */
    private Integer durationMin;
}
