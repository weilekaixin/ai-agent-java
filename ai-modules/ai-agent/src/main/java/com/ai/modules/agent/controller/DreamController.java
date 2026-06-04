package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AutoDream 手动触发接口
 *
 * @author root 2026-06-04
 */
@RestController
@RequestMapping("/dream")
@RequiredArgsConstructor
public class DreamController {

    private final AgentClient agentClient;

    /** 手动触发夠间记忆整合任务（不必等待定时，运维可即刻调用） */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> triggerDream() {
        return ResponseEntity.ok(agentClient.triggerDream());
    }
}
