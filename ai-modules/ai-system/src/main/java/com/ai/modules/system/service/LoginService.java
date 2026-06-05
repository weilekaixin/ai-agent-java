package com.ai.modules.system.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.ObjectUtil;
import com.ai.common.core.enums.UserType;
import com.ai.common.core.exception.BusinessException;
import com.ai.common.core.utils.ServletUtils;
import com.ai.common.satoken.domain.SsoUser;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.system.domain.entity.User;
import com.ai.modules.system.domain.vo.LoginQuery;
import com.ai.modules.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 登录业务
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户名密码登录
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public String login(LoginQuery query) {
        User user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        if (ObjectUtil.isNull(user)) {
            throw new BusinessException("用户名或密码错误！");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误！");
        }
        if (user.getStatus() != null && user.getStatus() == 1) {
            throw new BusinessException("账号已被禁用！");
        }

        recordLogin(user);

        SsoUser ssoUser = buildSsoUser(user);
        LoginHelper.login(ssoUser, new SaLoginParameter());
        return StpUtil.getTokenValue();
    }

    private SsoUser buildSsoUser(User user) {
        SsoUser ssoUser = new SsoUser();
        ssoUser.setUserId(user.getId());
        ssoUser.setUsername(user.getUsername());
        ssoUser.setNickname(user.getNickname());
        ssoUser.setUserType(UserType.SYS_USER.getUserType());
        return ssoUser;
    }

    private void recordLogin(User user) {
        User update = new User();
        update.setId(user.getId());
        update.setLoginIp(ServletUtils.getClientIP());
        update.setLoginDate(new Date());
        sysUserMapper.updateById(update);
    }
}
