package com.scaffold.test.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.WeatherTime;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */
public interface WeatherTimeService extends IService<WeatherTime> {

    List<WeatherTime> getSevenDayTime(JSONObject day7Data);
}
