package com.ai.modules.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.system.domain.entity.User;
import com.ai.modules.system.domain.query.UserListQuery;
import com.ai.modules.system.domain.query.UserQuery;
import com.ai.modules.system.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author zhangyunlong
 * @folder 系统管理/用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public R<Page<User>> list(UserListQuery query) {
        return R.ok(userService.listPage(query));
    }

    @GetMapping("/get")
    public R<User> get(Long userId) {
        if (ObjUtil.isNull(userId)) {
            return R.fail("请选择用户！");
        }
        User user = userService.getUser(userId);
        if (ObjUtil.isNull(user)) {
            return R.fail("用户不存在！");
        }
        user.setPassword(null);
        user.setSalt(null);
        return R.ok(user);
    }

    @PostMapping("/add")
    public R<?> add(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:add");
        return userService.addUser(query);
    }

    @PostMapping("/update")
    public R<?> update(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:edit");
        return userService.updateUser(query);
    }

    @PostMapping("/del")
    public R<?> del(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:del");
        return userService.delUser(query.getId());
    }

    @PostMapping("/delBatch")
    public R<?> delBatch(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:del");
        return userService.delBatch(query.getIdList());
    }

    @PostMapping("/reset_pwd")
    public R<?> resetPwd(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:edit");
        return userService.resetPwd(query.getId(), query.getNewPassword());
    }

    @PostMapping("/update_pwd")
    public R<?> updatePwd(@RequestBody UserQuery query) {
        return userService.updatePwd(LoginHelper.getUserId(), query.getOldPassword(), query.getNewPassword());
    }

    @PostMapping("/enable_disable")
    public R<?> enableDisable(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:edit");
        return userService.enableDisable(query.getId(), query.getStatus());
    }
}
