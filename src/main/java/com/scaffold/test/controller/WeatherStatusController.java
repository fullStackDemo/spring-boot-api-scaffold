package com.scaffold.test.controller;


import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.service.WeatherStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */
@RestController
@RequestMapping("/weather/status")
public class WeatherStatusController {

    @Autowired
    private WeatherStatusService weatherStatusService;

    @PassToken
    @PostMapping("/update")
    public Result updateWeatherStatus() {
        weatherStatusService.insertStatus();
        return ResultGenerator.setSuccessResult("更新");
    }

}

