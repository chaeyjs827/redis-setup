package com.redis.common.utils;

import org.apache.commons.lang3.StringUtils;

public class FormmatUtils {

    public static String generateKey(String cacheKey, String subKey) {
        if (StringUtils.isNotEmpty(cacheKey) && StringUtils.isNotEmpty(subKey)) {
            return cacheKey + ":" + subKey;
        }
        return null;
    }

}
