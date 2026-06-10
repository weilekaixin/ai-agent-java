package com.ai.common.core.constant;

/**
 * 缓存常量
 *
 * @author zhangyunlong
 */
public interface CacheConstants {

    /**
     * 登录错误次数缓存前缀
     */
    String LOGIN_ERROR_COUNT_KEY = "login_error_count:";

    /**
     * 最大登录错误次数
     */
    int LOGIN_ERROR_MAX_COUNT = 5;

    /**
     * 登录错误锁定时间（分钟）
     */
    int LOGIN_ERROR_LOCK_MINUTES = 5;
}
