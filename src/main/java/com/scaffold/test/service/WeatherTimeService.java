package com.scaffold.test.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.WeatherTime;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */
public interface WeatherTimeService extends IService<WeatherTime> {

    /**
     * 获取七日分时数据
     *
     * @param day7Data
     * @return
     */
    List<WeatherTime> getSevenDayTime(JSONObject day7Data);

    /**
     * 获取当前日期下分时数据
     *
     * @param date
     * @return
     */
    List<WeatherTime> getCurrentDateTime(String date);
}
