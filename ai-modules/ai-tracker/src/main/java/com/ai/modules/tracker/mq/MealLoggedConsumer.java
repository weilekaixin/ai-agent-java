package com.ai.modules.tracker.mq;

import com.ai.modules.tracker.domain.entity.DailyCalorieSummary;
import com.ai.modules.tracker.mapper.DailyCalorieSummaryMapper;
import com.ai.modules.tracker.mq.dto.MealLoggedEvent;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 饮食记录事件消费者 — 聚合每日卡路里
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = "nutrition_event_topic",
        selectorExpression = "meal_logged",
        consumerGroup = "group-tracker-aggregator"
)
public class MealLoggedConsumer implements RocketMQListener<MealLoggedEvent> {

    private final DailyCalorieSummaryMapper summaryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(MealLoggedEvent event) {
        DailyCalorieSummary summary = summaryMapper.selectByUserAndDate(event.getUserId(), event.getMealDate());
        if (summary == null) {
            summary = new DailyCalorieSummary();
            summary.setUserId(event.getUserId());
            summary.setSummaryDate(event.getMealDate());
            summary.setTotalCalories(event.getCalories());
            summary.setUpdateTime(LocalDateTime.now());
            summaryMapper.insert(summary);
        } else {
            summaryMapper.update(null, new LambdaUpdateWrapper<DailyCalorieSummary>()
                    .eq(DailyCalorieSummary::getId, summary.getId())
                    .setSql("total_calories = total_calories + " + event.getCalories())
                    .set(DailyCalorieSummary::getUpdateTime, LocalDateTime.now()));
        }
    }
}
