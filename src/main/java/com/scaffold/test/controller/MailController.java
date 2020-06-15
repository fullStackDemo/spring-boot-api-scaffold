package com.scaffold.test.controller;

import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("post")
    public Result postMail(){
        mailService.sendMail("749856591@qq.com", "automatic", "你猜我是谁？来自自动邮件发布");
        return ResultGenerator.getSuccessResult().setMessage("发送成功");
    }

}
