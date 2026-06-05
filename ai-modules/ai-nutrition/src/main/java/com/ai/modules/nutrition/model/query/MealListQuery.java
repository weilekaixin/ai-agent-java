package com.ai.modules.nutrition.model.query;

import com.ai.common.mybatis.core.page.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 饮食记录
 * 列表查询
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MealListQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 记录日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate mealDate;

    /** 餐次（breakfast/lunch/dinner/snack） */
    private String mealType;

    /** 批量操作ID集合 */
    private List<Long> idList;
}
