package com.ai.modules.tracker.constant;

/**
 * tracker 模块 MQ 常量
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
public interface TrackerMqConstant {

    /** 订阅的营养事件 Topic（与 ai-nutrition 保持一致） */
    String TOPIC_NUTRITION_EVENT = "nutrition_event_topic";

    /** 饮食记录 Tag */
    String TAG_MEAL_LOGGED = "meal_logged";

    /** 本模块消费组 */
    String GROUP_TRACKER_AGGREGATOR = "group-tracker-aggregator";
}
