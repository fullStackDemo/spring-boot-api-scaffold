package com.scaffold.test.controller;


import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.service.WeatherTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */
@RestController
@RequestMapping("/weather")
public class WeatherTimeController {

    @Autowired
    WeatherTimeService weatherTimeService;

    @PassToken
    @GetMapping("time")
    public Result getTime(@RequestParam String date, @RequestParam int hour){
        return ResultGenerator.setSuccessResult(weatherTimeService.getCurrentDateTime(date, hour));
    }

}

