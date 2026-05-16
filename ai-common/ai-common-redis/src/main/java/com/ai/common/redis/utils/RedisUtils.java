package com.ai.common.redis.utils;

import cn.hutool.core.collection.CollUtil;
import com.ai.common.core.utils.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 *
 * @author root 2026-05-16 16:04
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisUtils {

    private static RedissonClient redissonClient;

    /**
     * 初始化 RedissonClient
     */
    public static void setClient(RedissonClient client) {
        redissonClient = client;
    }

    /**
     * 获取 RedissonClient
     */
    private static RedissonClient getClient() {
        if (redissonClient == null) {
            redissonClient = SpringUtils.getBean(RedissonClient.class);
        }
        return redissonClient;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static <T> T getCacheObject(String key) {
        RBucket<T> bucket = getClient().getBucket(key);
        return bucket.get();
    }

    /**
     * 设置缓存（永不过期）
     *
     * @param key   键
     * @param value 值
     */
    public static <T> void setCacheObject(String key, T value) {
        RBucket<T> bucket = getClient().getBucket(key);
        bucket.set(value);
    }

    /**
     * 设置缓存（带过期时间）
     *
     * @param key      键
     * @param value    值
     * @param duration 过期时间
     */
    public static <T> void setCacheObject(String key, T value, Duration duration) {
        RBucket<T> bucket = getClient().getBucket(key);
        bucket.set(value, duration);
    }

    /**
     * 设置缓存（保留过期时间）
     *
     * @param key   键
     * @param value 值
     */
    public static <T> void setCacheObject(String key, T value, boolean keepTtl) {
        RBucket<T> bucket = getClient().getBucket(key);
        if (keepTtl) {
            long ttl = bucket.remainTimeToLive();
            if (ttl > 0) {
                bucket.set(value, ttl, TimeUnit.MILLISECONDS);
                return;
            }
        }
        bucket.set(value);
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否删除成功
     */
    public static boolean deleteObject(String key) {
        return getClient().getBucket(key).delete();
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public static boolean hasKey(String key) {
        return getClient().getBucket(key).isExists();
    }

    /**
     * 获取缓存剩余存活时间
     *
     * @param key 键
     * @return 剩余存活时间（毫秒）
     */
    public static long getTimeToLive(String key) {
        return getClient().getBucket(key).remainTimeToLive();
    }

    /**
     * 设置过期时间
     *
     * @param key      键
     * @param duration 过期时间
     */
    public static boolean expire(String key, Duration duration) {
        RBucket<Object> bucket = getClient().getBucket(key);
        return bucket.expire(duration);
    }

    /**
     * 按模式匹配Key
     *
     * @param pattern 模式
     * @return 匹配的key集合
     */
    public static Collection<String> keys(String pattern) {
        RKeys keys = getClient().getKeys();
        return CollUtil.addAll(new java.util.ArrayList<>(), keys.getKeysByPattern(pattern));
    }
}
