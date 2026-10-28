package com.ai.modules.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.exception.BusinessException;
import com.ai.modules.auth.domain.dto.LoginDTO;
import com.ai.modules.auth.domain.vo.LoginVO;
import com.ai.modules.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
