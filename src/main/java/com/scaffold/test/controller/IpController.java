package com.scaffold.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.Util;
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

    /**
     * 获取IP归属地
     *
     * @return Object
     */
    @GetMapping("me")
    public Result setIp(String ip) {
        if (ip == null) {
            ip = IpUtils.getIpAddress();
        }
        log.info("The current ip is " + ip);
        JSONObject object = new JSONObject();
        if (!Util.isIpAddress(ip)) {
            object.put("ip", ip);
            log.error("Error: Invalid ip address");
            object.put("error", "Error: Invalid ip address");
            object.put("country", null);
        } else {
            object.put("ip", ip);
            String cityInfo = IpUtils.getCityInfo(ip);
            if (cityInfo.length() > 0) {
                String[] strings = cityInfo.split("\\|");
                object.put("origin", cityInfo);
                object.put("country", strings[0]);
            }
        }
        return ResultGenerator.setSuccessResult(object);
    }


}
