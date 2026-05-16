package com.ai.common.redis.config;

import com.ai.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 配置
 *
 * @author root 2026-05-16 16:04
 */
@Slf4j
@AutoConfiguration
public class RedisConfiguration {

    @Bean
    public RedisUtils redisUtils(RedissonClient redissonClient) {
        RedisUtils.setClient(redissonClient);
        log.debug("RedisUtils initialized with RedissonClient");
        return null; // RedisUtils is a static utility class, this bean just initializes it
    }
}
