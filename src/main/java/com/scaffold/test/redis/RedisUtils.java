package com.scaffold.test.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author alex
 * Redis工具类
 */

@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除一个
     *
     * @param key 键 可以是一个，也可以是','连接的多个，比如1,2,3,4
     * @return true
     */
    public Object delete(String key) {
        String splitChar = ",";
        if (key.contains(splitChar)) {
            String[] keys = key.split(splitChar);
            List<String> keyList = Arrays.asList(keys);
            return redisTemplate.delete(keyList);
        } else {
            return redisTemplate.delete(key);
        }
    }


}
