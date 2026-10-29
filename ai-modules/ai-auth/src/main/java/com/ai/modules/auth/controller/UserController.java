package com.ai.modules.auth.controller;

import com.ai.common.core.domain.R;
import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.modules.auth.domain.query.ResetPasswordQuery;
import com.ai.modules.auth.domain.query.UpdateUsernameQuery;
import com.ai.modules.auth.domain.query.UserQuery;
import com.ai.modules.auth.domain.query.UserStatusQuery;
import com.ai.modules.auth.domain.vo.UserVO;
import com.ai.modules.auth.service.LoginService;
import com.ai.modules.auth.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 管理员查询用户详情
     */
    @GetMapping("/{userId}")
    public R<UserVO> getInfo(@PathVariable Long userId) {
        loginService.checkAdmin();
        return R.ok(userService.getInfo(userId));
    }

    /**
     * 管理员新增普通用户
     */
    @PostMapping
    public R<UserVO> create(@Valid @RequestBody UserQuery query) {
        loginService.checkAdmin();
        return R.ok(userService.create(query));
    }

    /**
     * 管理员启用或禁用用户
     */
    @PutMapping("/{userId}/status")
    public R<Void> updateStatus(@PathVariable Long userId, @Valid @RequestBody UserStatusQuery query) {
        loginService.checkAdmin();
        userService.updateStatus(userId, query);
        return R.ok();
    }

    /**
     * 管理员重置用户密码
     */
    @PutMapping("/{userId}/password")
    public R<Void> resetPassword(@PathVariable Long userId, @Valid @RequestBody ResetPasswordQuery query) {
        loginService.checkAdmin();
        userService.resetPassword(userId, query);
        return R.ok();
    }

    /**
     * 管理员修改用户名
     */
    @PutMapping("/{userId}/username")
    public R<Void> updateUsername(@PathVariable Long userId, @Valid @RequestBody UpdateUsernameQuery query) {
        loginService.checkAdmin();
        userService.updateUsername(userId, query);
        return R.ok();
    }
}
