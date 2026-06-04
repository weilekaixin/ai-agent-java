package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import com.ai.agent.model.TagCreate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会话标签控制器 — 代理至 Python AI Agent 服务
 */
@Slf4j
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final AgentClient agentClient;

    /** 为会话添加标签（幂等）。 */
    @PostMapping("/sessions/{sessionId}")
    public ResponseEntity<String> addTag(
            @PathVariable String sessionId,
            @Valid @RequestBody TagCreate body) {
        return ResponseEntity.ok(agentClient.addSessionTag(sessionId, body));
    }

    /** 移除会话标签。 */
    @DeleteMapping("/sessions/{sessionId}/{tag}")
    public ResponseEntity<String> removeTag(
            @PathVariable String sessionId,
            @PathVariable String tag) {
        return ResponseEntity.ok(agentClient.removeSessionTag(sessionId, tag));
    }

    /** 查询会话的所有标签。 */
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<String> getSessionTags(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.getSessionTags(sessionId));
    }

    /** 列出全局所有标签及使用次数。 */
    @GetMapping
    public ResponseEntity<String> listAllTags() {
        return ResponseEntity.ok(agentClient.listAllTags());
    }

    /** 按标签分页查询会话列表。 */
    @GetMapping("/{tag}/sessions")
    public ResponseEntity<String> getSessionsByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(agentClient.getSessionsByTag(tag, page, size));
    }
}
