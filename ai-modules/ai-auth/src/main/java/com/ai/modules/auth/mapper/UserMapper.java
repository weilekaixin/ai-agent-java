package com.ai.modules.auth.mapper;

import com.ai.modules.auth.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

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
}
