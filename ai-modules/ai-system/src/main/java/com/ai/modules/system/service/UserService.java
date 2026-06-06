package com.ai.modules.system.service;

import cn.hutool.core.util.StrUtil;
import com.ai.modules.system.domain.entity.User;
import com.ai.modules.system.domain.query.UserQuery;
import com.ai.modules.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户管理业务处理
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Service
public class UserService extends ServiceImpl<SysUserMapper, User> {
    /**
     * 获取用户信息
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public User getUserByAccount(String account) {
        if (StrUtil.isBlankOrUndefined(account) || account.length() > UserQuery.ACCOUNT_LEN) {
            return null;
        }
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAccount, account)
            .last("limit 1");
        return baseMapper.selectOne(wrapper);
    }
}
