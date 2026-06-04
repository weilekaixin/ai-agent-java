package com.ai.agent;

import com.ai.agent.model.ChatQuery;
import com.ai.agent.model.MultiAgentQuery;
import com.ai.agent.model.PersonaCreate;
import com.ai.agent.model.PersonaUpdate;
import com.ai.agent.model.ResumeQuery;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Delete;
import com.dtflys.forest.annotation.ForestClient;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Put;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.http.ForestSSE;

import java.util.Map;

/**
 * AI Agent 客户端
 *
 * @author root 2026-05-16 16:04
 */
@BaseRequest(
    baseURL = "${agent_base_url}",
    headers = {"X-Api-Key: ${agent_api_key}"},
    interceptor = {AgentAuthInterceptor.class}
)
@ForestClient
public interface AgentClient {

    // ─ SSE 流式接口 ───────────────────────────────────────────────────────────────

    @Post(value = "/api/chat", contentType = ContentType.APPLICATION_JSON)
    ForestSSE chat(@JSONBody ChatQuery query);

    @Post(value = "/api/resume", contentType = ContentType.APPLICATION_JSON)
    ForestSSE resume(@JSONBody ResumeQuery query);

    @Post(value = "/api/multi-agent/chat", contentType = ContentType.APPLICATION_JSON)
    ForestSSE multiAgentChat(@JSONBody MultiAgentQuery query);

    // ─ 会话管理 ───────────────────────────────────────────────────────────────

    @Get(value = "/api/sessions")
    String getSessions();

    @Get(value = "/api/sessions/{sessionId}/messages")
    String getSessionMessages(@Var("sessionId") String sessionId);

    @Get(value = "/api/sessions/{sessionId}/export")
    String exportSession(@Var("sessionId") String sessionId);

    @Put(value = "/api/sessions/{sessionId}/title", contentType = ContentType.APPLICATION_JSON)
    String updateSessionTitle(@Var("sessionId") String sessionId, @JSONBody Map<String, String> body);

    @Post(value = "/api/sessions/{sessionId}/clear", contentType = ContentType.APPLICATION_JSON)
    String clearSession(@Var("sessionId") String sessionId);

    @Delete(value = "/api/sessions/{sessionId}")
    String deleteSession(@Var("sessionId") String sessionId);

    // ─ AutoDream ───────────────────────────────────────────────────────────────

    @Post(value = "/api/dream", contentType = ContentType.APPLICATION_JSON)
    String triggerDream();

    // ─ Ops ─────────────────────────────────────────────────────────────────────────────

    @Get(value = "/health")
    String getHealth();

    // ─ Structured Output ───────────────────────────────────────────────────────────────

    @Post(value = "/api/structured", contentType = ContentType.APPLICATION_JSON)
    String structuredOutput(@JSONBody Map<String, Object> body);

    // ─ Persona 管理 ───────────────────────────────────────────────────────────────

    @Get(value = "/api/personas")
    String getPersonas();

    @Post(value = "/api/personas", contentType = ContentType.APPLICATION_JSON)
    String createPersona(@JSONBody PersonaCreate body);

    @Get(value = "/api/personas/{personaId}")
    String getPersona(@Var("personaId") String personaId);

    @Put(value = "/api/personas/{personaId}", contentType = ContentType.APPLICATION_JSON)
    String updatePersona(@Var("personaId") String personaId, @JSONBody PersonaUpdate body);

    @Delete(value = "/api/personas/{personaId}")
    String deletePersona(@Var("personaId") String personaId);
}
