package com.scaffold.test.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * 缓存配置文件
 * 配置文件读取是否启用此配置
 * @author alex
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "caching", name = "enabled", havingValue = "true")
public class CacheConfig {

//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("cacheData");
//    }

    private Duration timeToLive = Duration.ofSeconds(600);

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        // 解决从redis数据缓存value使用Jackson2JsonRedisSerialize序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存的存活时间
                .entryTtl(timeToLive)
                // 设置缓存名字的前缀
                .prefixCacheNameWith("test")
                // 禁止缓存名字的前缀
                .disableKeyPrefix()
                // 序列化value数据
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                // 禁止缓存 Null 数据
                .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(config).build();
        return cacheManager;
    }

}
