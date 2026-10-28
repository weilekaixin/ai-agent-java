package com.ai.modules.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.exception.BusinessException;
import com.ai.modules.auth.constant.PasswordConstants;
import com.ai.modules.auth.domain.dto.ChangePasswordDTO;
import com.ai.modules.auth.domain.dto.LoginDTO;
import com.ai.modules.auth.domain.vo.LoginVO;
import com.ai.modules.auth.domain.vo.UserVO;
import com.ai.modules.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 登录业务
 *
 * @author root
 */
@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 校验账号密码并创建登录会话
     */
    public LoginVO login(LoginDTO dto) {
        var user = userMapper.selectByUsername(dto.getUsername());
        if (ObjUtil.isNull(user) || !Boolean.TRUE.equals(user.getEnabled())
                || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        StpUtil.login(user.getUserId());
        LoginVO vo = new LoginVO();
        vo.setTokenName(StpUtil.getTokenName());
        vo.setTokenValue(StpUtil.getTokenValue());
        vo.setMustChangePassword(user.getMustChangePassword());
        return vo;
    }

    /**
     * 查询当前登录用户，不返回密码和内部字段
     */
    public UserVO getUserInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        var user = userMapper.selectById(userId);
        if (ObjUtil.isNull(user) || !Boolean.TRUE.equals(user.getEnabled())) {
            throw new BusinessException("账号不存在或已禁用");
        }
        UserVO vo = new UserVO();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setRole(user.getRole());
        vo.setMustChangePassword(user.getMustChangePassword());
        return vo;
    }

    /**
     * 退出当前设备，不影响其他设备
     */
    public void logout() {
        StpUtil.checkLogin();
        StpUtil.logout();
    }

    /**
     * 校验当前用户已修改初始密码
     */
    public void checkPasswordChanged() {
        var user = this.getUserInfo();
        if (Boolean.TRUE.equals(user.getMustChangePassword())) {
            throw new BusinessException("请先修改初始密码", HttpStatus.FORBIDDEN.value());
        }
    }

    /**
     * 修改当前用户密码，成功后退出所有设备
     */
    public void changePassword(ChangePasswordDTO dto) {
        long userId = StpUtil.getLoginIdAsLong();
        var user = userMapper.selectById(userId);
        if (ObjUtil.isNull(user) || !Boolean.TRUE.equals(user.getEnabled())) {
            throw new BusinessException("账号不存在或已禁用");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        String newPassword = dto.getNewPassword();
        if (StrUtil.isBlank(newPassword) || newPassword.length() < PasswordConstants.MIN_LENGTH
                || newPassword.getBytes(StandardCharsets.UTF_8).length > PasswordConstants.MAX_BYTES) {
            throw new BusinessException(String.format("新密码至少 %d 个字符，UTF-8 长度不超过 %d 字节",
                    PasswordConstants.MIN_LENGTH, PasswordConstants.MAX_BYTES));
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BusinessException("新密码不能与原密码相同");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        if (!userMapper.updatePassword(userId, user.getPassword(), encodedPassword)) {
            throw new BusinessException("账号状态或密码已变更，请重新登录");
        }
        // 单条更新已提交，再清理 Redis 中的所有设备会话。
        StpUtil.logout(userId);
    }
}
