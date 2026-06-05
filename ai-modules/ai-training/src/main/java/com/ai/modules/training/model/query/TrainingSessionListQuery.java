package com.ai.modules.training.model.query;

import com.ai.common.mybatis.core.page.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 训练会话
 * 列表查询
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainingSessionListQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    /** 状态 */
    private String status;

    private List<Long> idList;
}
