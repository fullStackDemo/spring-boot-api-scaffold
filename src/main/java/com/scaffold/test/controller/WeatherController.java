package com.scaffold.test.controller;


import com.scaffold.test.entity.Mail;
import com.scaffold.test.entity.Weather;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(JobTask.class);

    private static final int times = 60 * 60 * 1000;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MailService mailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    // 定时获取七日天气数据
    @Async
    @Scheduled(fixedRate = times)
    @GetMapping("/get")
    public void getDataFromHtml() {
        String url = "http://www.weather.com.cn/weather/101020100.shtml";
        log.info("-------定时获取七日天气数据--------");
        try {
            Document document = Jsoup.connect(url).get();
            weatherService.getWeekList(document);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 定时发送邮件
    @Async
//    @Scheduled(fixedRate = times)
    @GetMapping("post")
    public void sendMail() throws MessagingException {
        Context context = new Context();
        // 获取七日天气
        List<Weather> weathers = weatherService.selectAll();
        context.setVariable("resultList", weathers);
//        String emailTemplate = templateEngine.process("weatherTemplate", context);
        String emailTemplate = templateEngine.process("weather", context);

        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());

        // 邮件发送, 多人接收
//        String[] addressList = {"1498097245@qq.com", "749856591@qq.com"};
        String[] addressList = {"1498097245@qq.com"};
        for (String address : addressList) {
            Mail mail = new Mail();
            mail.setTo(address);
            mail.setSubject("天气预报小助手-" + currentTime);
            mail.setContent(emailTemplate);
            mailService.sendHtmlMail(mail);
        }
    }


}
