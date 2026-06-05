package com.ai.modules.training.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.training.domain.entity.TrainingSession;
import com.ai.modules.training.model.query.TrainingSessionListQuery;
import com.ai.modules.training.model.query.TrainingSessionQuery;
import com.ai.modules.training.service.TrainingSessionService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 训练会话
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 训练管理/训练会话
 */
@RestController
@RequestMapping("/session")
public class TrainingSessionController {

    private static final String MODEL_NAME = "训练会话";

    @Resource
    private TrainingSessionService trainingSessionService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    @SentinelResource("session:list")
    public R<Page<TrainingSession>> list(@Valid TrainingSessionListQuery query) {
        return R.ok(trainingSessionService.listPage(LoginHelper.getUserId(), query));
    }

    /**
     * 详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/get")
    public R<TrainingSession> get(@Valid TrainingSessionListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return R.ok(trainingSessionService.getById(query.getId()));
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    public R<?> add(@Valid @RequestBody TrainingSessionQuery query) {
        return trainingSessionService.addOrUpdate(LoginHelper.getUserId(), query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody TrainingSessionQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return trainingSessionService.addOrUpdate(LoginHelper.getUserId(), query);
    }

    /**
     * 手动完成会话
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/complete")
    public R<?> complete(@RequestBody TrainingSessionListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        trainingSessionService.complete(query.getId());
        return R.ok();
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody TrainingSessionListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return trainingSessionService.delBatch(query.getIdList());
    }
}
