package com.ai.modules.agent.service;

import com.ai.agent.AgentClient;
import com.ai.agent.model.ChatQuery;
import com.ai.modules.agent.config.AgentProperties;
import com.ai.modules.agent.constant.AgentConstant;
import com.dtflys.forest.http.ForestSSE;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 聊天服务
 *
 * @author root
 */
@Slf4j
@Service
public class ChatService {
    @Resource
    private AgentClient agentClient;
    @Resource
    private AgentProperties agentProperties;

    /**
     * 调用智能体
     *
     * @author root 2026-05-16 16:04
     */
    public SseEmitter chat(ChatQuery query) {
        SseEmitter emitter = new SseEmitter(agentProperties.getTimeout());
        ForestSSE chat = agentClient.chat(query);
        chat.addOnData((event, name, value) -> {
                try {
                    var data = SseEmitter.event()
                        .name(AgentConstant.MESSAGE)
                        .data(value);
                    emitter.send(data);
                } catch (Exception e) {
                    log.error("SSE 发送失败", e);
                    emitter.completeWithError(e);
                }
            })
            .setOnClose(event -> {
                log.info("SSE 流关闭");
                emitter.complete();
            })
            .asyncListen();
        return emitter;
    }

}
