package com.ai.modules.nutrition.domain.entity;

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
 * 饮食记录
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("meal")
public class Meal extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 食物ID */
    private Long foodId;

    /** 食用量（g/ml/个） */
    private BigDecimal amount;

    /** 本次摄入热量（kcal，冗余计算存储） */
    private BigDecimal calories;

    /** 记录日期 */
    private LocalDate mealDate;

    /** 餐次（breakfast/lunch/dinner/snack） */
    private String mealType;

    /** 备注 */
    private String remark;

    @TableLogic
    private Integer delFlag;
}
