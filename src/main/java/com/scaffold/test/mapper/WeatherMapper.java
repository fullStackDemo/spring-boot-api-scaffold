package com.scaffold.test.mapper;

import com.scaffold.test.entity.Weather;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-06-18
 */
public interface WeatherMapper extends BaseMapper<Weather> {

    void insertWeather(Weather weather);
}
