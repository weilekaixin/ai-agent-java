package com.ai.common.redis.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoForRedisson;
import com.ai.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Redis 配置
 *
 * @author root 2026-05-16 16:04
 */
@Slf4j
@AutoConfiguration
public class RedisConfiguration {

    /**
     * 使用 Redis 共享 Sa-Token 登录会话
     */
    @Bean
    public SaTokenDao saTokenDao(RedissonClient redissonClient) {
        return new SaTokenDaoForRedisson(redissonClient);
    }

    @Bean
    public RedisUtils redisUtils(RedissonClient redissonClient) {
        RedisUtils.setClient(redissonClient);
        log.debug("RedisUtils initialized with RedissonClient");
        return null; // RedisUtils is a static utility class, this bean just initializes it
    }
}
