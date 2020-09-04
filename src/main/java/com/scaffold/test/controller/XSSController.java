package com.scaffold.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xss")
public class XSSController {


    /**
     * 获取输入的内容
     * @param content
     * @return
     */
    @GetMapping("get")
    public String getXss(String content) {
        return content;
    }
}
