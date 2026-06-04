package com.ai.modules.agent.controller;

import com.ai.agent.AgentClient;
import com.ai.agent.model.PersonaCreate;
import com.ai.agent.model.PersonaUpdate;
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

/**
 * 自定义 AI 角色接口（代理 Python FastAPI 的 /api/personas 系列）
 *
 * @author root 2026-06-04
 */
@RestController
@RequestMapping("/persona")
@RequiredArgsConstructor
public class PersonaController {

    private final AgentClient agentClient;

    /** 列出所有活跃 Persona */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listPersonas() {
        return ResponseEntity.ok(agentClient.getPersonas());
    }

    /** 创建新 Persona */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPersona(@RequestBody PersonaCreate body) {
        return ResponseEntity.ok(agentClient.createPersona(body));
    }

    /** 获取单个 Persona 详情（含 system_prompt） */
    @GetMapping(value = "/{personaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPersona(@PathVariable String personaId) {
        return ResponseEntity.ok(agentClient.getPersona(personaId));
    }

    /** 更新 Persona 字段 */
    @PutMapping(value = "/{personaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePersona(
            @PathVariable String personaId,
            @RequestBody PersonaUpdate body) {
        return ResponseEntity.ok(agentClient.updatePersona(personaId, body));
    }

    /** 删除 Persona */
    @DeleteMapping(value = "/{personaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deletePersona(@PathVariable String personaId) {
        return ResponseEntity.ok(agentClient.deletePersona(personaId));
    }
}
