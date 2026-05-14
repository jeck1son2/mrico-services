package com.demo.uaa.config.security;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Config region
 */
public class CRCorsConfigurationSource implements CorsConfigurationSource {

    private final UrlBasedCorsConfigurationSource delegate = new UrlBasedCorsConfigurationSource();

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:63342"));
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        //配置浏览器options请求间隔
        config.setMaxAge(600L);
        //可以配置多个url规则
//        delegate.registerCorsConfiguration("/api/**",config);
//        delegate.registerCorsConfiguration("/api/**",config);
        delegate.registerCorsConfiguration("/**",config);
        return delegate.getCorsConfiguration(request);
    }
}
