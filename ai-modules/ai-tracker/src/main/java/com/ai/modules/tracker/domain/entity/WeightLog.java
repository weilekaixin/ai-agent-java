package com.ai.modules.tracker.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 体重记录
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("weight_log")
public class WeightLog extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    /** 体重（kg） */
    private BigDecimal weight;

    /** 体脂率（%） */
    private BigDecimal bodyFat;

    /** 记录日期 */
    private LocalDate logDate;

    private String remark;

    @TableLogic
    private Integer delFlag;
}
