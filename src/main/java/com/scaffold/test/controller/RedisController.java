package com.scaffold.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.redis.RedisUtils;
import com.scaffold.test.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author alex
 */

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取
     *
     * @param key
     * @return
     */
    @GetMapping("get")
    @PassToken
    public Object getRedis(@RequestParam String key) {
        return redisUtils.get(key);
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    @GetMapping("set")
    @PassToken
    public void setRedis(@RequestParam String key, @RequestParam String value) {
        redisUtils.set(key, value, 60, TimeUnit.SECONDS);
    }

    /**
     * 删除
     *
     * @param key 可以是单个，也可以是多个
     * @return
     */
    @GetMapping("delete")
    @PassToken
    public Object deleteRedis(@RequestParam String key) {
        return redisUtils.delete(key);
    }


    /**
     * 第一种方法(初次耗时18S)
     * 获取IP并存入redis
     */
    @GetMapping("ip2")
    public Boolean setIp2(@RequestParam String ip) {
        // 判断IP是否在缓存数据中
        Object exist = redisUtils.get(ip);
        if (exist == null) {
            // 判断IP库是否存在
            Map<Integer, List<int[]>> ipData= IpUtils.initData();
            boolean isChinaIp = IpUtils.isChinaIp(ipData, ip);
            return isChinaIp;
        } else {
            return exist.equals(true);
        }
    }

    /**
     * 第二种方法（耗时）
     * 获取IP并存入redis, 判断是国内外IP
     */
    @GetMapping("ip")
    public Result setIp(@RequestParam String ip) {
        Map<String, Object> ipData;
        // 从缓存中获取数据
        Object ip_map = redisUtils.get("ip_map");
        if (ip_map == null) {
            ipData = (Map<String, Object>) ip_map;
        } else {
            ipData = IpUtils.getIpList();
            redisUtils.set("ip_map", ipData, 2, TimeUnit.HOURS);
        }
        Boolean inChina = IpUtils.ipInChina(ipData, ip);
        JSONObject object = new JSONObject();
        object.put("ip", ip);
        object.put("CN", inChina);
        return ResultGenerator.setSuccessResult(object);
    }


}
