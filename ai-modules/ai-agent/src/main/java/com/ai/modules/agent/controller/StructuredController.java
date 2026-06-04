package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 结构化输出接口（代理 Python FastAPI 的 /api/structured）
 *
 * <p>调用方传入 message 和 JSON Schema，Agent 按照 Schema 提取结构化数据。
 *
 * @author root 2026-06-04
 */
@RestController
@RequestMapping("/structured")
@RequiredArgsConstructor
public class StructuredController {

    private final AgentClient agentClient;

    /**
     * 结构化信息提取
     *
     * <p>请求体示例：
     * <pre>{@code
     * {
     *   "message": "张三于2024年01月01日购了一台苹果手机，花了7999元",
     *   "schema": {
     *     "type": "object",
     *     "properties": {
     *       "person": {"type": "string"},
     *       "date": {"type": "string"},
     *       "item": {"type": "string"},
     *       "amount": {"type": "number"}
     *     }
     *   }
     * }
     * }</pre>
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> structuredOutput(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(agentClient.structuredOutput(body));
    }
}
