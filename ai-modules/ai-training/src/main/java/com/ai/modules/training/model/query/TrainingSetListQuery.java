package com.ai.modules.training.model.query;

import com.ai.common.mybatis.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 训练组
 * 列表查询
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainingSetListQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 训练会话ID */
    private Long sessionId;

    /** 动作ID */
    private Long exerciseId;

    /** 是否完成 */
    private Boolean done;

    private List<Long> idList;
}
