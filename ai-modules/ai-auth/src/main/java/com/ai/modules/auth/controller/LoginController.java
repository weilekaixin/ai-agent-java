package com.ai.modules.auth.controller;

import com.ai.common.core.domain.R;
import com.ai.modules.auth.domain.dto.ChangePasswordDTO;
import com.ai.modules.auth.domain.dto.LoginDTO;
import com.ai.modules.auth.domain.vo.LoginVO;
import com.ai.modules.auth.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 *
 * @author root
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {
    private final LoginService loginService;

    /**
     * 用户名密码登录
     */
    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO query) {
        return R.ok(loginService.login(query));
    }

    /**
     * 修改本人密码，成功后需要重新登录
     */
    @PostMapping("/password")
    public R<Void> changePassword(@Valid @RequestBody ChangePasswordDTO query) {
        loginService.changePassword(query);
        return R.ok();
    }
}
