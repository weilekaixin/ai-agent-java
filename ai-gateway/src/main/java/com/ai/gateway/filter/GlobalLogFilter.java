package com.ai.gateway.filter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ai.common.core.utils.StringUtils;
import com.ai.common.json.utils.JsonUtils;
import com.ai.gateway.config.properties.ApiDecryptProperties;
import com.ai.gateway.config.properties.CustomGatewayProperties;
import com.ai.gateway.utils.WebFluxUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

/**
 * 全局日志过滤器
 * <p>
 * 用于打印请求执行参数与响应时间等等
 *
 * @author root 2026-05-16 16:04
 */
@Slf4j
@Component
public class GlobalLogFilter implements GlobalFilter, Ordered {

    @Resource
    private CustomGatewayProperties customGatewayProperties;
    @Resource
    private ApiDecryptProperties apiDecryptProperties;

    private static final String START_TIME = "startTime";

    /**
     * 敏感字段（日志脱敏）
     */
    private static final String[] EXCLUDE_PROPERTIES = {
        "password", "oldPassword", "newPassword", "confirmPassword"
    };

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!customGatewayProperties.getRequestLog()) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        String path = WebFluxUtils.getOriginalRequestUrl(exchange);
        String url = request.getMethod().name() + " " + path;

        // SSE 流式接口请求体可能很大，跳过耗时的 JSON 反序列化与脱敏
        if (path.contains("/api/chat")) {
            log.info("[AI]开始请求 => URL[{}]", url);
            exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Long startTime = exchange.getAttribute(START_TIME);
                if (startTime != null) {
                    long executeTime = (System.currentTimeMillis() - startTime);
                    log.info("[AI]结束请求 => URL[{}],耗时:[{}]毫秒", url, executeTime);
                }
            }));
        }

        // 打印请求参数
        if (WebFluxUtils.isJsonRequest(exchange)) {
            if (apiDecryptProperties.getEnabled()
                && ObjectUtil.isNotNull(request.getHeaders().getFirst(apiDecryptProperties.getHeaderFlag()))) {
                log.info("[AI]开始请求 => URL[{}],参数类型[encrypt]", url);
            } else {
                String jsonParam = WebFluxUtils.resolveBodyFromCacheRequest(exchange);
                if (StringUtils.isNotBlank(jsonParam)) {
                    ObjectMapper objectMapper = JsonUtils.getObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(jsonParam);
                    removeSensitiveFields(rootNode, EXCLUDE_PROPERTIES);
                    jsonParam = rootNode.toString();
                }
                log.info("[AI]开始请求 => URL[{}],参数类型[json],参数:[{}]", url, jsonParam);
            }
        } else {
            MultiValueMap<String, String> parameterMap = request.getQueryParams();
            if (MapUtil.isNotEmpty(parameterMap)) {
                LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>(parameterMap);
                MapUtil.removeAny(map, EXCLUDE_PROPERTIES);
                String parameters = JsonUtils.toJsonString(map);
                log.info("[AI]开始请求 => URL[{}],参数类型[param],参数:[{}]", url, parameters);
            } else {
                log.info("[AI]开始请求 => URL[{}],无参数", url);
            }
        }

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                long executeTime = (System.currentTimeMillis() - startTime);
                log.info("[AI]结束请求 => URL[{}],耗时:[{}]毫秒", url, executeTime);
            }
        }));
    }

    private void removeSensitiveFields(JsonNode node, String[] excludeProperties) {
        if (node == null) {
            return;
        }
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Set<String> fieldsToRemove = new HashSet<>();
            objectNode.fieldNames().forEachRemaining(fieldName -> {
                if (ArrayUtil.contains(excludeProperties, fieldName)) {
                    fieldsToRemove.add(fieldName);
                }
            });
            fieldsToRemove.forEach(objectNode::remove);
            objectNode.elements().forEachRemaining(child -> removeSensitiveFields(child, excludeProperties));
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (JsonNode child : arrayNode) {
                removeSensitiveFields(child, excludeProperties);
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
