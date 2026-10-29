package com.ai.modules.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.exception.BusinessException;
import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.modules.auth.constant.PasswordConstants;
import com.ai.modules.auth.domain.User;
import com.ai.modules.auth.domain.query.ResetPasswordQuery;
import com.ai.modules.auth.domain.query.UpdateUsernameQuery;
import com.ai.modules.auth.domain.query.UserQuery;
import com.ai.modules.auth.domain.query.UserStatusQuery;
import com.ai.modules.auth.domain.vo.UserVO;
import com.ai.modules.auth.enums.Permission;
import com.ai.modules.auth.mapper.UserMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 用户管理业务
 *
 * @author root
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    /**
     * 创建普通用户，初始密码只保存 BCrypt 摘要
     */
    public UserVO create(UserQuery query) {
        String username = StrUtil.trim(query.getUsername());
        if (StrUtil.isBlank(username) || username.length() > UserQuery.USERNAME_MAX_LENGTH) {
            throw new BusinessException("用户名不能为空或超出长度限制", HttpStatus.BAD_REQUEST.value());
        }
        String password = query.getPassword();
        if (StrUtil.isBlank(password) || password.length() < PasswordConstants.MIN_LENGTH
                || password.getBytes(StandardCharsets.UTF_8).length > PasswordConstants.MAX_BYTES) {
            throw new BusinessException(String.format("初始密码至少 %d 个字符，UTF-8 长度不超过 %d 字节",
                    PasswordConstants.MIN_LENGTH, PasswordConstants.MAX_BYTES), HttpStatus.BAD_REQUEST.value());
        }
        var wrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getUsername, username);
        if (userMapper.exists(wrapper)) {
            throw new BusinessException("用户名已存在", HttpStatus.CONFLICT.value());
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Permission.USER);
        user.setEnabled(true);
        user.setMustChangePassword(true);
        user.setCreateBy(StpUtil.getLoginIdAsLong());
        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException exception) {
            // 并发新增仍由数据库唯一约束兜底。
            throw new BusinessException("用户名已存在", HttpStatus.CONFLICT.value());
        }
        return this.toUserVO(user);
    }

    /**
     * 修改用户启用状态，禁用后退出该用户的所有设备
     */
    public void updateStatus(Long userId, UserStatusQuery query) {
        var user = userMapper.selectById(userId);
        if (ObjUtil.isNull(user)) {
            throw new BusinessException("用户不存在", HttpStatus.NOT_FOUND.value());
        }
        if (Boolean.FALSE.equals(query.getEnabled()) && user.getRole() == Permission.ADMIN) {
            throw new BusinessException("不允许禁用管理员", HttpStatus.FORBIDDEN.value());
        }
        User update = new User();
        update.setUserId(userId);
        update.setEnabled(query.getEnabled());
        update.setUpdateBy(StpUtil.getLoginIdAsLong());
        if (userMapper.updateById(update) == 0) {
            throw new BusinessException("用户状态更新失败，请刷新后重试", HttpStatus.CONFLICT.value());
        }
        // 数据库更新已提交；禁用时同时撤销 Redis 中的全部会话。
        if (Boolean.FALSE.equals(query.getEnabled())) {
            StpUtil.logout(userId);
        }
    }

    /**
     * 重置初始密码并退出所有设备，用户下次登录必须修改密码
     */
    public void resetPassword(Long userId, ResetPasswordQuery query) {
        String password = query.getPassword();
        if (StrUtil.isBlank(password) || password.length() < PasswordConstants.MIN_LENGTH
                || password.getBytes(StandardCharsets.UTF_8).length > PasswordConstants.MAX_BYTES) {
            throw new BusinessException(String.format("初始密码至少 %d 个字符，UTF-8 长度不超过 %d 字节",
                    PasswordConstants.MIN_LENGTH, PasswordConstants.MAX_BYTES), HttpStatus.BAD_REQUEST.value());
        }
        if (ObjUtil.isNull(userMapper.selectById(userId))) {
            throw new BusinessException("用户不存在", HttpStatus.NOT_FOUND.value());
        }
        User update = new User();
        update.setUserId(userId);
        update.setPassword(passwordEncoder.encode(password));
        update.setMustChangePassword(true);
        update.setUpdateBy(StpUtil.getLoginIdAsLong());
        if (userMapper.updateById(update) == 0) {
            throw new BusinessException("密码重置失败，请刷新后重试", HttpStatus.CONFLICT.value());
        }
        // 单条更新已提交，再撤销全部会话，避免数据库回滚后旧密码仍可使用。
        StpUtil.logout(userId);
    }

    /**
     * 修改用户名，保留用户标识及现有会话
     */
    public void updateUsername(Long userId, UpdateUsernameQuery query) {
        String username = StrUtil.trim(query.getUsername());
        if (StrUtil.isBlank(username) || username.length() > UserQuery.USERNAME_MAX_LENGTH) {
            throw new BusinessException("用户名不能为空或超出长度限制", HttpStatus.BAD_REQUEST.value());
        }
        if (ObjUtil.isNull(userMapper.selectById(userId))) {
            throw new BusinessException("用户不存在", HttpStatus.NOT_FOUND.value());
        }
        var wrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getUsername, username)
                .ne(User::getUserId, userId);
        if (userMapper.exists(wrapper)) {
            throw new BusinessException("用户名已存在", HttpStatus.CONFLICT.value());
        }
        User update = new User();
        update.setUserId(userId);
        update.setUsername(username);
        update.setUpdateBy(StpUtil.getLoginIdAsLong());
        try {
            if (userMapper.updateById(update) == 0) {
                throw new BusinessException("用户名更新失败，请刷新后重试", HttpStatus.CONFLICT.value());
            }
        } catch (DuplicateKeyException exception) {
            // 并发修改仍由数据库唯一约束兜底。
            throw new BusinessException("用户名已存在", HttpStatus.CONFLICT.value());
        }
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
