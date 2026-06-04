package com.ai.modules.agent.health;

import com.ai.agent.AgentClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Python Agent 健康状态检查
 * 挂载到 Spring Boot Actuator: /actuator/health/agent
 *
 * @author root 2026-06-04
 */
@Slf4j
@Component("agent")
@RequiredArgsConstructor
public class AgentHealthIndicator implements HealthIndicator {

    private final AgentClient agentClient;
    private final ObjectMapper objectMapper;

    @Override
    public Health health() {
        try {
            String json = agentClient.getHealth();
            JsonNode root = objectMapper.readTree(json);
            String status = root.path("status").asText("unknown");

            Health.Builder builder = "ok".equals(status) ? Health.up() : Health.down();
            // Forward individual check results (postgres, redis, agent init)
            root.fields().forEachRemaining(entry ->
                    builder.withDetail(entry.getKey(), entry.getValue().asText()));
            return builder.build();
        } catch (Exception e) {
            log.warn("Python agent health check failed: {}", e.getMessage());
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
