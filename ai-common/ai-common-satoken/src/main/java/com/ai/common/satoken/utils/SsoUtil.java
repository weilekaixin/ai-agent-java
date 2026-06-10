package com.ai.common.satoken.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.satoken.domain.LoginInfo;
import com.ai.common.satoken.domain.SsoUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 用户登录相关操作工具类
 *
 * @author zhangyunlong
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SsoUtil {

    /** 主会话存储用户对象的 key */
    public static final String USER = "USER";

    /** 主会话存储用户ID的 key */
    public static final String USER_ID = "USER_ID";

    /** token 会话存储登录信息的 key */
    public static final String LOGIN_INFO = "LOGIN_INFO";


    /**
     * 获取登录用户信息
     */
    public static SsoUser getLoginUser() {
        return getLoginUser(true);
    }

    /**
     * 获取登录用户信息
     *
     * @param isLogin 是否必须登录
     */
    public static SsoUser getLoginUser(boolean isLogin) {
        if (!isLogin && !StpUtil.isLogin()) {
            return null;
        }
        return (SsoUser) StpUtil.getSession().get(USER);
    }

    /**
     * 根据 token 获取登录用户信息
     */
    public static SsoUser getLoginUserByToken(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        Object loginId = StpUtil.getLoginIdByToken(token);
        if (ObjUtil.isNull(loginId)) {
            return null;
        }
        SaSession session = StpUtil.getSessionByLoginId(loginId, false);
        if (ObjUtil.isNull(session)) {
            return null;
        }
        return (SsoUser) session.get(USER);
    }

    /**
     * 获取当前用户 ID
     */
    public static Long getUserId() {
        return getUserId(true);
    }

    /**
     * 获取当前用户 ID
     *
     * @param isLogin 是否必须登录
     */
    public static Long getUserId(boolean isLogin) {
        try {
            if (!isLogin && !StpUtil.isLogin()) {
                return null;
            }
            return (Long) StpUtil.getSession().get(USER_ID);
        } catch (Exception e) {
            log.debug("getUserId failed: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取登录信息（token 会话级别）
     */
    public static LoginInfo getLoginInfo() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        return StpUtil.getTokenSession().get(LOGIN_INFO, new LoginInfo());
    }

    /**
     * 生成加密密码（MD5(account + password + salt)）
     */
    public static String encryptPassword(String account, String password, String salt) {
        return DigestUtils.md5DigestAsHex(
            (account + password + salt).getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 生成6位随机盐值
     */
    public static String generateSalt() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(chars[ThreadLocalRandom.current().nextInt(chars.length)]);
        }
        return sb.toString();
    }
}
