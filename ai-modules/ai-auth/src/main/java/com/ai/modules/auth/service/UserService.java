package com.ai.modules.auth.service;

import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.modules.auth.domain.User;
import com.ai.modules.auth.domain.vo.UserVO;
import com.ai.modules.auth.mapper.UserMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户管理业务
 *
 * @author root
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    /**
     * 分页查询用户，仅返回允许展示的字段
     */
    public IPage<UserVO> listPage(PageQuery query) {
        var wrapper = Wrappers.lambdaQuery(User.class)
                .select(User::getUserId, User::getUsername, User::getRole,
                        User::getEnabled, User::getMustChangePassword)
                .orderByDesc(User::getCreateTime)
                .orderByDesc(User::getUserId);
        var page = userMapper.selectPage(query.build(), wrapper);
        return page.convert(this::toUserVO);
    }

    private UserVO toUserVO(User user) {
        UserVO vo = new UserVO();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setRole(user.getRole());
        vo.setEnabled(user.getEnabled());
        vo.setMustChangePassword(user.getMustChangePassword());
        return vo;
    }
}
