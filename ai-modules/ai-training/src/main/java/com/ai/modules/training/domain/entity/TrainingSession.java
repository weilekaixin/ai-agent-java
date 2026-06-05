package com.ai.modules.training.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 训练会话（一次完整训练）
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("training_session")
public class TrainingSession extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    /** 训练标题 */
    private String title;

    /** 训练日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate trainDate;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /** 训练时长（分钟） */
    private Integer durationMin;

    /** 本次消耗热量（kcal，完成后由 set 聚合写入） */
    private BigDecimal caloriesBurned;

    /** 状态（in_progress/completed） */
    private String status;

    private String remark;

    @TableLogic
    private Integer delFlag;
}
