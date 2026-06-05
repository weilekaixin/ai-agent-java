package com.ai.modules.training.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运动动作库
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exercise")
public class Exercise extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 动作名称 */
    private String name;

    /** 肌肉群（chest/back/legs/shoulders/arms/core/cardio） */
    private String muscleGroup;

    /** 器械类型（barbell/dumbbell/machine/bodyweight/cable） */
    private String equipmentType;

    /** 每分钟消耗热量估算（kcal，有氧运动用） */
    private Integer caloriesPerMin;

    /** 动作说明 */
    private String description;

    @TableLogic
    private Integer delFlag;
}
