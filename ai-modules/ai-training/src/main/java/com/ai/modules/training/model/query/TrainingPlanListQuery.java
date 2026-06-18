package com.ai.modules.training.model.query;

import com.ai.common.mybatis.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 训练计划
 * 列表查询
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainingPlanListQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 计划名称（模糊） */
    private String name;

    /** 难度：beginner/intermediate/advanced */
    private String difficulty;

    private List<Long> idList;
}
