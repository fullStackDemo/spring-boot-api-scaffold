package com.scaffold.test.service.impl;

import com.scaffold.test.entity.WeatherTime;
import com.scaffold.test.mapper.WeatherTimeMapper;
import com.scaffold.test.service.WeatherTimeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */
@Service
public class WeatherTimeServiceImpl extends ServiceImpl<WeatherTimeMapper, WeatherTime> implements WeatherTimeService {

}
