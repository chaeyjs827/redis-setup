package com.redis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheKey {

    DEVICE_METRIC_INFO("", "device_metric_info", 12, 10000);

    private final String cacheKey;
    private final String subKey;
    private final int expiredAfterWrite;
    private final int maximumSize;

}
