package com.ai.modules.tracker.mapper;

import com.ai.modules.tracker.domain.entity.Goal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 目标 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface GoalMapper extends BaseMapper<Goal> {
}
