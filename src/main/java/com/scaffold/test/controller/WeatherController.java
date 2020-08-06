package com.scaffold.test.controller;


import com.alibaba.fastjson.JSONArray;
import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.constants.BaseApplication;
import com.scaffold.test.entity.Mail;
import com.scaffold.test.entity.Weather;
import com.scaffold.test.entity.WeatherTime;
import com.scaffold.test.service.MailService;
import com.scaffold.test.service.WeatherService;
import com.scaffold.test.service.WeatherTimeService;
import com.scaffold.test.utils.SystemUtils;
import org.apache.commons.lang3.StringUtils;
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


/**
 * @author alex
 */

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);

    private static final int times = 60 * 60 * 1000;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MailService mailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private WeatherTimeService weatherTimeService;

    @Autowired
    private BaseApplication baseApplication;

    /**
     * 定时获取七日天气数据
     */
    @Async
    @Scheduled(fixedRate = times)
    @GetMapping("/get")
    public void getDataFromHtml() {
        // String url = "http://www.weather.com.cn/weather/101020100.shtml";

        // 新版
        // 七日
        String url_day7 = "http://www.weather.com.cn/weathern/101020100.shtml";
        // 当天分时数据
        String url_time = "http://www.weather.com.cn/weather1dn/101020100.shtml";
        log.info("-------定时获取七日天气数据--------");
        try {
            // 1 只能获取静态数据
            Document document = Jsoup.connect(url_day7).get();
            Document timeDocument = Jsoup.connect(url_time).get();


            // 同步7日天气
            weatherService.getWeekList(document);

            //  获取分时数据
            // JSONObject timeList = JSONObject.parseObject(document.getElementsByTag("script").get(5).toString().split("hour3data=")[1].replace("</script>", ""));
            // weatherTimeService.getSevenDayTime(timeList);
            // 分时数据

//            String hourDataScript = timeDocument.getElementsByTag("script").get(7).toString();
//            // hourData字符串
//            String hourDataStr = hourDataScript.replace(" ", "").split("varhour3data=")[1].split(";varhour3week")[0];
//            JSONArray hourDataArr = JSONObject.parseArray(hourDataStr);

            // 分时数据
            weatherTimeService.insertCurrentTime();


        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 定时发送邮件
    @Async
    @Scheduled(cron = "0 0 6,12,18 * * ?")
    public void sendWeatherMail() throws MessagingException {
        sendMail("");
    }


    /**
     * 自定义邮件发送地址
     * @param mailAddress 1328@qq.com,6666@qq.com
     * @throws MessagingException
     */
    @GetMapping("post")
    @PassToken
    public String sendWeatherMail2(String mailAddress) throws MessagingException {
        sendMail(mailAddress);
        return "手动订阅天气, 邮件已发送，请查收";
    }

    /**
     * 发送邮件方法
     * @throws MessagingException
     */
    public void sendMail(String mailAddress) throws MessagingException {
        log.info("-------定时发送邮件--------");
        Context context = new Context();
        // 获取七日天气
        List<Weather> weathers = weatherService.selectAll();

        // 获取当天的分时数据
        String currentDate = SystemUtils.getDateFromToday(0);
        String currentDateNext = SystemUtils.getDateFromToday(1);
        String currentDateNext2 = SystemUtils.getDateFromToday(2);
        int currentHour = SystemUtils.getCurrentHour();
        List<WeatherTime> weatherTimeList = weatherTimeService.getCurrentDateTime(currentDate, currentHour);
        // 获取当天天气状况ICON
        String todayStatusIcon = "";
        for (Weather weather : weathers) {
            String date = weather.getDate();
            if (date.equals(currentDate)) {
                todayStatusIcon = weather.getIcon();
                weather.setDate("今天");
            } else if (date.equals(currentDateNext)) {
                weather.setDate("明天");
            } else if (date.equals(currentDateNext2)) {
                weather.setDate("后天");
            }
            weather.setWeek(SystemUtils.getWeek(date));
        }
        // 七日天气
        context.setVariable("resultList", weathers);
        // 今日分时天气
        context.setVariable("weatherTimeList", weatherTimeList);
        // 今天天气状况图标
        context.setVariable("todayStatusIcon", todayStatusIcon);
        String emailTemplate = templateEngine.process("weather", context);

        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());

        // 邮件发送, 多人接收
        // String[] addressList = {"1498097245@qq.com", "749856591@qq.com"};
        String[] addressList = baseApplication.getMailRecipient().split(",");

        // 判断自定义邮箱地址
        if(StringUtils.isNoneBlank(mailAddress)){
            addressList = mailAddress.split(",");
        }

        log.info("邮件接收：" +JSONArray.toJSONString(addressList));

        for (String address : addressList) {
            Mail mail = new Mail();
            mail.setTo(address);
            mail.setSubject("天气小管家" + currentTime);
            mail.setContent(emailTemplate);
            mailService.sendHtmlMail(mail);
        }
    }


}
