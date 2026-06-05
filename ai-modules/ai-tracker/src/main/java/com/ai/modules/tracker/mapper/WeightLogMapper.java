package com.ai.modules.tracker.mapper;

import com.ai.modules.tracker.domain.entity.WeightLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 体重记录 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface WeightLogMapper extends BaseMapper<WeightLog> {
}
