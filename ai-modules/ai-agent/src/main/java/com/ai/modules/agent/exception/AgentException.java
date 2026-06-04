package com.ai.modules.agent.exception;

import lombok.Getter;

/**
 * 业务异常：附带 HTTP-style 错误码，便于在 GlobalExceptionHandler 中统一映射成标准 HTTP 响应。
 */
@Getter
public class AgentException extends RuntimeException {

    private final int code;

    public AgentException(String message) {
        super(message);
        this.code = 500;
    }

    public AgentException(int code, String message) {
        super(message);
        this.code = code;
    }

    /** Convenience factory: 404 Not Found */
    public static AgentException notFound(String resource) {
        return new AgentException(404, resource + " 不存在");
    }

    /** Convenience factory: 400 Bad Request */
    public static AgentException badRequest(String message) {
        return new AgentException(400, message);
    }
}
