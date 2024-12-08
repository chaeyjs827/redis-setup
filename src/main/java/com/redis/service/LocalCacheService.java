package com.redis.service;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LocalCacheService {

    private final CacheManager caffeineCacheManager;

    public LocalCacheService(
            @Qualifier("caffeineCacheManager") CacheManager caffeineCacheManager
    ) {
        this.caffeineCacheManager = caffeineCacheManager;
    }

    // 1. 캐시 값 조회
    public Optional<Object> getCacheValue(String cacheName, String key) {
        return Optional.ofNullable(caffeineCacheManager.getCache(cacheName))
                .map(cache -> {
                    Object value = cache.get(key, Object.class);
                    if (ObjectUtils.isEmpty(value)) {
                        log.debug("Cache miss for key: " + key);
                    }
                    return value;
                });
    }

    // 2. 캐시 값 삭제
    public boolean evictCache(String cacheName, String key) {
        if (caffeineCacheManager.getCache(cacheName) != null) {
            caffeineCacheManager.getCache(cacheName).evict(key);
            return true;
        }
        return false;
    }

    // 3. 캐시 TTL 조회 (Caffeine 전용)
    public Optional<Long> getTTL(String cacheName, String key) {
        return Optional.ofNullable(caffeineCacheManager.getCache(cacheName))
                .filter(c -> c instanceof CaffeineCache)
                .map(c -> (CaffeineCache) c)
                .map(CaffeineCache::getNativeCache)
                .map(nativeCache -> (Cache<?, ?>) nativeCache)
                .map(cache -> cache.policy().expireAfterWrite())
                .flatMap(policy -> policy.map(p -> p.getExpiresAfter(TimeUnit.MILLISECONDS)));
    }

    // 4. 캐시 갱신
    public boolean updateCache(String cacheName, String key, Object value) {
        org.springframework.cache.Cache cache = caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key); // 기존 값 삭제
            cache.put(key, value); // 새로운 값 추가
            return true;
        }
        return false;
    }

    // 모든 캐시의 통계 조회
    public void logAllCacheStats() {
        // CacheManager에서 모든 캐시 이름 가져오기
        caffeineCacheManager.getCacheNames().forEach(this::logCacheStats);
    }

    // 캐시 통계 조회(로그 표시 용)
    private void logCacheStats(String cacheName) {
        CaffeineCache caffeineCache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);

        if (caffeineCache != null) {
            // 네이티브 캐시 객체 가져오기
            Cache<Object, Object> nativeCache = (Cache<Object, Object>) caffeineCache.getNativeCache();

            // 통계가 활성화되어 있지 않으면 stats() 메서드를 호출할 수 없으므로,
            // Caffeine 캐시가 stats() 메서드를 제공하는지 확인
            if (nativeCache != null) {
                System.out.println("Cache Stats for '" + cacheName + "':");
                var stats = nativeCache.stats();
                if (stats != null) {
                    System.out.println("Cache Hits: " + stats.hitCount());
                    System.out.println("Cache Misses: " + stats.missCount());
                    System.out.println("Cache Evictions: " + stats.evictionCount());
                    System.out.println("Cache Size: " + nativeCache.estimatedSize());
                } else {
                    System.out.println("Stats not available for cache.");
                }
            }
        } else {
            System.out.println("Cache '" + cacheName + "' not found");
        }
    }
}

