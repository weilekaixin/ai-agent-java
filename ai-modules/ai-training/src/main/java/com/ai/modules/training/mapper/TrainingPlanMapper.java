package com.ai.modules.training.mapper;

import com.ai.modules.training.domain.entity.TrainingPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 训练计划 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface TrainingPlanMapper extends BaseMapper<TrainingPlan> {
}
