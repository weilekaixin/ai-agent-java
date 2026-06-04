package com.ai.modules.agent.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Controller 方法请求日志切面。
 *
 * <p>记录每个 Controller 方法的执行耗时，并在异常时输出错误信息。
 * SSE 接口仅记录 SseEmitter 创建时间（非流式总耗时）。
 */
@Slf4j
@Aspect
@Component
public class RequestLoggingAspect {

    @Pointcut("execution(* com.ai.modules.agent.controller.*.*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logRequest(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String method = pjp.getSignature().toShortString();
        try {
            Object result = pjp.proceed();
            log.info("[CTRL] {} 完成 {}ms", method, System.currentTimeMillis() - start);
            return result;
        } catch (Throwable t) {
            log.error("[CTRL] {} 失败 {}ms: {}", method, System.currentTimeMillis() - start, t.getMessage());
            throw t;
        }
    }
}
