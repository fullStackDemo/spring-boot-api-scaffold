package com.scaffold.test.controller;

import com.scaffold.test.entity.Weather;
import com.scaffold.test.service.WeatherService;
import com.scaffold.test.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    WeatherService weatherService;

    @GetMapping("export")
    public void export(Map<String, Object> params){
        List<Weather> list = weatherService.selectAll();
        ExcelUtils.createExcel(list);
    }
}
