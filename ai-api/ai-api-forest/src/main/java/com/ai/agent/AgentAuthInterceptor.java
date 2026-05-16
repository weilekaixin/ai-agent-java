package com.ai.agent;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * 签名拦截器生命周期
 *
 * @author root 2026-05-16 16:04
 */
@Slf4j
public class AgentAuthInterceptor implements Interceptor<Object> {
    @Override
    public boolean beforeExecute(ForestRequest request) {
        // 在请求前加鉴权头
        return true;
    }

    @Override
    public void onSuccess(Object data, ForestRequest request, ForestResponse response) {
        // 请求成功处理
    }

    @Override
    public void onError(ForestRuntimeException ex, ForestRequest request, ForestResponse response) {
        // 请求失败处理
    }
}
