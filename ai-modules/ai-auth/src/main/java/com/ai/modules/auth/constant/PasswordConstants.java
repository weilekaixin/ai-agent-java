package com.ai.modules.auth.constant;

/**
 * 密码输入约束
 *
 * @author root
 */
public final class PasswordConstants {
    /**
     * 密码最小字符数
     */
    public static final int MIN_LENGTH = 10;

    /**
     * BCrypt 支持的密码最大 UTF-8 字节数
     */
    public static final int MAX_BYTES = 72;

    private PasswordConstants() {
    }
}
