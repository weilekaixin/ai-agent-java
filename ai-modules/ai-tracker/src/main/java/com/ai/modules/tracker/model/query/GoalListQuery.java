package com.ai.modules.tracker.model.query;

import com.ai.common.mybatis.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 目标管理
 * 列表查询
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoalListQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 单条详情ID */
    private Long id;

    /** 目标类型 */
    private String goalType;

    /** 状态 */
    private String status;

    private List<Long> idList;
}
