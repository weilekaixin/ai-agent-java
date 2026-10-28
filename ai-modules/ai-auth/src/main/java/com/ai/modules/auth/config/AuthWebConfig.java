package com.ai.modules.auth.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.ai.modules.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 认证服务访问控制
 *
 * @author root
 */
@Configuration
@RequiredArgsConstructor
public class AuthWebConfig implements WebMvcConfigurer {
    private final LoginService loginService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> loginService.checkPasswordChanged()))
                .addPathPatterns("/auth/**")
                .excludePathPatterns("/auth/login", "/auth/me", "/auth/password", "/auth/logout");
    }
}
