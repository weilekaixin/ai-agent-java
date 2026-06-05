package com.ai.modules.training.model.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 训练会话
 * 新增/编辑
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class TrainingSessionQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "训练日期不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate trainDate;

    /** 状态：pending / active / completed / cancelled */
    private String status;

    private Integer durationMin;

    private String notes;
}
