package com.ai.gateway.handler;

import com.ai.gateway.utils.WebFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 服务降级 / 熔断 Fallback 处理
 *
 * @author root 2026-05-17
 */
@Slf4j
@RestController
public class FallbackController {

    /**
     * ai-agent 服务降级处理
     */
    @RequestMapping("/fallback/ai-agent")
    public Mono<Void> aiAgentFallback(ServerWebExchange exchange) {
        // 获取原始异常（CircuitBreaker 会封装为相关异常）
        Throwable ex = exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);
        if (ex != null) {
            log.error("[服务降级] ai-agent 调用失败: {}", ex.getMessage());
        } else {
            log.error("[服务降级] ai-agent 不可用（无异常详情）");
        }
        return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), "服务繁忙，请稍后重试");
    }

}
