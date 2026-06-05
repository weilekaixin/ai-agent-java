package com.ai.modules.training.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.training.domain.entity.TrainingSet;
import com.ai.modules.training.mapper.TrainingSetMapper;
import com.ai.modules.training.model.query.TrainingSetListQuery;
import com.ai.modules.training.model.query.TrainingSetQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 训练组
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class TrainingSetService extends ServiceImpl<TrainingSetMapper, TrainingSet> {

    @Lazy
    @Resource
    private TrainingSessionService trainingSessionService;

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<TrainingSet> listPage(TrainingSetListQuery query) {
        var wrapper = this.buildWrapper(query).orderByAsc(TrainingSet::getSetOrder);
        return this.page(query.build(), wrapper);
    }

    /**
     * 新增或更新
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addOrUpdate(TrainingSetQuery query) {
        TrainingSet data;
        if (ObjectUtil.isNotNull(query.getId())) {
            data = baseMapper.selectById(query.getId());
            if (ObjectUtil.isNull(data)) {
                return R.fail("获取训练组信息失败，请刷新后重试！");
            }
        } else {
            data = new TrainingSet();
        }
        this.copyFields(data, query);
        this.saveOrUpdate(data);
        return R.ok();
    }

    /**
     * 标记训练组完成，并检查会话是否全部完成
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> markDone(Long id) {
        TrainingSet data = baseMapper.selectById(id);
        if (ObjectUtil.isNull(data)) {
            return R.fail("训练组不存在！");
        }
        data.setDone(1);
        baseMapper.updateById(data);
        // 检查会话所有组是否全部完成，若是则自动完成会话
        int total = baseMapper.countTotalBySession(data.getSessionId());
        int done = baseMapper.countDoneBySession(data.getSessionId());
        if (total > 0 && total == done) {
            trainingSessionService.complete(data.getSessionId());
        }
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
    private LambdaQueryWrapper<TrainingSet> buildWrapper(TrainingSetListQuery query) {
        var wrapper = Wrappers.lambdaQuery(TrainingSet.class);
        wrapper.eq(ObjectUtil.isNotNull(query.getSessionId()), TrainingSet::getSessionId, query.getSessionId())
                .eq(ObjectUtil.isNotNull(query.getExerciseId()), TrainingSet::getExerciseId, query.getExerciseId())
                .eq(ObjectUtil.isNotNull(query.getDone()), TrainingSet::getDone, Boolean.TRUE.equals(query.getDone()) ? 1 : 0)
                .in(CollUtil.isNotEmpty(query.getIdList()), TrainingSet::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(TrainingSet entity, TrainingSetQuery query) {
        entity.setSessionId(query.getSessionId());
        entity.setExerciseId(query.getExerciseId());
        entity.setSetOrder(query.getSetOrder());
        entity.setReps(query.getReps());
        entity.setWeight(query.getWeightKg());
        entity.setDone(Boolean.TRUE.equals(query.getDone()) ? 1 : 0);
        entity.setRemark(query.getNotes());
    }
}
