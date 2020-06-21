package com.scaffold.test.controller;


import com.scaffold.test.entity.Test;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author alex wong
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public Object testGet(@RequestParam String name, @RequestParam int age) {
        Map<String, Object> result = new HashMap<>();
        // 数据处理
        result.put("name", name);
        result.put("age", age);
        return result;
    }

    // 单个参数接收
    @PostMapping("/post")
    public Object testPost(@RequestParam String name, @RequestParam int age) {
        Map<String, Object> result = new HashMap<>();
        // 数据处理
        result.put("name", name);
        result.put("age", age);
        return result;
    }

    // 实体类接收
    @PostMapping("/post2")
    public Object testPost2(Test data) {
        Map<String, Object> result = new HashMap<>();
        // 数据处理
        result.put("name", data.getName());
        result.put("age", data.getAge());
        return result;
    }

    // 数组接收
    @PostMapping("/post3")
    public Object testPost3(@RequestBody List<Test> data) {
        ArrayList<Object> result = new ArrayList<>();
        Map<String, Object> testMap = new HashMap<>();
        for (Test test : data) {
            // 逻辑处理在这里
            testMap.put("name", test.getName());
            testMap.put("age", test.getAge());
            result.add(testMap);
        }
        return result;
    }

    // 数组接收
    @GetMapping("/get4")
    public Object testGet4(@RequestParam String[] productId) {
        ArrayList<Object> list = new ArrayList<>();
        for (String id : productId) {
            // 数据处理
            list.add(id);
        }
        return list;
    }

}

