package com.ai.agent;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 请求拦截器：注入 X-Request-Id 并记录请求耐时
 *
 * @author root 2026-05-16 16:04
 */
@Slf4j
@Component
public class AgentAuthInterceptor implements Interceptor<Object> {

    private static final String HEADER_REQUEST_ID = "X-Request-Id";
    private static final String ATTR_START = "startTime";

    @Override
    public boolean beforeExecute(ForestRequest request) {
        // 链路追踪 ID，方便日志关联
        String requestId = UUID.randomUUID().toString().replace("-", "");
        request.addHeader(HEADER_REQUEST_ID, requestId);
        request.addAttribute(ATTR_START, System.currentTimeMillis());
        log.debug("[Agent] → {} {} rid={}", request.getType(), request.getUrl(), requestId);
        return true;
    }

    @Override
    public void onSuccess(Object data, ForestRequest request, ForestResponse response) {
        log.info("[Agent] ← {} {} status={} elapsed={}ms",
            request.getType(), request.getUrl(),
            response.getStatusCode(), elapsed(request));
    }

    @Override
    public void onError(ForestRuntimeException ex, ForestRequest request, ForestResponse response) {
        int status = response != null ? response.getStatusCode() : -1;
        log.error("[Agent] ✘ {} {} status={} elapsed={}ms msg={}",
            request.getType(), request.getUrl(),
            status, elapsed(request), ex.getMessage());
    }

    private long elapsed(ForestRequest request) {
        Object start = request.getAttribute(ATTR_START);
        return start instanceof Long ? System.currentTimeMillis() - (Long) start : -1L;
    }
}
