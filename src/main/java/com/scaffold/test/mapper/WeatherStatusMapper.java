package com.scaffold.test.mapper;

import com.scaffold.test.entity.WeatherStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */
public interface WeatherStatusMapper extends BaseMapper<WeatherStatus> {

    void insertStatus(WeatherStatus weatherStatus);

}
