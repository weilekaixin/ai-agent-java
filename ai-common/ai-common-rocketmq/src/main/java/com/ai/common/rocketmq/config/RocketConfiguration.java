package com.ai.common.rocketmq.config;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;

/**
 * RocketMQ 自动装配
 *
 * @author zhangyunlong 2026/6/5 12:30
 */
@AutoConfiguration
@ConditionalOnClass(RocketMQTemplate.class)
@ComponentScan("com.ai.common.rocketmq.support")
public class RocketConfiguration {
}
