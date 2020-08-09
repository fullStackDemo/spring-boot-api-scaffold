package com.scaffold.test.controller;

import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alex
 */

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisUtils redisUtils;

    /**
     *  获取
     * @param key
     * @return
     */
    @GetMapping("get")
    @PassToken
    public Object getRedis(@RequestParam String key) {
        return redisUtils.get(key);
    }

    /**
     * 删除
     * @param key 可以是单个，也可以是多个
     * @return
     */
    @GetMapping("delete")
    @PassToken
    public Object deleteRedis(@RequestParam String key) {
        return redisUtils.delete(key);
    }


}
