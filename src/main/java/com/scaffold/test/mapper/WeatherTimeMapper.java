package com.scaffold.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scaffold.test.entity.WeatherTime;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */
public interface WeatherTimeMapper extends BaseMapper<WeatherTime> {

    void insertTime(WeatherTime weatherTime);

    List<WeatherTime> findTimeByDate(String date, int hour);

}
