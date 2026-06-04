package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Token 用量控制器 — 代理至 Python AI Agent 服务
 */
@Slf4j
@RestController
@RequestMapping("/usage")
@RequiredArgsConstructor
public class UsageController {

    private final AgentClient agentClient;

    /** 分页查询会话的 Token 用量明细。 */
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<String> getSessionUsage(
            @PathVariable String sessionId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(agentClient.getSessionUsage(sessionId, page, size));
    }

    /** 汇总会话的 Token 用量（请求次数、输入/输出总计）。 */
    @GetMapping("/sessions/{sessionId}/summary")
    public ResponseEntity<String> getSessionUsageSummary(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.getSessionUsageSummary(sessionId));
    }
}
