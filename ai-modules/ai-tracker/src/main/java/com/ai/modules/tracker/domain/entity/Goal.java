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
 * 目标设定
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goal")
public class Goal extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    /** 目标类型（weight_loss/muscle_gain/calorie_control） */
    private String goalType;

    /** 目标值 */
    private BigDecimal targetValue;

    /** 当前值（创建时快照） */
    private BigDecimal currentValue;

    /** 截止日期 */
    private LocalDate deadline;

    /** 状态（in_progress/achieved/abandoned） */
    private String status;

    private String remark;

    @TableLogic
    private Integer delFlag;
}
