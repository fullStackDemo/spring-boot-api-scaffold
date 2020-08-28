package com.scaffold.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.RouteStop;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
public interface RouteStopService extends IService<RouteStop> {

    void insertStop(RouteStop routeStop);

    /**
     * 查找当前路线的所有站点
     * @param routeCode
     * @return
     */
    List<RouteStop> findCurrentRouteStopList(String routeCode);
}
