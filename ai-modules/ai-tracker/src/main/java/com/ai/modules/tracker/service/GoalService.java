package com.ai.modules.tracker.service;

import com.ai.common.core.exception.BusinessException;
import com.ai.modules.tracker.domain.entity.Goal;
import com.ai.modules.tracker.mapper.GoalMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 目标业务
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalMapper goalMapper;

    public void create(Goal goal) {
        goal.setStatus("in_progress");
        goalMapper.insert(goal);
    }

    public List<Goal> listByUser(Long userId) {
        return goalMapper.selectList(
                new LambdaQueryWrapper<Goal>()
                        .eq(Goal::getUserId, userId)
                        .orderByDesc(Goal::getCreateTime));
    }

    /** 标记目标达成 */
    public void achieve(Long id, Long userId) {
        Goal goal = goalMapper.selectById(id);
        if (goal == null || !goal.getUserId().equals(userId)) {
            throw new BusinessException("目标不存在！");
        }
        Goal update = new Goal();
        update.setId(id);
        update.setStatus("achieved");
        goalMapper.updateById(update);
    }

    /** 放弃目标 */
    public void abandon(Long id, Long userId) {
        Goal goal = goalMapper.selectById(id);
        if (goal == null || !goal.getUserId().equals(userId)) {
            throw new BusinessException("目标不存在！");
        }
        Goal update = new Goal();
        update.setId(id);
        update.setStatus("abandoned");
        goalMapper.updateById(update);
    }
}
