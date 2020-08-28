package com.scaffold.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.Route;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
public interface RouteService extends IService<Route> {

    /**
     * 捕获全部路线数据
     *
     * @return
     */
    Route getRouteData();

    /**
     * 获取routeCode对应路线数据
     * @param routeCode
     * @return
     */
    Route findRouteByCode(String routeCode);

}
