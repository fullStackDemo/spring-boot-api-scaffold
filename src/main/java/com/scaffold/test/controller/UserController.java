package com.scaffold.test.controller;

import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.entity.User;
import com.scaffold.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService UserService;


    @PostMapping("/add")
    public Result addUser(User user) {
        int flag = UserService.insertUser(user);
        if (flag == 1) {
            return ResultGenerator.setSuccessResult();
        } else {
            return ResultGenerator.setFailResult("注册失败, 用户已存在");
        }
    }


}
