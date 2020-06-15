package com.scaffold.test.controller;

import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.entity.Mail;
import com.scaffold.test.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    // 发送不带格式的文本
    @Async
    @GetMapping("post")
    public Result postMail() {
        Mail mail = new Mail();
        mail.setTo("1498097245@qq.com");
        mail.setSubject("automatic");
        mail.setContent("自动邮件发布");
        mailService.sendMail(mail);
        return ResultGenerator.getSuccessResult().setMessage("发送成功");
    }

    // 发送Html邮件
    @Async
    @GetMapping("postHtml")
    public Result postHtmlMail() throws MessagingException {
        String content = "<html>\n" +
                "<body>\n" +
                "<h3>hello! test Html test!</h3>\n" +
                "</body>\n" +
                "</html>";
        Mail mail = new Mail();
        mail.setTo("1498097245@qq.com");
        mail.setSubject("Html格式邮件");
        mail.setContent(content);
        mailService.sendHtmlMail(mail);
        return ResultGenerator.getSuccessResult().setMessage("发送成功");
    }

    // 发送带附件的邮件
    @Async
    @GetMapping("postAttachment")
    public Result postAttachmentsMail() throws MessagingException {
        Mail mail = new Mail();
        mail.setTo("1498097245@qq.com");
        mail.setSubject("附件");
        mail.setContent("有附件，赶紧看下");
        mail.setFilePath("E:\\test.png");
        mailService.sendAttachmentsMail(mail);
        return ResultGenerator.getSuccessResult().setMessage("发送成功");
    }

    // 发送 Html 模板邮件
    @Async
    @GetMapping("postTemplate")
    public Result postTemplateMail() throws MessagingException {
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        context.setVariable("name", "alex wong");
        String emailTemplate = templateEngine.process("emailTemplate", context);

        Mail mail = new Mail();
        mail.setTo("1498097245@qq.com");
        mail.setSubject("模板邮件");
        mail.setContent(emailTemplate);
        mailService.sendHtmlMail(mail);
        return ResultGenerator.getSuccessResult().setMessage("发送成功");
    }

}
