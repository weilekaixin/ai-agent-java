package com.ai.modules.training.mapper;

import com.ai.modules.training.domain.entity.TrainingSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 训练组 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface TrainingSetMapper extends BaseMapper<TrainingSet> {

    /**
     * 统计会话已完成组数
     *
     * @author zhangyunlong 2026/6/5 00:00
     * @param sessionId 会话ID
     */
    @Select("SELECT COUNT(*) FROM training_set WHERE session_id = #{sessionId} AND done = 1 AND del_flag = 0")
    int countDoneBySession(@Param("sessionId") Long sessionId);

    /**
     * 统计会话总组数
     *
     * @author zhangyunlong 2026/6/5 00:00
     * @param sessionId 会话ID
     */
    @Select("SELECT COUNT(*) FROM training_set WHERE session_id = #{sessionId} AND del_flag = 0")
    int countTotalBySession(@Param("sessionId") Long sessionId);
}
