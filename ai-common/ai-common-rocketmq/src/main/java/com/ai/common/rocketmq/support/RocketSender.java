package com.ai.common.rocketmq.support;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.rocketmq.constant.RocketConstant;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * RocketMQ消息发送者
 *
 * @author zhangyunlong 2026/6/5 13:00
 */
@Component
public class RocketSender {
    /**
     * RocketMQ 内置延迟级别上限
     */
    private static final int MAX_DELAY_LEVEL = 18;

    private final RocketMQTemplate rocketMQTemplate;

    public RocketSender(RocketMQTemplate rocketTemplate) {
        this.rocketMQTemplate = rocketTemplate;
    }

    /**
     * 同步发送，自动生成业务 key
     *
     * @param topic   主题
     * @param payload 消息体
     */
    public <T> SendResult syncSend(String topic, T payload) {
        return rocketMQTemplate.syncSend(topic, this.buildMessage(payload, null));
    }

    /**
     * 同步发送，带 Tag，自动生成业务 key
     *
     * @param topic   主题
     * @param tag     标签
     * @param payload 消息体
     */
    public <T> SendResult syncSend(String topic, String tag, T payload) {
        return rocketMQTemplate.syncSend(this.buildDestination(topic, tag), this.buildMessage(payload, null));
    }

    /**
     * 同步发送，带 Tag 和业务 key
     *
     * @param topic   主题
     * @param tag     标签
     * @param payload 消息体
     * @param bizKey  业务主键，为空时自动生成雪花 ID
     */
    public <T> SendResult syncSend(String topic, String tag, T payload, String bizKey) {
        return rocketMQTemplate.syncSend(this.buildDestination(topic, tag), this.buildMessage(payload, bizKey));
    }

    /**
     * 异步发送，自动生成业务 key
     *
     * @param topic    主题
     * @param payload  消息体
     * @param callback 发送回调
     */
    public <T> void asyncSend(String topic, T payload, SendCallback callback) {
        rocketMQTemplate.asyncSend(topic, this.buildMessage(payload, null), callback);
    }

    /**
     * 单向发送，无返回值
     *
     * @param topic   主题
     * @param payload 消息体
     */
    public <T> void sendOneWay(String topic, T payload) {
        rocketMQTemplate.sendOneWay(topic, payload);
    }

    /**
     * 延迟消息，自动生成业务 key
     *
     * @param topic      主题
     * @param payload    消息体
     * @param delayLevel 延迟级别 1~18
     */
    public <T> SendResult syncSendDelay(String topic, T payload, int delayLevel) {
        if (delayLevel < 1 || delayLevel > MAX_DELAY_LEVEL) {
            throw new IllegalArgumentException("delayLevel 必须在 1~18 之间！");
        }
        return rocketMQTemplate.syncSend(topic, this.buildMessage(payload, null),
            RocketConstant.DEFAULT_SEND_TIMEOUT, delayLevel);
    }

    /**
     * 拼接 destination
     *
     * @param topic 主题
     * @param tag   标签
     * @return topic 或 topic:tag
     */
    private String buildDestination(String topic, String tag) {
        if (StrUtil.isBlank(tag)) {
            return topic;
        }
        return String.format("%s%s%s", topic, RocketConstant.TOPIC_TAG_SEPARATOR, tag);
    }

    /**
     * 构造带业务 key 的 Spring Message
     *
     * @param payload 消息体
     * @param bizKey  业务 key，为空时自动生成雪花 ID
     */
    private <T> Message<T> buildMessage(T payload, String bizKey) {
        String finalKey = StrUtil.isBlank(bizKey) ? IdUtil.getSnowflakeNextIdStr() : bizKey;
        return MessageBuilder.withPayload(payload)
            .setHeader(RocketMQHeaders.KEYS, finalKey)
            .build();
    }
}
