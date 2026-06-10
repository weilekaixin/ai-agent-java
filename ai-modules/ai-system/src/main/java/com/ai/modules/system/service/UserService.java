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

    public User getUserByAccount(String account) {
        if (StrUtil.isBlankOrUndefined(account) || account.length() > UserQuery.ACCOUNT_LEN) {
            return null;
        }
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAccount, account).last("limit 1");
        return baseMapper.selectOne(wrapper);
    }

    public Page<User> listPage(UserListQuery query) {
        var wrapper = Wrappers.<User>lambdaQuery()
            .like(StrUtil.isNotBlank(query.getAccount()), User::getAccount, query.getAccount())
            .like(StrUtil.isNotBlank(query.getNickname()), User::getNickname, query.getNickname())
            .eq(query.getStatus() != null, User::getStatus, query.getStatus())
            .eq(StrUtil.isNotBlank(query.getPhone()), User::getPhone, query.getPhone())
            .orderByDesc(User::getCreateTime);
        return this.page(query.build(), wrapper);
    }

    public User getUser(Long userId) {
        return ObjectUtil.isNull(userId) ? null : baseMapper.selectById(userId);
    }

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
        if (StrUtil.isNotBlank(query.getNickname())) user.setNickname(query.getNickname());
        if (StrUtil.isNotBlank(query.getPhone())) user.setPhone(query.getPhone());
        if (StrUtil.isNotBlank(query.getEmail())) user.setEmail(query.getEmail());
        if (ObjectUtil.isNotNull(query.getUserRoleType())) user.setUserRoleType(query.getUserRoleType());
        if (ObjectUtil.isNotNull(query.getStatus())) user.setStatus(query.getStatus());
        this.updateById(user);
        return R.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public R<?> delUser(Long userId) {
        if (ObjectUtil.isNull(userId)) return R.fail("请选择正确的用户！");
        if (SystemConstants.SUPER_ADMIN_USER_ID.equals(userId)) return R.fail("超级管理员不能删除！");
        this.removeById(userId);
        return R.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public R<?> delBatch(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) return R.ok();
        if (idList.contains(SystemConstants.SUPER_ADMIN_USER_ID)) return R.fail("超级管理员不能删除！");
        this.removeByIds(idList);
        return R.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public R<?> resetPwd(Long userId, String newPassword) {
        if (ObjectUtil.isNull(userId)) return R.fail("请选择正确的用户！");
        if (StrUtil.isBlank(newPassword)) return R.fail("新密码不能为空！");
        if (SystemConstants.SUPER_ADMIN_USER_ID.equals(userId)) return R.fail("超级管理员密码请在数据库修改！");

        User user = baseMapper.selectById(userId);
        if (ObjectUtil.isNull(user)) return R.fail("用户不存在！");

        String salt = SsoUtil.generateSalt();
        user.setSalt(salt);
        user.setPassword(SsoUtil.encryptPassword(user.getAccount(), newPassword, salt));
        user.setInitPassword(1);
        user.setPwdUpdateDate(new Date());
        this.updateById(user);
        return R.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public R<?> updatePwd(Long userId, String oldPassword, String newPassword) {
        if (StrUtil.isBlank(oldPassword) || StrUtil.isBlank(newPassword)) {
            return R.fail("旧密码和新密码不能为空！");
        }
        User user = baseMapper.selectById(userId);
        if (ObjectUtil.isNull(user)) return R.fail("用户不存在！");
        if (!SsoUtil.encryptPassword(user.getAccount(), oldPassword, user.getSalt()).equals(user.getPassword())) {
            return R.fail("旧密码错误！");
        }
        String salt = SsoUtil.generateSalt();
        user.setSalt(salt);
        user.setPassword(SsoUtil.encryptPassword(user.getAccount(), newPassword, salt));
        user.setInitPassword(0);
        user.setPwdUpdateDate(new Date());
        this.updateById(user);
        return R.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public R<?> enableDisable(Long userId, Integer status) {
        if (ObjectUtil.isNull(userId)) return R.fail("请选择正确的用户！");
        if (!StatusEnum.NORMAL.getCode().equals(status) && !StatusEnum.DISABLED.getCode().equals(status)) {
            return R.fail("状态值不正确！");
        }
        if (SystemConstants.SUPER_ADMIN_USER_ID.equals(userId)) return R.fail("超级管理员不能禁用！");
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        this.updateById(user);
        return R.ok();
    }
}
