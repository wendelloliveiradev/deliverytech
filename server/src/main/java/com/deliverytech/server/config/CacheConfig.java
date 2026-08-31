package com.deliverytech.server.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    CacheManager cacheManager(@Value("${app.cache.ttl:10m}") Duration cacheTtl) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("products", "productLists");
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(cacheTtl).maximumSize(1_000));
        return cacheManager;
    }
}