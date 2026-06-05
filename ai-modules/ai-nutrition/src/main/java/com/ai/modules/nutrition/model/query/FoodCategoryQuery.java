package com.ai.modules.nutrition.model.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 食物分类
 * 新增/编辑
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class FoodCategoryQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 分类ID（编辑时传入） */
    private Long id;

    @NotBlank(message = "分类名称不能为空！")
    private String name;

    private String icon;

    private Integer sort;
}
