package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理面板控制器 — 代理至 Python AI Agent 服务
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AgentClient agentClient;

    /** 全局统计面板：会话、消息、Token 用量、反馈、标签等聂合指标。 */
    @GetMapping("/stats")
    public ResponseEntity<String> getAdminStats() {
        return ResponseEntity.ok(agentClient.getAdminStats());
    }
}
