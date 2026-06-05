package com.ai.modules.nutrition.service;

import com.ai.common.core.exception.BusinessException;
import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.common.rocketmq.support.RocketSender;
import com.ai.modules.nutrition.constant.NutritionMqConstant;
import com.ai.modules.nutrition.domain.entity.Food;
import com.ai.modules.nutrition.domain.entity.Meal;
import com.ai.modules.nutrition.domain.event.MealLoggedEvent;
import com.ai.modules.nutrition.mapper.MealMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * 饮食记录业务
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Service
@RequiredArgsConstructor
public class MealService {

    private final MealMapper mealMapper;
    private final FoodService foodService;
    private final RocketSender rocketSender;

    /**
     * 记录饮食，写入DB后异步发 MQ 事件通知 ai-tracker 聚合
     *
     * @param meal 饮食记录（需含 foodId、amount、mealDate、mealType、userId）
     */
    @Transactional(rollbackFor = Exception.class)
    public Meal log(Meal meal) {
        Food food = foodService.getById(meal.getFoodId());
        if (ObjectUtil.isNull(food)) {
            throw new BusinessException("食物不存在！");
        }
        // 计算本次摄入热量 = amount / 100 * calories
        BigDecimal calories = food.getCalories()
                .multiply(meal.getAmount())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        meal.setCalories(calories);

        mealMapper.insert(meal);

        // 发 MQ 事件（异步，不阻塞写入结果）
        MealLoggedEvent event = new MealLoggedEvent(
                meal.getId(), meal.getUserId(), meal.getFoodId(),
                calories, meal.getMealDate(), meal.getMealType());
        rocketSender.syncSend(NutritionMqConstant.TOPIC_NUTRITION_EVENT,
                NutritionMqConstant.TAG_MEAL_LOGGED, event);

        return meal;
    }

    /** 分页查询用户某天的饮食记录 */
    public IPage<Meal> pageByDate(Long userId, LocalDate mealDate, PageQuery pageQuery) {
        LambdaQueryWrapper<Meal> wrapper = new LambdaQueryWrapper<Meal>()
                .eq(Meal::getUserId, userId)
                .eq(ObjectUtil.isNotNull(mealDate), Meal::getMealDate, mealDate)
                .orderByDesc(Meal::getCreateTime);
        return mealMapper.selectPage(pageQuery.build(), wrapper);
    }

    /** 查询用户某天总热量 */
    public BigDecimal todayCalories(Long userId, LocalDate date) {
        return mealMapper.sumCaloriesByDate(userId, date);
    }

    /** 删除饮食记录 */
    public void remove(Long id) {
        mealMapper.deleteById(id);
    }
}
