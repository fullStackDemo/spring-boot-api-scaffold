package com.scaffold.test.controller;


import com.scaffold.test.service.WeatherService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public void getDataFromHtml() {
        String url = "http://www.weather.com.cn/weather/101020100.shtml";
        try {
            Document document = Jsoup.connect(url).get();
            weatherService.getWeekList(document);
            // 数据提取解析
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
