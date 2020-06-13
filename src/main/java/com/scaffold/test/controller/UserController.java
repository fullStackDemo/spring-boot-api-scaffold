package com.scaffold.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scaffold.test.entity.User;
import com.scaffold.test.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/list")
    public List<User> getUserList() {
        return userMapper.selectList(null);
    }

    @PostMapping("/add")
    public Object addUser() {
        User user = new User();
        user.setUserName("wangz");
        user.setPassword("123444");
        user.setRemark("test");
        userMapper.insert(user);
        return "success";
    }

    @GetMapping("/findUserById")
    public User findUserById(@RequestParam int userId) {
        return userMapper.selectById(userId);
    }

    @GetMapping("/findUserByIds")
    public List<User> findUserByIds(@RequestParam String userIds) {
        String[] s = userIds.split(",");
        List<String> ids = Arrays.asList(s);
        System.out.println(ids);
        return userMapper.selectBatchIds(ids);
    }

    // SELECT * FROM user WHERE user_id = ?
    @GetMapping("/findUser")
    public List<User> findUser(@RequestParam int userId) {
        // selectMap
         Map<String, Object> map = new HashMap<>();
         map.put("user_id", userId);
         return userMapper.selectByMap(map);
    }

    @GetMapping("/findUsers")
    public List<User> findUsers() {
        // 条件查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "w");
        return userMapper.selectList(queryWrapper);
    }

}
