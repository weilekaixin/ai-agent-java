package com.ai.agent;

import com.ai.agent.model.ChatQuery;
import com.ai.agent.model.FeedbackCreate;
import com.ai.agent.model.MultiAgentQuery;
import com.ai.agent.model.PersonaCreate;
import com.ai.agent.model.PersonaUpdate;
import com.ai.agent.model.PinCreate;
import com.ai.agent.model.ResumeQuery;
import com.ai.agent.model.TagCreate;
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
 * AI Agent HTTP 客户端（Forest）
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

    // ─ SSE 流式接口 ─────────────────────────────────────────────────────────────────────

    @Post(value = "/api/chat", contentType = ContentType.APPLICATION_JSON)
    ForestSSE chat(@JSONBody ChatQuery query);

    @Post(value = "/api/resume", contentType = ContentType.APPLICATION_JSON)
    ForestSSE resume(@JSONBody ResumeQuery query);

    @Post(value = "/api/multi-agent/chat", contentType = ContentType.APPLICATION_JSON)
    ForestSSE multiAgentChat(@JSONBody MultiAgentQuery query);

    // ─ 会话管理 ────────────────────────────────────────────────────────────────────

    @Get(value = "/api/sessions")
    String getSessions();

    @Get(value = "/api/sessions/search?q={q}&page={page}&size={size}")
    String searchMessages(@Var("q") String q, @Var("page") int page, @Var("size") int size);

    @Get(value = "/api/sessions/{sessionId}/messages")
    String getSessionMessages(@Var("sessionId") String sessionId);

    @Get(value = "/api/sessions/{sessionId}/export")
    String exportSession(@Var("sessionId") String sessionId);

    @Get(value = "/api/sessions/{sessionId}/export/csv")
    String exportSessionCsv(@Var("sessionId") String sessionId);

    @Put(value = "/api/sessions/{sessionId}/title", contentType = ContentType.APPLICATION_JSON)
    String updateSessionTitle(@Var("sessionId") String sessionId, @JSONBody Map<String, String> body);

    @Post(value = "/api/sessions/{sessionId}/auto-title", contentType = ContentType.APPLICATION_JSON)
    String autoTitle(@Var("sessionId") String sessionId);

    @Post(value = "/api/sessions/{sessionId}/clear", contentType = ContentType.APPLICATION_JSON)
    String clearSession(@Var("sessionId") String sessionId);

    @Delete(value = "/api/sessions/{sessionId}")
    String deleteSession(@Var("sessionId") String sessionId);

    // ─ 消息反馈 ───────────────────────────────────────────────────────────────────────

    @Post(value = "/api/messages/{messageId}/feedback", contentType = ContentType.APPLICATION_JSON)
    String createFeedback(@Var("messageId") Long messageId, @JSONBody FeedbackCreate body);

    @Get(value = "/api/messages/{messageId}/feedback")
    String getMessageFeedback(@Var("messageId") Long messageId);

    @Get(value = "/api/sessions/{sessionId}/feedback")
    String getSessionFeedback(@Var("sessionId") String sessionId);

    @Get(value = "/api/sessions/{sessionId}/feedback/stats")
    String getSessionFeedbackStats(@Var("sessionId") String sessionId);

    // ─ 会话标签 ─────────────────────────────────────────────────────────────────────

    @Post(value = "/api/sessions/{sessionId}/tags", contentType = ContentType.APPLICATION_JSON)
    String addSessionTag(@Var("sessionId") String sessionId, @JSONBody TagCreate body);

    @Delete(value = "/api/sessions/{sessionId}/tags/{tag}")
    String removeSessionTag(@Var("sessionId") String sessionId, @Var("tag") String tag);

    @Get(value = "/api/sessions/{sessionId}/tags")
    String getSessionTags(@Var("sessionId") String sessionId);

    @Get(value = "/api/tags")
    String listAllTags();

    @Get(value = "/api/tags/{tag}/sessions?page={page}&size={size}")
    String getSessionsByTag(@Var("tag") String tag, @Var("page") int page, @Var("size") int size);

    // ─ 消息置顶 ─────────────────────────────────────────────────────────────────────

    @Post(value = "/api/messages/{messageId}/pin", contentType = ContentType.APPLICATION_JSON)
    String pinMessage(@Var("messageId") Long messageId, @JSONBody PinCreate body);

    @Delete(value = "/api/messages/{messageId}/pin")
    String unpinMessage(@Var("messageId") Long messageId);

    @Get(value = "/api/sessions/{sessionId}/pinned")
    String getPinnedMessages(@Var("sessionId") String sessionId);

    // ─ Token 用量 ────────────────────────────────────────────────────────────────────

    @Get(value = "/api/sessions/{sessionId}/usage?page={page}&size={size}")
    String getSessionUsage(
            @Var("sessionId") String sessionId,
            @Var("page") int page,
            @Var("size") int size);

    @Get(value = "/api/sessions/{sessionId}/usage/summary")
    String getSessionUsageSummary(@Var("sessionId") String sessionId);

    // ─ AutoDream ─────────────────────────────────────────────────────────────────────

    @Post(value = "/api/dream", contentType = ContentType.APPLICATION_JSON)
    String triggerDream();

    // ─ Ops ─────────────────────────────────────────────────────────────────────────────

    @Get(value = "/health")
    String getHealth();

    // ─ Structured Output ──────────────────────────────────────────────────────────────────

    @Post(value = "/api/structured", contentType = ContentType.APPLICATION_JSON)
    String structuredOutput(@JSONBody Map<String, Object> body);

    // ─ Persona 管理 ──────────────────────────────────────────────────────────────────

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
