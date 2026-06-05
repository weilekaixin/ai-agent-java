package com.ai.modules.nutrition.model.query;

import com.ai.common.mybatis.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 食物分类
 * 列表查询
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FoodCategoryListQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 单条详情ID */
    private Long id;

    /** 分类名称（模糊） */
    private String name;

    /** 批量操作ID集合 */
    private List<Long> idList;
}
