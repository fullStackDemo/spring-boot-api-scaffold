package com.scaffold.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.Route;
import com.scaffold.test.entity.RouteBus;
import com.scaffold.test.entity.RouteStop;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
public interface RouteBusService extends IService<RouteBus> {

    List<RouteBus> getBusList(Route route);

    void insertRouteBus(RouteStop routeStop);

}
