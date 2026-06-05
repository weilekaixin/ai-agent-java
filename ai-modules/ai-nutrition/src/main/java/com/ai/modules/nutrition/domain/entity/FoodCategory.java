package com.ai.modules.nutrition.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 食物分类
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("food_category")
public class FoodCategory extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 分类名称 */
    private String name;

    /** 分类图标 */
    private String icon;

    /** 排序 */
    private Integer sort;

    @TableLogic
    private Integer delFlag;
}
