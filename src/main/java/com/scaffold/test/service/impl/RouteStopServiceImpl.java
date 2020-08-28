package com.scaffold.test.service.impl;

import com.scaffold.test.entity.RouteStop;
import com.scaffold.test.mapper.RouteStopMapper;
import com.scaffold.test.service.RouteStopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
@Service
public class RouteStopServiceImpl extends ServiceImpl<RouteStopMapper, RouteStop> implements RouteStopService {

    @Autowired
    RouteStopMapper routeStopMapper;


    @Override
    public void insertStop(RouteStop routeStop) {
        routeStopMapper.insertRoute(routeStop);
    }

    @Override
    public List<RouteStop> findCurrentRouteStopList(String routeCode) {
        return routeStopMapper.findRouteStopList(routeCode);
    }
}
