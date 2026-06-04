package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 会话管理接口（代理 Python FastAPI 的 /api/sessions 系列）
 *
 * @author root 2026-06-04
 */
@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final AgentClient agentClient;

    /** 列出所有会话（按创建时间降序） */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listSessions() {
        return ResponseEntity.ok(agentClient.getSessions());
    }

    /** 查询某会话的全部消息 */
    @GetMapping(value = "/{sessionId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMessages(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.getSessionMessages(sessionId));
    }

    /** 导出会话完整记录为 JSON */
    @GetMapping(value = "/{sessionId}/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> exportSession(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.exportSession(sessionId));
    }

    /** 更新会话标题 */
    @PutMapping(value = "/{sessionId}/title", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSessionTitle(
            @PathVariable String sessionId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(agentClient.updateSessionTitle(sessionId, body));
    }

    /** 清空会话消息（保留会话，开始新对话） */
    @PostMapping(value = "/{sessionId}/clear", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> clearSession(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.clearSession(sessionId));
    }

    /** 删除会话及其全部消息 */
    @DeleteMapping(value = "/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSession(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.deleteSession(sessionId));
    }
}
