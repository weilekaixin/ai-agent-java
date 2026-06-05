package com.ai.modules.tracker.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.tracker.domain.entity.Goal;
import com.ai.modules.tracker.mapper.GoalMapper;
import com.ai.modules.tracker.model.query.GoalListQuery;
import com.ai.modules.tracker.model.query.GoalQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 目标管理
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class GoalService extends ServiceImpl<GoalMapper, Goal> {

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<Goal> listPage(Long userId, GoalListQuery query) {
        var wrapper = this.buildWrapper(userId, query).orderByDesc(Goal::getCreateTime);
        return this.page(query.build(), wrapper);
    }

    /**
     * 新增或更新
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addOrUpdate(Long userId, GoalQuery query) {
        Goal data;
        if (ObjectUtil.isNotNull(query.getId())) {
            data = baseMapper.selectById(query.getId());
            if (ObjectUtil.isNull(data)) {
                return R.fail("获取目标信息失败，请刷新后重试！");
            }
            if (!data.getUserId().equals(userId)) {
                return R.fail("您没有权限操作该目标数据！");
            }
        } else {
            data = new Goal();
            data.setUserId(userId);
            data.setStatus("in_progress");
        }
        this.copyFields(data, query);
        this.saveOrUpdate(data);
        return R.ok();
    }

    /**
     * 变更目标状态（达成/放弃）
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> changeStatus(Long id, Long userId, String status) {
        Goal data = baseMapper.selectById(id);
        if (ObjectUtil.isNull(data)) {
            return R.fail("目标不存在或已被删除，请刷新后重试！");
        }
        if (!data.getUserId().equals(userId)) {
            return R.fail("您没有权限操作该目标数据！");
        }
        data.setStatus(status);
        baseMapper.updateById(data);
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
        this.removeByIds(idList);
        return R.ok();
    }

    /**
     * 构建查询条件
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private LambdaQueryWrapper<Goal> buildWrapper(Long userId, GoalListQuery query) {
        var wrapper = Wrappers.lambdaQuery(Goal.class);
        wrapper.eq(Goal::getUserId, userId)
                .eq(StrUtil.isNotBlank(query.getGoalType()), Goal::getGoalType, query.getGoalType())
                .eq(StrUtil.isNotBlank(query.getStatus()), Goal::getStatus, query.getStatus())
                .in(CollUtil.isNotEmpty(query.getIdList()), Goal::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(Goal entity, GoalQuery query) {
        entity.setGoalType(query.getGoalType());
        entity.setTargetValue(query.getTargetValue());
        entity.setCurrentValue(query.getCurrentValue());
        entity.setDeadline(query.getDeadline());
        entity.setRemark(query.getRemark());
    }
}
