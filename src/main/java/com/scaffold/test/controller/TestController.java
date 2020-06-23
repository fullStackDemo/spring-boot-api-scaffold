package com.scaffold.test.controller;


import com.scaffold.test.entity.Test;
import com.scaffold.test.entity.TestList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/get")
    public Object testGet(@RequestParam String name, @RequestParam int age) {
        Map<String, Object> result = new HashMap<>();
        // 数据处理
        result.put("name", name);
        result.put("age", age);
        return result;
    }

    // 数组接收
    @GetMapping("/getList")
    public Object testGet4(@RequestParam String[] productId) {
        ArrayList<Object> list = new ArrayList<>();
        for (String id : productId) {
            // 数据处理
            list.add(id);
        }
        return list;
    }

    // 路径
    @GetMapping("/path/{id}")
    public Object testId(@PathVariable String id, @RequestParam String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }

    // @RequestParam 单个参数接收------前端使用FromData
    @PostMapping("/post")
    public Object testPost(@RequestParam String name, @RequestParam int age) {
        Map<String, Object> result = new HashMap<>();
        // 数据处理
        result.put("name", name);
        result.put("age", age);
        return result;
    }

    // 实体类接收：不加注解------前端使用FromData
    @PostMapping("/post22")
    public Object testPost22(Test data) {
        Map<String, Object> result = new HashMap<>();
        // 数据处理
        result.put("name", data.getName());
        result.put("age", data.getAge());
        return result;
    }

    // @RequestBody 实体类接收
    @PostMapping("/post23")
    public Object testPost23(@RequestBody Test data) {
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

    // 数组接收, map里面还有数组类型
    @PostMapping("/post5")
    public Object testPost5(@RequestBody List<TestList> testList) {
        return testList;
    }


    // 获取请求头
    @RequestMapping("/header")
    public Object getHeader(@RequestHeader(name = "privateHeader") String privateHeader, @RequestHeader(name = "privateHeader2") String privateHeader2) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("privateHeader", privateHeader);
        map.put("privateHeader2", privateHeader2);
        return map;
    }

    // 获取所有的请求头
    @RequestMapping("/headers")
    public Object getAllHeader(HttpServletRequest request) {
        Map<Object, Object> resultHeader = new HashMap<>();
        resultHeader.put("privateHeader", request.getHeader("privateHeader"));
        resultHeader.put("privateHeader2", request.getHeader("privateHeader2"));
        resultHeader.put("privateHeader3", request.getHeader("privateHeader3"));
        return resultHeader;
    }

    // 获取Cookie
    @RequestMapping("/cookie")
    public Object getCookie(@CookieValue(name = "token") String token) {
        return token;
    }


}

