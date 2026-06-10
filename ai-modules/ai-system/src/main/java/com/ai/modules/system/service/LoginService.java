package com.ai.modules.system.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.constant.CacheConstants;
import com.ai.common.core.enums.LoginSource;
import com.ai.common.core.enums.StatusEnum;
import com.ai.common.core.enums.UserType;
import com.ai.common.core.exception.BusinessException;
import com.ai.common.core.utils.ServletUtils;
import com.ai.common.redis.utils.RedisUtils;
import com.ai.common.satoken.domain.SsoUser;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.common.satoken.utils.SsoUtil;
import com.ai.modules.system.domain.entity.User;
import com.ai.modules.system.domain.vo.LoginQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

/**
 * 登录校验
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;

    public String login(LoginQuery query) {
        LoginSource source = ObjUtil.defaultIfNull(query.getSource(), LoginSource.PC);

        // 登录错误次数检查
        String errKey = CacheConstants.LOGIN_ERROR_COUNT_KEY + query.getUsername();
        Integer errNum = ObjUtil.defaultIfNull(RedisUtils.getCacheObject(errKey), 0);
        if (errNum >= CacheConstants.LOGIN_ERROR_MAX_COUNT) {
            throw new BusinessException("登录错误尝试次数达到限制，请5分钟后再试！");
        }

        // 用户是否存在
        User user = userService.getUserByAccount(query.getUsername());
        if (ObjUtil.isNull(user)) {
            RedisUtils.setCacheObject(errKey, ++errNum, Duration.ofMinutes(CacheConstants.LOGIN_ERROR_LOCK_MINUTES));
            throw new BusinessException("用户名或密码错误！");
        }

        // 密码是否正确
        if (!SsoUtil.encryptPassword(query.getUsername(), query.getPassword(), user.getSalt())
            .equals(user.getPassword())) {
            RedisUtils.setCacheObject(errKey, ++errNum, Duration.ofMinutes(CacheConstants.LOGIN_ERROR_LOCK_MINUTES));
            throw new BusinessException("用户名或密码错误！");
        }

        // 账号是否禁用
        if (StatusEnum.DISABLED.getCode().equals(user.getStatus())) {
            throw new BusinessException("账号已被禁用！");
        }

        // 清除错误次数
        RedisUtils.deleteObject(errKey);

        // 更新登录信息
        this.recordLogin(user);

        LoginHelper.login(this.buildSsoUser(user), source, query.getDeviceKey());
        return StpUtil.getTokenValue();
    }

    /**
     * 构建SSO
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private SsoUser buildSsoUser(User user) {
        SsoUser ssoUser = new SsoUser();
        ssoUser.setUserId(user.getId());
        ssoUser.setAccount(user.getAccount());
        ssoUser.setNickname(user.getNickname());
        ssoUser.setPhone(user.getPhone());
        ssoUser.setUserType(UserType.SYS_USER.getUserType());
        ssoUser.setUserRoleType(user.getUserRoleType());
        ssoUser.setInitPassword(user.getInitPassword());
        return ssoUser;
    }

    /**
     * 记录登录
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void recordLogin(User user) {
        User update = new User();
        update.setId(user.getId());
        update.setLoginIp(ServletUtils.getClientIP());
        update.setLoginDate(new Date());
        userService.updateById(update);
    }
}
