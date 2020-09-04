package com.scaffold.test.controller;


import com.scaffold.test.service.FxRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  外汇牌价数据
 * </p>
 *
 * @author alex wong
 * @since 2020-09-04
 */
@RestController
@RequestMapping("/fxRate")
public class FxRateController {


    @Autowired
    FxRateService fxRateService;

    @GetMapping("read")
    public void readExcel(){
        fxRateService.readExcel();
    }

}

