package com.ai.modules.tracker.service;

import com.ai.modules.tracker.domain.entity.WeightLog;
import com.ai.modules.tracker.mapper.WeightLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 体重记录业务
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Service
@RequiredArgsConstructor
public class WeightLogService {

    private final WeightLogMapper weightLogMapper;

    public void log(WeightLog weightLog) {
        weightLogMapper.insert(weightLog);
    }

    /** 查最近 N 条记录（折线图用） */
    public List<WeightLog> recent(Long userId, int limit) {
        return weightLogMapper.selectList(
                new LambdaQueryWrapper<WeightLog>()
                        .eq(WeightLog::getUserId, userId)
                        .orderByDesc(WeightLog::getLogDate)
                        .last("LIMIT " + limit));
    }

    public IPage<WeightLog> page(Long userId, int pageNum, int pageSize) {
        return weightLogMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<WeightLog>()
                        .eq(WeightLog::getUserId, userId)
                        .orderByDesc(WeightLog::getLogDate));
    }
}
