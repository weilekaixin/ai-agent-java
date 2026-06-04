package com.ai.modules.agent.controller;

import com.ai.agent.model.MultiAgentQuery;
import com.ai.modules.agent.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 多智能体协作对话接口（代理 Python FastAPI 的 /api/multi-agent/chat）
 *
 * <p>请求自动路由到 researcher / coder / general 专家智能体。
 *
 * @author root 2026-06-04
 */
@RestController
@RequestMapping("/multi-agent")
@RequiredArgsConstructor
public class MultiAgentController {

    private final ChatService chatService;

    /**
     * 多智能体协作流式对话
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter multiAgentChat(@Valid @RequestBody MultiAgentQuery query) {
        return chatService.multiAgentChat(query);
    }
}
