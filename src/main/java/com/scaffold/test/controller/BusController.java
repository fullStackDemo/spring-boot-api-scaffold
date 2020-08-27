package com.scaffold.test.controller;


import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.entity.Bus;
import com.scaffold.test.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author alex wong
 * @since 2020-08-26
 */
@RestController
@RequestMapping("/bus")
public class BusController {

    @Autowired
    BusService busService;

    /**
     * 获取路线状态
     *
     * @return
     */
    @PassToken
    @GetMapping("status")
    public Bus getStatus() {
        Bus data = busService.getRouteData();
        return data;
    }
}

