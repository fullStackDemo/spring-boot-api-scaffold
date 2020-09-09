package com.scaffold.test.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author alex
 * Redis工具类
 */

@Component
public class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置key value
     *
     * @param key key
     * @param value value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置key value，并设置过期时间
     *
     * @param key key
     * @param value value
     * @param timeout 过期时间
     * @param unit 时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *            秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

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

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }




}
