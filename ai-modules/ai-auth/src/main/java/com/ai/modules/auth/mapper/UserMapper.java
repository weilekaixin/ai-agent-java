package com.ai.modules.auth.mapper;

import com.ai.modules.auth.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Date;

/**
 * 用户数据访问
 *
 * @author root
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名查询登录账号，包含密码摘要
     */
    default User selectByUsername(String username) {
        var wrapper = Wrappers.lambdaQuery(User.class)
            .eq(User::getUsername, username);
        return this.selectOne(wrapper);
    }

    /**
     * 修改当前用户密码，原摘要和启用状态必须保持一致
     */
    default boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        var wrapper = Wrappers.lambdaUpdate(User.class)
                .eq(User::getUserId, userId)
                .eq(User::getPassword, oldPassword)
                .eq(User::getEnabled, true)
                .set(User::getPassword, newPassword)
                .set(User::getMustChangePassword, false)
                .set(User::getUpdateBy, userId)
                .set(User::getUpdateTime, new Date());
        return this.update(null, wrapper) > 0;
    }
}
