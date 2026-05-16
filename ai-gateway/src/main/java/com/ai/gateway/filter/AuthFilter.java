package com.ai.gateway.filter;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.ai.gateway.config.properties.IgnoreWhiteProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关鉴权过滤器
 *
 * @author root 2026-05-16 16:04
 */
@Configuration
public class AuthFilter {

    @Resource
    private IgnoreWhiteProperties ignoreWhiteProperties;

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
            .addInclude("/**")
            .setExcludeList(ignoreWhiteProperties.getWhites())
            .addExclude("/favicon.ico", "/actuator", "/actuator/**")
            .setAuth(obj -> {
                SaRouter.match("/**")
                    .notMatch(ignoreWhiteProperties.getWhites())
                    .check(r -> StpUtil.checkLogin());
            })
            .setError(e -> "认证失败，无法访问系统资源");
    }

}
