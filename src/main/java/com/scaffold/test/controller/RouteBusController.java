package com.scaffold.test.controller;


import com.scaffold.test.entity.Route;
import com.scaffold.test.entity.RouteBus;
import com.scaffold.test.service.RouteBusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  路线上行驶的动态车辆信息
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */

@Slf4j
@RestController
@RequestMapping("/routeBus")
public class RouteBusController {

    @Autowired
    RouteBusService routeBusService;

    /**
     * 获取路线上正在行驶的车辆信息
     * @param route 路线数据
     * @return
     */
    @GetMapping("list")
    public List<RouteBus> getBusList(Route route){
        return routeBusService.getBusList(route);
    }

    /**
     * 定时更新数据
     */
    @Async
    @Scheduled(fixedDelay = 10000)
    public void getLiveBusList(){
        log.info("公交实时更新");
        Route route = new Route();
        route.setRouteCode("d3ulY7HyJmfscUBCvHDX4dBnV8Te5ZK4orhtzbSf0e0=");
        routeBusService.getBusList(route);
    }
}

