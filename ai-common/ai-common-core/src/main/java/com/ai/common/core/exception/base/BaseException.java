package com.ai.common.core.exception.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.ai.common.core.utils.MessageUtils;
import com.ai.common.core.utils.StringUtils;

import java.io.Serial;
import java.util.List;

/**
 * 基础异常
 *
 * @author root 2026-05-16 16:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException {
    /**
     * 所属模块
     */
    private String module;
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误码对应的参数
     */
    private List<?> args;
    /**
     * 错误消息
     */
    private String defaultMessage;
    public BaseException(String module, String code, List<?> args) {
        this(module, code, args, null);
    }
    public BaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }
    public BaseException(String code, List<?> args) {
        this(null, code, args, null);
    }
    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }
    @Override
    public String getMessage() {
        String message = null;
        if (!StringUtils.isEmpty(code)) {
            message = MessageUtils.message(code, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }
    @Serial
    private static final long serialVersionUID = 1L;
}
