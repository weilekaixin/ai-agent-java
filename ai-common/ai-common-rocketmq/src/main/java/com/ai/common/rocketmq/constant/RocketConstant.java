package com.ai.common.rocketmq.constant;

/**
 * RocketMQ 常量
 *
 * @author zhangyunlong 2026/6/5 12:30
 */
public interface RocketConstant {
    /**
     * Topic 与 Tag 拼接分隔符
     */
    String TOPIC_TAG_SEPARATOR = ":";

    /**
     * 默认同步发送超时时间（毫秒）
     */
    long DEFAULT_SEND_TIMEOUT = 3000L;
}
