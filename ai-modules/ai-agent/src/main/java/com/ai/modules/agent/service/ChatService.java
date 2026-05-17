package com.ai.modules.agent.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.agent.AgentClient;
import com.ai.agent.model.ChatQuery;
import com.ai.agent.model.ResumeQuery;
import com.ai.modules.agent.config.AgentProperties;
import com.ai.modules.agent.constant.AgentConstant;
import com.ai.modules.agent.enums.StreamTag;
import com.dtflys.forest.http.ForestSSE;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 聊天服务
 *
 * @author root 2026-05-16 16:04
 */
@Slf4j
@Service
public class ChatService {
    @Resource
    private AgentClient agentClient;
    @Resource
    private AgentProperties agentProperties;

    /**
     * 恢复执行敏感操作
     *
     * @author root 2026-05-16 16:04
     */
    public SseEmitter chat(ChatQuery query) {
        return this.stream(StreamTag.CHAT, () -> agentClient.chat(query));
    }

    /**
     * 恢复执行敏感操作
     *
     * @author root 2026-05-16 16:04
     */
    public SseEmitter resume(ResumeQuery query) {
        return this.stream(StreamTag.RESUME, () -> agentClient.resume(query));
    }

    /**
     * 通用 SSE 流式转发：通过 Forest 调用 Python agent，将 SSE 事件转发到 SseEmitter
     *
     * @author root 2026-05-16 16:04
     */
    private SseEmitter stream(StreamTag tag, Supplier<ForestSSE> supplier) {
        SseEmitter emitter = new SseEmitter(agentProperties.getTimeout());

        CompletableFuture.runAsync(() -> {
            try {
                ForestSSE sse = supplier.get();
                log.info("[{}] Forest SSE 连接已建立", tag);

                sse.setOnMessage(event -> {
                    String data = event.data();
                    // 空 data 代表 LLM 发出的 \n token，转发为换行
                    if (ObjectUtil.isNull(data)) {
                        return;
                    }
                    if (StrUtil.isBlank(data)) {
                        data = "\n";
                    }
                    log.debug("[{}] Forest SSE data: {}", tag, data);
                    try {
                        var sseEvent = SseEmitter.event()
                            .name(AgentConstant.MESSAGE)
                            .data(data);
                        emitter.send(sseEvent);
                    } catch (IOException e) {
                        log.error("[{}] SseEmitter 发送失败", tag, e);
                        emitter.completeWithError(e);
                    }
                });

                sse.setOnClose(event -> {
                    log.info("[{}] Forest SSE 连接关闭", tag);
                    emitter.complete();
                });

                sse.listen();
            } catch (Exception e) {
                log.error("[{}] Forest SSE 异常", tag, e);
                try {
                    emitter.completeWithError(e);
                } catch (Exception ignored) {
                }
            }
        });

        return emitter;
    }
}
