package com.scaffold.test.service;

import com.alibaba.fastjson.JSONObject;
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
public interface WeatherTimeService {

    /**
     * 获取七日分时数据
     *
     * @param day7Data
     * @return
     */
    List<WeatherTime> getSevenDayTime(JSONObject day7Data);

    /**
     * 获取今天逐小时汇报分时数据
     * @return
     */
    void insertCurrentTime();

    /**
     * 获取当前日期下分时数据
     *
     * @param date
     * @return
     */
    List<WeatherTime> getCurrentDateTime(String date, String hour);
}
