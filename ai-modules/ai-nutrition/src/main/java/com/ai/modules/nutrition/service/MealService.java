package com.ai.modules.nutrition.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.domain.R;
import com.ai.common.rocketmq.support.RocketSender;
import com.ai.modules.message.constant.NutritionMqConstant;
import com.ai.modules.message.event.MealLoggedEvent;
import com.ai.modules.nutrition.domain.entity.Food;
import com.ai.modules.nutrition.domain.entity.Meal;
import com.ai.modules.nutrition.mapper.MealMapper;
import com.ai.modules.nutrition.model.query.MealListQuery;
import com.ai.modules.nutrition.model.query.MealQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * 饮食记录
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class MealService extends ServiceImpl<MealMapper, Meal> {

    @Resource
    private FoodService foodService;
    @Resource
    private RocketSender rocketSender;

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<Meal> listPage(Long userId, MealListQuery query) {
        var wrapper = this.buildWrapper(userId, query).orderByDesc(Meal::getCreateTime);
        return this.page(query.build(), wrapper);
    }

    /**
     * 记录饮食，写入DB后发 MQ 事件通知 ai-tracker 聚合
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> log(Long userId, MealQuery query) {
        Food food = foodService.getById(query.getFoodId());
        if (ObjectUtil.isNull(food)) {
            return R.fail("食物不存在，请刷新后重试！");
        }
        Meal data = new Meal();
        data.setUserId(userId);
        this.copyFields(data, query);
        // 计算本次摄入热量 = amount / 100 * calories
        BigDecimal calories = food.getCalories()
                .multiply(query.getAmount())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        data.setCalories(calories);
        baseMapper.insert(data);
        // 发 MQ 事件
        var event = new MealLoggedEvent(data.getId(), userId, query.getFoodId(),
                calories, query.getMealDate(), query.getMealType());
        rocketSender.syncSend(NutritionMqConstant.TOPIC_NUTRITION_EVENT,
                NutritionMqConstant.TAG_MEAL_LOGGED, event);
        return R.ok(data);
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> delBatch(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return R.ok();
        }
        this.removeByIds(idList);
        return R.ok();
    }

    /**
     * 查询用户某天总热量
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public BigDecimal todayCalories(Long userId, LocalDate date) {
        return baseMapper.sumCaloriesByDate(userId, date);
    }

    /**
     * 构建查询条件
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private LambdaQueryWrapper<Meal> buildWrapper(Long userId, MealListQuery query) {
        var wrapper = Wrappers.lambdaQuery(Meal.class);
        wrapper.eq(Meal::getUserId, userId)
                .eq(ObjectUtil.isNotNull(query.getMealDate()), Meal::getMealDate, query.getMealDate())
                .eq(StrUtil.isNotBlank(query.getMealType()), Meal::getMealType, query.getMealType())
                .in(CollUtil.isNotEmpty(query.getIdList()), Meal::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(Meal entity, MealQuery query) {
        entity.setFoodId(query.getFoodId());
        entity.setAmount(query.getAmount());
        entity.setMealDate(query.getMealDate());
        entity.setMealType(query.getMealType());
        entity.setRemark(query.getRemark());
    }
}
