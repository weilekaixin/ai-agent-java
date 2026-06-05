package com.ai.modules.training.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.training.domain.entity.TrainingPlan;
import com.ai.modules.training.model.query.TrainingPlanListQuery;
import com.ai.modules.training.model.query.TrainingPlanQuery;
import com.ai.modules.training.service.TrainingPlanService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 训练计划
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 训练管理/训练计划
 */
@RestController
@RequestMapping("/plan")
public class TrainingPlanController {

    private static final String MODEL_NAME = "训练计划";

    @Resource
    private TrainingPlanService trainingPlanService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    public R<Page<TrainingPlan>> list(@Valid TrainingPlanListQuery query) {
        return R.ok(trainingPlanService.listPage(LoginHelper.getUserId(), query));
    }

    /**
     * 详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/get")
    public R<TrainingPlan> get(@Valid TrainingPlanListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return R.ok(trainingPlanService.getById(query.getId()));
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    public R<?> add(@Valid @RequestBody TrainingPlanQuery query) {
        return trainingPlanService.addOrUpdate(LoginHelper.getUserId(), query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody TrainingPlanQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return trainingPlanService.addOrUpdate(LoginHelper.getUserId(), query);
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody TrainingPlanListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return trainingPlanService.delBatch(query.getIdList());
    }
}
