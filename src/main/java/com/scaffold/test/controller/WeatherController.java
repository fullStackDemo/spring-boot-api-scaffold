package com.scaffold.test.controller;


import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.entity.Mail;
import com.scaffold.test.entity.Weather;
import com.scaffold.test.entity.WeatherTime;
import com.scaffold.test.service.MailService;
import com.scaffold.test.service.WeatherService;
import com.scaffold.test.service.WeatherTimeService;
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

    // 定时获取七日天气数据
    @Async
    @Scheduled(fixedRate = times)
    @GetMapping("/get")
    public void getDataFromHtml() {
        String url = "http://www.weather.com.cn/weather/101020100.shtml";
//        String url = "http://www.weather.com.cn/weathern/101020100.shtml";
        log.info("-------定时获取七日天气数据--------");
        try {
            // 1 只能获取静态数据
            Document document = Jsoup.connect(url).get();

//            // 2 获取动态生成的数据
//            // 模拟谷歌Chrome浏览器的浏览器客户端对象
//            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
//            // 不启用CSS
//            webClient.getOptions().setCssEnabled(false);
//            // 启用JS
//            webClient.getOptions().setJavaScriptEnabled(false);
//            // 禁用当JS执行出错的时候是否抛出异常
//            webClient.getOptions().setThrowExceptionOnScriptError(false);
//            // 禁用当HTTP的状态非200时是否抛出异常
//            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//            // 设置支持AJAX
//            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//            webClient.getOptions().setDoNotTrackEnabled(false);
//            webClient.getOptions().setUseInsecureSSL(false);
//            // 获取页面
//            HtmlPage page = null;
//            try {
//                page = webClient.getPage(url);
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            } finally {
//                webClient.close();
//            }
//            // 设置js加载时间
//            webClient.waitForBackgroundJavaScript(30 * 1000);
//            // 使用xml的方式解析获取到jsoup的document对象
////            Document document = Jsoup.parse(page.asXml());
//
//
//            // 3 httpclient
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            CloseableHttpResponse response = null;
//            HttpGet request = new HttpGet(url);
//            try {
//                response = httpClient.execute(request);
//
//                // 判断响应状态为200，进行处理
//                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                    //5.获取响应内容
//                    HttpEntity httpEntity = response.getEntity();
//                    String html = EntityUtils.toString(httpEntity, "utf-8");
//                    System.out.println(html);
//                } else {
//                    //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
//                    System.out.println("返回状态不是200");
//                    System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            //  同步天气
            weatherService.getWeekList(document);

            //  获取分时数据
            JSONObject timeList = JSONObject.parseObject(document.getElementsByTag("script").get(5).toString().split("hour3data=")[1].replace("</script>", ""));
            weatherTimeService.getSevenDayTime(timeList);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 定时发送邮件
    @Async
//    @Scheduled(fixedRate = times)
    @Scheduled(cron = "0 30 6,12,18 * * ?")
    @GetMapping("post")
    public void sendMail() throws MessagingException {
        log.info("-------定时发送邮件--------");
        Context context = new Context();
        // 获取七日天气
        List<Weather> weathers = weatherService.selectAll();

        // 获取当天的分时数据
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<WeatherTime> weatherTimeList = weatherTimeService.getCurrentDateTime(currentDate);
        context.setVariable("weatherTimeList", weatherTimeList);
        context.setVariable("resultList", weathers);
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
            mail.setSubject("天气小管家" + currentTime);
            mail.setContent(emailTemplate);
            mailService.sendHtmlMail(mail);
        }
    }


}
