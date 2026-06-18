package com.ai.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.constant.SystemConstants;
import com.ai.common.core.domain.R;
import com.ai.common.core.enums.StatusEnum;
import com.ai.common.core.enums.UserRoleType;
import com.ai.common.satoken.utils.SsoUtil;
import com.ai.modules.system.domain.entity.User;
import com.ai.modules.system.domain.query.UserListQuery;
import com.ai.modules.system.domain.query.UserQuery;
import com.ai.modules.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户管理业务处理
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<SysUserMapper, User> {

    /**
     * 根据账号查询用户
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public User getUserByAccount(String account) {
        if (StrUtil.isBlankOrUndefined(account) || account.length() > UserQuery.ACCOUNT_LEN) {
            return null;
        }
        var wrapper = Wrappers.<User>lambdaQuery().eq(User::getAccount, account).last("limit 1");
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<User> listPage(UserListQuery query) {
        var wrapper = this.buildWrapper(query).orderByDesc(User::getCreateTime);
        return this.page(query.build(), wrapper);
    }

    /**
     * 查询用户详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public User getUser(Long userId) {
        return ObjectUtil.isNull(userId) ? null : baseMapper.selectById(userId);
    }

    /**
     * 新增用户
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addUser(UserQuery query) {
        if (ObjectUtil.isNotNull(getUserByAccount(query.getAccount()))) {
            return R.fail("用户名已存在！");
        }
        if (StrUtil.isBlank(query.getPassword())) {
            return R.fail("密码不能为空！");
        }
        User user = new User();
        user.setAccount(query.getAccount());
        user.setNickname(query.getNickname());
        user.setPhone(query.getPhone());
        user.setEmail(query.getEmail());
        user.setAvatar(query.getAvatar());
        user.setUserType(ObjectUtil.defaultIfNull(query.getUserType(), "sys_user"));
        user.setUserRoleType(ObjectUtil.defaultIfNull(query.getUserRoleType(), UserRoleType.GENERAL.getCode()));
        user.setStatus(ObjectUtil.defaultIfNull(query.getStatus(), StatusEnum.NORMAL.getCode()));
        user.setInitPassword(1);

        String salt = SsoUtil.generateSalt();
        user.setSalt(salt);
        user.setPassword(SsoUtil.encryptPassword(query.getAccount(), query.getPassword(), salt));

        this.save(user);
        return R.ok();
    }

    /**
     * 编辑用户
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateUser(UserQuery query) {
        if (ObjectUtil.isNull(query.getId())) {
            return R.fail("请选择正确的用户！");
        }
        User user = baseMapper.selectById(query.getId());
        if (ObjectUtil.isNull(user)) {
            return R.fail("用户不存在！");
        }
        if (StrUtil.isNotBlank(query.getAccount()) && !query.getAccount().equals(user.getAccount())) {
            User exist = getUserByAccount(query.getAccount());
            if (ObjectUtil.isNotNull(exist) && !exist.getId().equals(user.getId())) {
                return R.fail("用户名已存在！");
            }
            user.setAccount(query.getAccount());
        }
        this.copyFields(user, query);
        this.updateById(user);
        return R.ok();
    }

    /**
     * 删除单个用户
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> delUser(Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return R.fail("请选择正确的用户！");
        }
        if (SystemConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            return R.fail("超级管理员不能删除！");
        }
        this.removeById(userId);
        return R.ok();
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> delBatch(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return R.ok();
        }
        if (idList.contains(SystemConstants.SUPER_ADMIN_USER_ID)) {
            return R.fail("超级管理员不能删除！");
        }
        this.removeByIds(idList);
        return R.ok();
    }

    /**
     * 重置密码（管理员操作）
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> resetPwd(Long userId, String newPassword) {
        if (ObjectUtil.isNull(userId)) {
            return R.fail("请选择正确的用户！");
        }
        if (StrUtil.isBlank(newPassword)) {
            return R.fail("新密码不能为空！");
        }
        if (SystemConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            return R.fail("超级管理员密码请在数据库修改！");
        }
        User user = baseMapper.selectById(userId);
        if (ObjectUtil.isNull(user)) {
            return R.fail("用户不存在！");
        }
        this.updatePassword(user, newPassword, 1);
        return R.ok();
    }

    /**
     * 修改密码（用户本人操作，需校验旧密码）
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> updatePwd(Long userId, String oldPassword, String newPassword) {
        if (StrUtil.isBlank(oldPassword) || StrUtil.isBlank(newPassword)) {
            return R.fail("旧密码和新密码不能为空！");
        }
        User user = baseMapper.selectById(userId);
        if (ObjectUtil.isNull(user)) {
            return R.fail("用户不存在！");
        }
        if (!SsoUtil.encryptPassword(user.getAccount(), oldPassword, user.getSalt()).equals(user.getPassword())) {
            return R.fail("旧密码错误！");
        }
        this.updatePassword(user, newPassword, 0);
        return R.ok();
    }

    /**
     * 启用/禁用用户
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> enableDisable(Long userId, Integer status) {
        if (ObjectUtil.isNull(userId)) {
            return R.fail("请选择正确的用户！");
        }
        if (!StatusEnum.NORMAL.getCode().equals(status) && !StatusEnum.DISABLED.getCode().equals(status)) {
            return R.fail("状态值不正确！");
        }
        if (SystemConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            return R.fail("超级管理员不能禁用！");
        }
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        this.updateById(user);
        return R.ok();
    }

    /**
     * 构建查询条件
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private LambdaQueryWrapper<User> buildWrapper(UserListQuery query) {
        var wrapper = Wrappers.lambdaQuery(User.class);
        wrapper.like(StrUtil.isNotBlank(query.getAccount()), User::getAccount, query.getAccount())
                .like(StrUtil.isNotBlank(query.getNickname()), User::getNickname, query.getNickname())
                .eq(ObjectUtil.isNotNull(query.getStatus()), User::getStatus, query.getStatus())
                .eq(StrUtil.isNotBlank(query.getPhone()), User::getPhone, query.getPhone());
        return wrapper;
    }

    /**
     * 复制字段（编辑用户，仅覆盖非空字段）
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(User entity, UserQuery query) {
        if (StrUtil.isNotBlank(query.getNickname())) {
            entity.setNickname(query.getNickname());
        }
        if (StrUtil.isNotBlank(query.getPhone())) {
            entity.setPhone(query.getPhone());
        }
        if (StrUtil.isNotBlank(query.getEmail())) {
            entity.setEmail(query.getEmail());
        }
        if (ObjectUtil.isNotNull(query.getUserRoleType())) {
            entity.setUserRoleType(query.getUserRoleType());
        }
        if (ObjectUtil.isNotNull(query.getStatus())) {
            entity.setStatus(query.getStatus());
        }
    }

    /**
     * 更新密码及盐值
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void updatePassword(User user, String newPassword, int initPassword) {
        String salt = SsoUtil.generateSalt();
        user.setSalt(salt);
        user.setPassword(SsoUtil.encryptPassword(user.getAccount(), newPassword, salt));
        user.setInitPassword(initPassword);
        user.setPwdUpdateDate(new Date());
        this.updateById(user);
    }
}
