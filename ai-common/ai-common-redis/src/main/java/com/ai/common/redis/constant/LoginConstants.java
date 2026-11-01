package com.ai.common.redis.constant;

/**
 * 跨服务登录会话属性
 */
public final class LoginConstants {
    /**
     * 是否已修改初始密码，未设置时不能访问业务接口
     */
    public static final String PASSWORD_CHANGED = "passwordChanged";

    private LoginConstants() {
    }
}
