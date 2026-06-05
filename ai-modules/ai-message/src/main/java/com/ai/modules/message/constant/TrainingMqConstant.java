package com.ai.modules.message.constant;

/**
 * 训练模块 MQ 常量
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
public interface TrainingMqConstant {

    /** 训练事件 Topic */
    String TOPIC_TRAINING_EVENT = "training_event_topic";

    /** 训练会话完成 Tag */
    String TAG_SESSION_COMPLETED = "session_completed";

    /** ai-tracker 消费消耗热量聚合 消费组 */
    String GROUP_TRACKER_CALORIE_BURN = "group-tracker-calorie-burn";
}
