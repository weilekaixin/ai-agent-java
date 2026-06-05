package com.ai.modules.training.model.query;

import com.ai.common.mybatis.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 运动动作库
 * 列表查询
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExerciseListQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 动作名称（模糊） */
    private String name;

    /** 肌肉群 */
    private String muscleGroup;

    /** 器械类型 */
    private String equipmentType;

    private List<Long> idList;
}
