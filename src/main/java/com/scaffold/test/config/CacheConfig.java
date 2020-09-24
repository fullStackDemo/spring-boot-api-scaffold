package com.scaffold.test.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
//配置文件读取是否启用此配置
@ConditionalOnProperty(prefix = "caching", name = "enabled", havingValue = "true")
public class CacheConfig {

    // ConcurrentMapCacheManager 最简单的缓存
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("cacheData", "otherCache");
    }

}
