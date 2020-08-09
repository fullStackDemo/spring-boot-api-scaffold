package com.scaffold.test.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author alex
 * Redis工具类
 */

@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;


    public Object get(String key){
        return  redisTemplate.opsForValue().get(key);
    }

}
