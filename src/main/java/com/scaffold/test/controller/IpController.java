package com.scaffold.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.redis.RedisUtils;
import com.scaffold.test.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alex
 */

@RestController
@RequestMapping("ip")
@Slf4j
public class IpController {

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 第二种方法（耗时1~2S）
     * 获取IP并存入redis, 判断是国内外IP
     *
     * @return
     */
    @GetMapping("me")
    public Result setIp() {
        String ip = IpUtils.getIpAddress();
        JSONObject object = new JSONObject();
        if (!Util.isIpAddress(ip)) {
            object.put("ip", null);
            log.error("Error: Invalid ip address");
            object.put("error", "Error: Invalid ip address");
            object.put("country", null);
        } else {
            object.put("ip", ip);
            String cityInfo = IpUtils.getCityInfo(ip);
            if (cityInfo.length() > 0) {
                String[] strings = cityInfo.split("\\|");
                object.put("country", strings[0]);
            }
        }
        return ResultGenerator.setSuccessResult(object);
    }


}
