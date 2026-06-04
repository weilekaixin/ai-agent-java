package com.ai.agent;

import com.ai.agent.model.ChatQuery;
import com.ai.agent.model.ResumeQuery;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Delete;
import com.dtflys.forest.annotation.ForestClient;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.http.ForestSSE;

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

    // ─── SSE 流式接口 ─────────────────────────────────────────────────────

    @Post(value = "/api/chat", contentType = ContentType.APPLICATION_JSON)
    ForestSSE chat(@JSONBody ChatQuery query);

    @Post(value = "/api/resume", contentType = ContentType.APPLICATION_JSON)
    ForestSSE resume(@JSONBody ResumeQuery query);

    // ─── 会话管理 ────────────────────────────────────────────────────────

    @Get(value = "/api/sessions")
    String getSessions();

    @Get(value = "/api/sessions/{sessionId}/messages")
    String getSessionMessages(@Var("sessionId") String sessionId);

    @Delete(value = "/api/sessions/{sessionId}")
    String deleteSession(@Var("sessionId") String sessionId);

    // ─── AutoDream ──────────────────────────────────────────────────────

    @Post(value = "/api/dream", contentType = ContentType.APPLICATION_JSON)
    String triggerDream();
}
