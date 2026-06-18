package com.ai.modules.training.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.training.domain.entity.TrainingPlan;
import com.ai.modules.training.mapper.TrainingPlanMapper;
import com.ai.modules.training.model.query.TrainingPlanListQuery;
import com.ai.modules.training.model.query.TrainingPlanQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 训练计划
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class TrainingPlanService extends ServiceImpl<TrainingPlanMapper, TrainingPlan> {

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<TrainingPlan> listPage(Long userId, TrainingPlanListQuery query) {
        var wrapper = this.buildWrapper(userId, query).orderByDesc(TrainingPlan::getId);
        return this.page(query.build(), wrapper);
    }

    /**
     * 新增或更新
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addOrUpdate(Long userId, TrainingPlanQuery query) {
        TrainingPlan data;
        if (ObjectUtil.isNotNull(query.getId())) {
            data = baseMapper.selectById(query.getId());
            if (ObjectUtil.isNull(data)) {
                return R.fail("获取训练计划失败，请刷新后重试！");
            }
        } else {
            data = new TrainingPlan();
            data.setUserId(userId);
        }
        this.copyFields(data, query);
        this.saveOrUpdate(data);
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
    private LambdaQueryWrapper<TrainingPlan> buildWrapper(Long userId, TrainingPlanListQuery query) {
        var wrapper = Wrappers.lambdaQuery(TrainingPlan.class);
        wrapper.eq(TrainingPlan::getUserId, userId)
                .like(StrUtil.isNotBlank(query.getName()), TrainingPlan::getName, query.getName())
                .eq(StrUtil.isNotBlank(query.getDifficulty()), TrainingPlan::getDifficulty, query.getDifficulty())
                .in(CollUtil.isNotEmpty(query.getIdList()), TrainingPlan::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(TrainingPlan entity, TrainingPlanQuery query) {
        entity.setName(query.getName());
        entity.setTargetMuscles(query.getTargetMuscles());
        entity.setEstimatedMin(query.getEstimatedMin());
        entity.setDifficulty(query.getDifficulty());
        entity.setRemark(query.getRemark());
    }
}
