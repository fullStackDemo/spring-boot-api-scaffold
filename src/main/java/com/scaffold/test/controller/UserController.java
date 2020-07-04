package com.scaffold.test.controller;

import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.entity.User;
import com.scaffold.test.service.UserService;
import com.scaffold.test.utils.BaseUtils;
import com.scaffold.test.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    // 注册
    @PostMapping("/add")
    public Result addUser(User user) {
        int flag = userService.insertUser(user);
        if (flag == 1) {
            return ResultGenerator.setSuccessResult();
        } else {
            return ResultGenerator.setFailResult("注册失败, 用户已存在");
        }
    }

    // 登录
    @PostMapping("/login")
    public Result userLogin(User user) {
        //验证码校验
        if (!userService.checkCode(user.getCode())) {
            return ResultGenerator.setFailResult("登录失败, 验证码不正确");
        }
        User userInfo = userService.findUser(user);
        if (userInfo != null) {
            HashMap<Object, Object> result = new HashMap<>();
            result.put("token", JWTUtils.createToken(userInfo));
            return ResultGenerator.setSuccessResult(result);
        } else {
            return ResultGenerator.setFailResult("登录失败, 请检查用户名和密码");
        }
    }

    // 获取用户信息
    @GetMapping("/info")
    public Result getUserInfo(){
        User currentUser = BaseUtils.getCurrentUser();
        return ResultGenerator.setSuccessResult(currentUser);
    }

    // 验证码校验
    @GetMapping("/checkCaptcha")
    public Result checkCode(@RequestParam String code) {
        if (userService.checkCode(code)) {
            return ResultGenerator.setSuccessResult("success");
        } else {
            return ResultGenerator.setFailResult("fail");
        }
    }




}
