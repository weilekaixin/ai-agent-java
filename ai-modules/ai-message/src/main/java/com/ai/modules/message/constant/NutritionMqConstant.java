package com.ai.modules.message.constant;

/**
 * 营养模块 MQ 常量
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
public interface NutritionMqConstant {

    /** 营养事件 Topic */
    String TOPIC_NUTRITION_EVENT = "nutrition_event_topic";

    /** 饮食记录写入 Tag */
    String TAG_MEAL_LOGGED = "meal_logged";

    /** ai-tracker 消费组 */
    String GROUP_TRACKER_AGGREGATOR = "group-tracker-aggregator";
}
