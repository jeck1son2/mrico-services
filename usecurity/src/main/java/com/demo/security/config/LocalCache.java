package com.demo.security.config;

import com.demo.security.mapper.UserMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class LocalCache {
    @Bean
    public Cache corsCache(UserMapper mapper){
        Cache<List<String>> corsCache = () -> {
            try {
                return CacheBuilder.newBuilder()
                        .expireAfterAccess(10, TimeUnit.SECONDS)
//                        设置为数据库中获取
//                        .build(CacheLoader.from(() -> mapper.selectOrigins)).get("origins");
                        .build(CacheLoader.from(() -> List.of("*"))).get("origins");
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        };
        return corsCache;
    }

    @Bean
    public Cache urlCache(UserMapper mapper){
        Cache<List<String>> urlCache = () -> {
            try {
                return CacheBuilder.newBuilder()
                        .expireAfterAccess(10, TimeUnit.SECONDS)
                        .build(CacheLoader.from(() -> List.of("aabb"))).get("origins");
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        };
        return urlCache;
    }
}
