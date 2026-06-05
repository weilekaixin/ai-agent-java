package com.ai.modules.training.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.training.domain.entity.Exercise;
import com.ai.modules.training.mapper.ExerciseMapper;
import com.ai.modules.training.model.query.ExerciseListQuery;
import com.ai.modules.training.model.query.ExerciseQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运动动作库
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class ExerciseService extends ServiceImpl<ExerciseMapper, Exercise> {

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<Exercise> listPage(ExerciseListQuery query) {
        var wrapper = this.buildWrapper(query).orderByAsc(Exercise::getId);
        return this.page(query.build(), wrapper);
    }

    /**
     * 新增或更新
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addOrUpdate(ExerciseQuery query) {
        Exercise data;
        if (ObjectUtil.isNotNull(query.getId())) {
            data = baseMapper.selectById(query.getId());
            if (ObjectUtil.isNull(data)) {
                return R.fail("获取动作信息失败，请刷新后重试！");
            }
        } else {
            data = new Exercise();
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
    private LambdaQueryWrapper<Exercise> buildWrapper(ExerciseListQuery query) {
        var wrapper = Wrappers.lambdaQuery(Exercise.class);
        wrapper.like(StrUtil.isNotBlank(query.getName()), Exercise::getName, query.getName())
                .eq(StrUtil.isNotBlank(query.getMuscleGroup()), Exercise::getMuscleGroup, query.getMuscleGroup())
                .eq(StrUtil.isNotBlank(query.getEquipmentType()), Exercise::getEquipmentType, query.getEquipmentType())
                .in(CollUtil.isNotEmpty(query.getIdList()), Exercise::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(Exercise entity, ExerciseQuery query) {
        entity.setName(query.getName());
        entity.setMuscleGroup(query.getMuscleGroup());
        entity.setEquipmentType(query.getEquipmentType());
        entity.setCaloriesPerMin(query.getCaloriesPerMin());
        entity.setDescription(query.getDescription());
    }
}
