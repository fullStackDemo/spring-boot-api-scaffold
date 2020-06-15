package com.scaffold.test.controller;

import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.entity.Mail;
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

    // 发送不带格式的文本
    @GetMapping("post")
    public Result postMail() {
        Mail mail = new Mail();
        mail.setTo("1498097245@qq.com");
        mail.setSubject("automatic");
        mail.setContent("自动邮件发布");
        mailService.sendMail(mail);
        return ResultGenerator.getSuccessResult().setMessage("发送成功");
    }

}
