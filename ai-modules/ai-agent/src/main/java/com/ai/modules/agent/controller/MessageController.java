package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import com.ai.agent.model.PinCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息操作控制器 — 代理至 Python AI Agent 服务
 */
@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final AgentClient agentClient;

    /** 置顶/收藏一条消息，可附加备注。幂等。 */
    @PostMapping("/{messageId}/pin")
    public ResponseEntity<String> pinMessage(
            @PathVariable Long messageId,
            @Valid @RequestBody(required = false) PinCreate body) {
        return ResponseEntity.ok(agentClient.pinMessage(messageId, body != null ? body : new PinCreate()));
    }

    /** 取消置顶/收藏。 */
    @DeleteMapping("/{messageId}/pin")
    public ResponseEntity<String> unpinMessage(@PathVariable Long messageId) {
        return ResponseEntity.ok(agentClient.unpinMessage(messageId));
    }

    /** 列出某会话下所有已置顶的消息。 */
    @GetMapping("/sessions/{sessionId}/pinned")
    public ResponseEntity<String> getPinnedMessages(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.getPinnedMessages(sessionId));
    }
}
