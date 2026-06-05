package com.ai.modules.tracker.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.tracker.domain.entity.WeightLog;
import com.ai.modules.tracker.mapper.WeightLogMapper;
import com.ai.modules.tracker.model.query.WeightLogListQuery;
import com.ai.modules.tracker.model.query.WeightLogQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 体重记录
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class WeightLogService extends ServiceImpl<WeightLogMapper, WeightLog> {

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<WeightLog> listPage(Long userId, WeightLogListQuery query) {
        var wrapper = this.buildWrapper(userId, query).orderByDesc(WeightLog::getLogDate);
        return this.page(query.build(), wrapper);
    }

    /**
     * 新增或更新
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addOrUpdate(Long userId, WeightLogQuery query) {
        WeightLog data;
        if (ObjectUtil.isNotNull(query.getId())) {
            data = baseMapper.selectById(query.getId());
            if (ObjectUtil.isNull(data)) {
                return R.fail("获取体重记录失败，请刷新后重试！");
            }
        } else {
            data = new WeightLog();
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
    private LambdaQueryWrapper<WeightLog> buildWrapper(Long userId, WeightLogListQuery query) {
        var wrapper = Wrappers.lambdaQuery(WeightLog.class);
        wrapper.eq(WeightLog::getUserId, userId)
                .ge(ObjectUtil.isNotNull(query.getStartDate()), WeightLog::getLogDate, query.getStartDate())
                .le(ObjectUtil.isNotNull(query.getEndDate()), WeightLog::getLogDate, query.getEndDate())
                .in(CollUtil.isNotEmpty(query.getIdList()), WeightLog::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(WeightLog entity, WeightLogQuery query) {
        entity.setWeight(query.getWeight());
        entity.setBodyFat(query.getBodyFat());
        entity.setLogDate(query.getLogDate());
        entity.setRemark(query.getRemark());
    }
}
