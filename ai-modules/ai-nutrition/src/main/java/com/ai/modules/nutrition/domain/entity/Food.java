package com.ai.modules.nutrition.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 食物（每100g营养数据）
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("food")
public class Food extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 食物名称 */
    private String name;

    /** 所属分类 */
    private Long categoryId;

    /** 每100g热量（kcal） */
    private BigDecimal calories;

    /** 每100g蛋白质（g） */
    private BigDecimal protein;

    /** 每100g脂肪（g） */
    private BigDecimal fat;

    /** 每100g碳水（g） */
    private BigDecimal carbs;

    /** 每100g膳食纤维（g） */
    private BigDecimal fiber;

    /** 计量单位（g/ml/个） */
    private String unit;

    /** 数据来源（manual/api） */
    private String source;

    @TableLogic
    private Integer delFlag;
}
