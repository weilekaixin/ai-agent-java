package com.ai.modules.training.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 训练组（会话下的每组动作）
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("training_set")
public class TrainingSet extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long sessionId;

    private Long exerciseId;

    /** 第几组 */
    private Integer setOrder;

    /** 重量（kg，有氧运动为0） */
    private BigDecimal weight;

    /** 次数（有氧运动填时长分钟） */
    private Integer reps;

    /** 完成状态（0=计划 1=完成） */
    private Integer done;

    private String remark;

    @TableLogic
    private Integer delFlag;
}
