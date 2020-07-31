package com.scaffold.test.service.impl;

import com.scaffold.test.entity.WeatherStatus;
import com.scaffold.test.mapper.WeatherStatusMapper;
import com.scaffold.test.service.WeatherStatusService;
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
public class WeatherStatusServiceImpl extends ServiceImpl<WeatherStatusMapper, WeatherStatus> implements WeatherStatusService {

}
