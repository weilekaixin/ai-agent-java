package com.ai.modules.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.system.domain.vo.LoginQuery;
import com.ai.modules.system.domain.vo.LoginVO;
import com.ai.modules.system.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    /**
     * 用户名密码登录
     */
    @PostMapping("login")
    public R<LoginVO> login(@Valid @RequestBody LoginQuery query) {
        String token = loginService.login(query);
        LoginVO vo = new LoginVO(token, StpUtil.getTokenName(), null);
        return R.ok(vo);
    }
}
