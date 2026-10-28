package com.ai.modules.auth.controller;

import com.ai.common.core.domain.R;
import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.modules.auth.domain.vo.UserVO;
import com.ai.modules.auth.service.LoginService;
import com.ai.modules.auth.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员用户管理接口
 *
 * @author root
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/admin/users")
public class UserController {
    private final LoginService loginService;
    private final UserService userService;

    /**
     * 管理员分页查询用户
     */
    @GetMapping("/list")
    public R<IPage<UserVO>> listPage(PageQuery query) {
        loginService.checkAdmin();
        return R.ok(userService.listPage(query));
    }
}
