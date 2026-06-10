package com.ai.common.satoken.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.ai.common.core.enums.LoginSource;
import com.ai.common.satoken.domain.LoginInfo;
import com.ai.common.satoken.domain.SsoUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 登录鉴权助手
 *
 * @author zhangyunlong
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    /**
     * 登录系统
     */
    public static void login(SsoUser ssoUser, LoginSource source, String devKey) {
        // loginId 格式 userType:userId，供 StpInterface 解析
        StpUtil.login(ssoUser.getUserType() + ":" + ssoUser.getUserId());

        // 缓存用户信息到主会话
        StpUtil.getSession().set(SsoUtil.USER, ssoUser);
        StpUtil.getSession().set(SsoUtil.USER_ID, ssoUser.getUserId());

        // 缓存登录信息到 token 会话
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(ssoUser.getUserId());
        loginInfo.setLoginTime(System.currentTimeMillis());
        loginInfo.setLoginSource(source != null ? String.valueOf(source.getCode()) : null);
        loginInfo.setDevKey(devKey);
        StpUtil.getTokenSession().set(SsoUtil.LOGIN_INFO, loginInfo);
    }

    public static Long getUserId() {
        return SsoUtil.getUserId();
    }

    @SuppressWarnings("unchecked")
    public static <T extends SsoUser> T getLoginUser() {
        return (T) SsoUtil.getLoginUser();
    }

    public static LoginInfo getLoginInfo() {
        return SsoUtil.getLoginInfo();
    }

    public static boolean isLogin() {
        return StpUtil.isLogin();
    }
}
