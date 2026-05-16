package com.ai.modules.agent.controller;

import com.ai.agent.model.ChatQuery;
import com.ai.modules.agent.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 聊天接口
 *
 * @author root 2026-05-16 16:04
 */
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    /**
     * 聊天
     *
     * @author root 2026-05-16 16:04
     */
    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestBody ChatQuery query) {
        return chatService.chat(query);
    }
}
