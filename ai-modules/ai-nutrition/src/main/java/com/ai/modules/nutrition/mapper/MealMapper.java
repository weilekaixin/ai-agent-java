package com.ai.modules.nutrition.mapper;

import com.ai.modules.nutrition.domain.entity.Meal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饮食记录 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface MealMapper extends BaseMapper<Meal> {

    /**
     * 统计用户某天总热量
     *
     * @param userId   用户ID
     * @param mealDate 日期
     */
    @Select("SELECT COALESCE(SUM(calories), 0) FROM meal WHERE user_id = #{userId} AND meal_date = #{mealDate} AND del_flag = 0")
    BigDecimal sumCaloriesByDate(Long userId, LocalDate mealDate);
}
