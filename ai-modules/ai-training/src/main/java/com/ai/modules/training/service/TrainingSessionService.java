package com.ai.modules.training.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ai.common.core.domain.R;
import com.ai.common.rocketmq.support.RocketSender;
import com.ai.modules.message.constant.TrainingMqConstant;
import com.ai.modules.message.event.SessionCompletedEvent;
import com.ai.modules.training.domain.entity.Exercise;
import com.ai.modules.training.domain.entity.TrainingSet;
import com.ai.modules.training.domain.entity.TrainingSession;
import com.ai.modules.training.mapper.ExerciseMapper;
import com.ai.modules.training.mapper.TrainingSessionMapper;
import com.ai.modules.training.mapper.TrainingSetMapper;
import com.ai.modules.training.model.query.TrainingSessionListQuery;
import com.ai.modules.training.model.query.TrainingSessionQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 训练会话
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class TrainingSessionService extends ServiceImpl<TrainingSessionMapper, TrainingSession> {

    /** 每组动作按 1.5 分钟估算（含组间休息），用于热量聚合 */
    private static final BigDecimal SET_DURATION_MIN = BigDecimal.valueOf(1.5);

    @Resource
    private TrainingSetMapper trainingSetMapper;
    @Resource
    private ExerciseMapper exerciseMapper;
    @Resource
    private RocketSender rocketSender;

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<TrainingSession> listPage(Long userId, TrainingSessionListQuery query) {
        var wrapper = this.buildWrapper(userId, query).orderByDesc(TrainingSession::getTrainDate);
        return this.page(query.build(), wrapper);
    }

    /**
     * 新增或更新
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addOrUpdate(Long userId, TrainingSessionQuery query) {
        TrainingSession data;
        if (ObjectUtil.isNotNull(query.getId())) {
            data = baseMapper.selectById(query.getId());
            if (ObjectUtil.isNull(data)) {
                return R.fail("获取训练会话失败，请刷新后重试！");
            }
        } else {
            data = new TrainingSession();
            data.setUserId(userId);
            data.setStatus("in_progress");
            data.setStartTime(new Date());
        }
        this.copyFields(data, query);
        this.saveOrUpdate(data);
        return R.ok(data);
    }

    /**
     * 完成训练会话：统计热量、计算时长、更新状态、发 MQ 事件
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public void complete(Long sessionId) {
        TrainingSession session = baseMapper.selectById(sessionId);
        if (ObjectUtil.isNull(session) || "completed".equals(session.getStatus())) {
            return;
        }
        BigDecimal totalCalories = this.calcCalories(sessionId);
        Date endTime = new Date();
        int durationMin = 0;
        if (ObjectUtil.isNotNull(session.getStartTime())) {
            durationMin = (int) ((endTime.getTime() - session.getStartTime().getTime()) / 60_000);
        }
        session.setStatus("completed");
        session.setEndTime(endTime);
        session.setDurationMin(durationMin);
        session.setCaloriesBurned(totalCalories);
        baseMapper.updateById(session);

        // 发 MQ 事件通知 ai-tracker
        var event = new SessionCompletedEvent(sessionId, session.getUserId(),
                session.getTrainDate(), totalCalories, durationMin);
        rocketSender.syncSend(TrainingMqConstant.TOPIC_TRAINING_EVENT,
                TrainingMqConstant.TAG_SESSION_COMPLETED, event);
        log.info("训练会话完成，sessionId={}, calories={}", sessionId, totalCalories);
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
    private LambdaQueryWrapper<TrainingSession> buildWrapper(Long userId, TrainingSessionListQuery query) {
        var wrapper = Wrappers.lambdaQuery(TrainingSession.class);
        wrapper.eq(TrainingSession::getUserId, userId)
                .ge(ObjectUtil.isNotNull(query.getStartDate()), TrainingSession::getTrainDate, query.getStartDate())
                .le(ObjectUtil.isNotNull(query.getEndDate()), TrainingSession::getTrainDate, query.getEndDate())
                .eq(ObjectUtil.isNotNull(query.getStatus()), TrainingSession::getStatus, query.getStatus())
                .in(CollUtil.isNotEmpty(query.getIdList()), TrainingSession::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(TrainingSession entity, TrainingSessionQuery query) {
        entity.setTrainDate(query.getTrainDate());
        if (ObjectUtil.isNotNull(query.getStatus())) {
            entity.setStatus(query.getStatus());
        }
        entity.setRemark(query.getNotes());
    }

    /**
     * 聚合会话消耗热量：按已完成组数 × 动作每分钟消耗 × 单组估算时长 计算
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private BigDecimal calcCalories(Long sessionId) {
        List<TrainingSet> sets = trainingSetMapper.selectList(
                Wrappers.lambdaQuery(TrainingSet.class)
                        .eq(TrainingSet::getSessionId, sessionId)
                        .eq(TrainingSet::getDone, 1));
        if (CollUtil.isEmpty(sets)) {
            return BigDecimal.ZERO;
        }
        List<Long> exerciseIds = sets.stream().map(TrainingSet::getExerciseId).distinct().toList();
        Map<Long, Exercise> exerciseMap = exerciseMapper.selectBatchIds(exerciseIds).stream()
                .collect(Collectors.toMap(Exercise::getId, e -> e));
        BigDecimal total = BigDecimal.ZERO;
        for (TrainingSet set : sets) {
            Exercise exercise = exerciseMap.get(set.getExerciseId());
            if (ObjectUtil.isNull(exercise) || ObjectUtil.isNull(exercise.getCaloriesPerMin())) {
                continue;
            }
            total = total.add(BigDecimal.valueOf(exercise.getCaloriesPerMin()).multiply(SET_DURATION_MIN));
        }
        return total;
    }
}
