package com.demo.security.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Config region
 */
public class CRCorsConfigurationSource implements CorsConfigurationSource {

    private final UrlBasedCorsConfigurationSource delegate = new UrlBasedCorsConfigurationSource();

    private final Cache<List<String>> cache;

    public CRCorsConfigurationSource(Cache<List<String>> corsCache) throws ExecutionException {
        this.cache = corsCache;
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(cache.getCacheInfo());
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
