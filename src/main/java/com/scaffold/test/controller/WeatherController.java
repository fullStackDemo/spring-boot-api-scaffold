package com.scaffold.test.controller;


import com.scaffold.test.entity.Mail;
import com.scaffold.test.service.MailService;
import com.scaffold.test.service.WeatherService;
import com.scaffold.test.task.JobTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(JobTask.class);

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MailService mailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    // 定时获取七日天气数据
    // @GetMapping("/")
    @Scheduled(fixedRate = 60000)
    @Async
    public void getDataFromHtml() {
        String url = "http://www.weather.com.cn/weather/101020100.shtml";
        try {
            Document document = Jsoup.connect(url).get();
            weatherService.getWeekList(document);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 定时发送邮件
    @Async
    @GetMapping("post")
    public void sendMail() throws MessagingException {
        Context context = new Context();
        Map<String, Object> emailParam = new HashMap<>();
        emailParam.put("name", "今日有雨");
        emailParam.put("date", "11");
        emailParam.put("status", "11");
        emailParam.put("max", 22);
        emailParam.put("min", 22);
        context.setVariable("emailParam", emailParam);
        String emailTemplate = templateEngine.process("weatherTemplate", context);

        Mail mail = new Mail();
        mail.setTo("1498097245@qq.com");
        mail.setSubject("天气预报");
        mail.setContent(emailTemplate);
        mailService.sendHtmlMail(mail);
    }



}
