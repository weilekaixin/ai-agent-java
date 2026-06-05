package com.ai.modules.tracker.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日卡路里聚合（由 MQ 事件驱动写入/更新）
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@TableName("daily_calorie_summary")
public class DailyCalorieSummary {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private LocalDate summaryDate;

    /** 当日总摄入热量（kcal） */
    private BigDecimal totalCalories;

    private LocalDateTime updateTime;
}
