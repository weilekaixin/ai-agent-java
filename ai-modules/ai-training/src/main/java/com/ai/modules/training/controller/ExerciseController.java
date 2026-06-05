package com.ai.modules.training.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.training.domain.entity.Exercise;
import com.ai.modules.training.model.query.ExerciseListQuery;
import com.ai.modules.training.model.query.ExerciseQuery;
import com.ai.modules.training.service.ExerciseService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 运动动作库
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 训练管理/动作库
 */
@RestController
@RequestMapping("/exercise")
public class ExerciseController {

    private static final String MODEL_NAME = "运动动作";

    @Resource
    private ExerciseService exerciseService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    @SentinelResource("exercise:list")
    public R<Page<Exercise>> list(@Valid ExerciseListQuery query) {
        return R.ok(exerciseService.listPage(query));
    }

    /**
     * 详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/get")
    public R<Exercise> get(@Valid ExerciseListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return R.ok(exerciseService.getById(query.getId()));
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    public R<?> add(@Valid @RequestBody ExerciseQuery query) {
        return exerciseService.addOrUpdate(query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody ExerciseQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return exerciseService.addOrUpdate(query);
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody ExerciseListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return exerciseService.delBatch(query.getIdList());
    }
}
