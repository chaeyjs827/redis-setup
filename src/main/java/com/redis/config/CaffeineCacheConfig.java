package com.redis.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.redis.enums.CacheType;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {

    @Bean(name = "caffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("gep_mq_cache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(CacheType.LOCAL_META_DATA.getExpiredAfterWrite(), TimeUnit.MINUTES)
                .maximumSize(CacheType.LOCAL_META_DATA.getMaximumSize()));
        return cacheManager;
    }

}

