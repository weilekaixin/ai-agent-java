package com.ai.modules.training.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 训练计划（模板，可复用）
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("training_plan")
public class TrainingPlan extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    /** 计划名称 */
    private String name;

    /** 目标肌肉群（逗号分隔，如 chest,back） */
    private String targetMuscles;

    /** 预计时长（分钟） */
    private Integer estimatedMin;

    /** 难度（beginner/intermediate/advanced） */
    private String difficulty;

    private String remark;

    @TableLogic
    private Integer delFlag;
}
