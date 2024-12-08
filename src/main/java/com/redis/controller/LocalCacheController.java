package com.redis.controller;

import com.redis.common.utils.FormmatUtils;
import com.redis.service.LocalCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cache")
public class LocalCacheController {

    private final LocalCacheService localCacheService;

    public LocalCacheController(LocalCacheService localCacheService) {
        this.localCacheService = localCacheService;
    }

    // 1. 캐시 값 조회
    @GetMapping
    public ResponseEntity<Object> getCacheValue(
            @RequestParam String cacheName,
            @RequestParam String cacheKey,
            @RequestParam String subKey
    ) {
        String key = FormmatUtils.generateKey(cacheKey, subKey);
        return localCacheService.getCacheValue(cacheName, key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(List.of()));
    }

    // 2. 캐시 값 삭제
    @DeleteMapping("/{cacheName}/{key}")
    public ResponseEntity<String> evictCache(@PathVariable String cacheName, @PathVariable String key) {
        boolean success = localCacheService.evictCache(cacheName, key);
        if (success) {
            return ResponseEntity.ok("Cache entry evicted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. 캐시 TTL 확인
    @GetMapping("/ttl")
    public ResponseEntity<Long> getCacheTTL(
            @RequestParam String cacheName,
            @RequestParam String cacheKey,
            @RequestParam String subKey
    ) {
        String key = FormmatUtils.generateKey(cacheKey, subKey);
        return localCacheService.getTTL(cacheName, key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    // 4. 캐시 갱신
    @PostMapping("/{cacheName}/{key}")
    public ResponseEntity<String> updateCache(
            @PathVariable String cacheName,
            @PathVariable String key,
            @RequestBody Object value) {
        boolean success = localCacheService.updateCache(cacheName, key, value);
        if (success) {
            return ResponseEntity.ok("Cache entry updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update cache.");
        }
    }
}

