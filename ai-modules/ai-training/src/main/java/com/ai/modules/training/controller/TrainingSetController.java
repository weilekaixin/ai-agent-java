package com.ai.modules.training.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.training.domain.entity.TrainingSet;
import com.ai.modules.training.model.query.TrainingSetListQuery;
import com.ai.modules.training.model.query.TrainingSetQuery;
import com.ai.modules.training.service.TrainingSetService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 训练组
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 训练管理/训练组
 */
@RestController
@RequestMapping("/training-set")
public class TrainingSetController {

    private static final String MODEL_NAME = "训练组";

    @Resource
    private TrainingSetService trainingSetService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    public R<Page<TrainingSet>> list(@Valid TrainingSetListQuery query) {
        return R.ok(trainingSetService.listPage(query));
    }

    /**
     * 详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/get")
    public R<TrainingSet> get(@Valid TrainingSetListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return R.ok(trainingSetService.getById(query.getId()));
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    public R<?> add(@Valid @RequestBody TrainingSetQuery query) {
        return trainingSetService.addOrUpdate(query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody TrainingSetQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return trainingSetService.addOrUpdate(query);
    }

    /**
     * 标记完成
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/mark_done")
    public R<?> markDone(@RequestBody TrainingSetListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return trainingSetService.markDone(query.getId());
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody TrainingSetListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return trainingSetService.delBatch(query.getIdList());
    }
}
