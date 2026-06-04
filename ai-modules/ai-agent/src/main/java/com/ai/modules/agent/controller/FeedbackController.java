package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import com.ai.agent.model.FeedbackCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息反馈控制器 — 代理至 Python AI Agent 服务
 */
@Slf4j
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final AgentClient agentClient;

    /** 提交消息反馈（点赞/踩）。重复提交会更新已有反馈。 */
    @PostMapping("/messages/{messageId}")
    public ResponseEntity<String> submitFeedback(
            @PathVariable Long messageId,
            @Valid @RequestBody FeedbackCreate body) {
        return ResponseEntity.ok(agentClient.createFeedback(messageId, body));
    }

    /** 查询某条消息的反馈详情。 */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<String> getMessageFeedback(@PathVariable Long messageId) {
        return ResponseEntity.ok(agentClient.getMessageFeedback(messageId));
    }

    /** 查询某会话下所有消息的反馈列表。 */
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<String> getSessionFeedback(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.getSessionFeedback(sessionId));
    }

    /** 查询会话反馈统计（好评率、点赞数、踩数）。 */
    @GetMapping("/sessions/{sessionId}/stats")
    public ResponseEntity<String> getSessionFeedbackStats(@PathVariable String sessionId) {
        return ResponseEntity.ok(agentClient.getSessionFeedbackStats(sessionId));
    }
}
