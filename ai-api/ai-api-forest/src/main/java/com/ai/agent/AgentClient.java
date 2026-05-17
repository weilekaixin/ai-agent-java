package com.ai.agent;

import com.ai.agent.model.ChatQuery;
import com.ai.agent.model.ResumeQuery;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.ForestClient;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.http.ForestSSE;

/**
 * AI Agent客户端
 *
 * @author root 2026-05-16 16:04
 */
@BaseRequest(
    baseURL = "${agent_base_url}",
    interceptor = {AgentAuthInterceptor.class}
)
@ForestClient
public interface AgentClient {
    /**
     * 聊天接口
     *
     * @param query 聊天请求
     * @return SSE 流
     */
    @Post(value = "/api/chat", contentType = ContentType.APPLICATION_JSON)
    ForestSSE chat(@JSONBody ChatQuery query);

    /**
     * 恢复执行敏感操作
     *
     * @param query 恢复请求
     * @return SSE 流
     */
    @Post(value = "/api/resume", contentType = ContentType.APPLICATION_JSON)
    ForestSSE resume(@JSONBody ResumeQuery query);
}
