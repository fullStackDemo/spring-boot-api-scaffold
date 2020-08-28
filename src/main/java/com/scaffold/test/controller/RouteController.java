package com.scaffold.test.controller;


import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.entity.Route;
import com.scaffold.test.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 公交路线数据
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    RouteService routeService;

    /**
     * 获取路线状态
     *
     * @return
     */
    @PassToken
    @GetMapping("status")
    public Route getStatus() {
        Route data = routeService.getRouteData();
        return data;
    }
}

