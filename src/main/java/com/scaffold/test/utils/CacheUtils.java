package com.scaffold.test.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheUtils {

    public static CacheManager getCacheManager(){
        return new ConcurrentMapCacheManager("cacheData", "otherCache");
    }

    // 最简单的缓存
    public static void main(String[] args) {
        Cache cacheData = new ConcurrentMapCacheManager().getCache("cacheData");
        System.out.println(cacheData);
    }
}
