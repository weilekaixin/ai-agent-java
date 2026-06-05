package com.ai.modules.tracker.mapper;

import com.ai.modules.tracker.domain.entity.DailyCalorieSummary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

/**
 * 每日卡路里聚合 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface DailyCalorieSummaryMapper extends BaseMapper<DailyCalorieSummary> {

    /**
     * 查询用户某天聚合记录
     *
     * @param userId      用户ID
     * @param summaryDate 日期
     */
    @Select("SELECT * FROM daily_calorie_summary WHERE user_id = #{userId} AND summary_date = #{summaryDate} LIMIT 1")
    DailyCalorieSummary selectByUserAndDate(@Param("userId") Long userId, @Param("summaryDate") LocalDate summaryDate);
}
