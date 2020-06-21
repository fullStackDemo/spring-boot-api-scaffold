package com.scaffold.test.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author alex wong
 * @since 2020-06-14
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public Object testGet(@RequestParam String name, @RequestParam int age){
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("age", age);
        return result;
    }

}

