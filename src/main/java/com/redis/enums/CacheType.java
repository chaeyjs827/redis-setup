package com.redis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    LOCAL_META_DATA("local_meta_data", 12, 10000)
    , REMOTE_META_DATA("remote_meta_data", 12, 10000);
    ;

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
