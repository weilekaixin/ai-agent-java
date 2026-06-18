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
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 系统管理/用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final String MODEL_NAME = "用户";

    @Resource
    private UserService userService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    public R<Page<User>> list(UserListQuery query) {
        return R.ok(userService.listPage(query));
    }

    /**
     * 详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/get")
    public R<User> get(Long userId) {
        if (ObjUtil.isNull(userId)) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        User user = userService.getUser(userId);
        if (ObjUtil.isNull(user)) {
            return R.fail(MODEL_NAME + "不存在！");
        }
        user.setPassword(null);
        user.setSalt(null);
        return R.ok(user);
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    public R<?> add(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:add");
        return userService.addUser(query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:edit");
        return userService.updateUser(query);
    }

    /**
     * 删除单个
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del")
    public R<?> del(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:del");
        return userService.delUser(query.getId());
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:del");
        return userService.delBatch(query.getIdList());
    }

    /**
     * 重置密码（管理员操作）
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/reset_pwd")
    public R<?> resetPwd(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:edit");
        return userService.resetPwd(query.getId(), query.getNewPassword());
    }

    /**
     * 修改密码（用户本人操作）
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update_pwd")
    public R<?> updatePwd(@RequestBody UserQuery query) {
        return userService.updatePwd(LoginHelper.getUserId(), query.getOldPassword(), query.getNewPassword());
    }

    /**
     * 启用/禁用
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/enable_disable")
    public R<?> enableDisable(@RequestBody UserQuery query) {
        StpUtil.checkPermission("system:user:edit");
        return userService.enableDisable(query.getId(), query.getStatus());
    }
}
