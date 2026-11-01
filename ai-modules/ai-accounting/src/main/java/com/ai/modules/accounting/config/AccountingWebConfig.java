package com.ai.modules.accounting.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.ai.common.core.exception.BusinessException;
import com.ai.common.redis.constant.LoginConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 记账服务访问控制
 */
@Configuration
public class AccountingWebConfig implements WebMvcConfigurer {
    /**
     * 校验登录及初始密码修改状态，直接访问服务也需要鉴权
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            StpUtil.checkLogin();
            Object passwordChanged = StpUtil.getTokenSession().get(LoginConstants.PASSWORD_CHANGED);
            if (Boolean.FALSE.equals(passwordChanged)) {
                throw new BusinessException("请先修改初始密码", HttpStatus.FORBIDDEN.value());
            }
            if (!Boolean.TRUE.equals(passwordChanged)) {
                throw new BusinessException("登录信息已更新，请重新登录", HttpStatus.UNAUTHORIZED.value());
            }
        })).addPathPatterns("/accounting/**");
    }
}
