package com.scaffold.test.controller;


import com.scaffold.test.entity.Route;
import com.scaffold.test.entity.RouteBus;
import com.scaffold.test.service.RouteBusService;
import org.springframework.beans.factory.annotation.Autowired;
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
}

