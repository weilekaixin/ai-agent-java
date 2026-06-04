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
    interceptor = {AgentAuthInterceptor.class}
)
@ForestClient
public interface AgentClient {

    // ─── SSE 流式接口 ─────────────────────────────────────────────────────

    @Post(value = "/api/chat", contentType = ContentType.APPLICATION_JSON)
    ForestSSE chat(@JSONBody ChatQuery query);

    @Post(value = "/api/resume", contentType = ContentType.APPLICATION_JSON)
    ForestSSE resume(@JSONBody ResumeQuery query);

    // ─── 会话管理接口（返回原始 JSON 字符串，网关不转换结构） ──────────────────────

    /** 列出全部会话 */
    @Get(value = "/api/sessions")
    String getSessions();

    /** 查询指定会话的全部消息 */
    @Get(value = "/api/sessions/{sessionId}/messages")
    String getSessionMessages(@Var("sessionId") String sessionId);

    /** 删除会话及其消息 */
    @Delete(value = "/api/sessions/{sessionId}")
    String deleteSession(@Var("sessionId") String sessionId);

    // ─── AutoDream 手动触发 ────────────────────────────────────────────────

    /** 手动触发夠间记忆整合任务 */
    @Post(value = "/api/dream", contentType = ContentType.APPLICATION_JSON)
    String triggerDream();
}
