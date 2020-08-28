package com.scaffold.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.Bus;
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
     * 获取路线数据
     *
     * @return
     */
    Bus getRouteData();

}
