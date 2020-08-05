package com.scaffold.test.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.entity.Weather;
import com.scaffold.test.mapper.WeatherMapper;
import com.scaffold.test.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-06-18
 */

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherMapper weatherMapper;

    @Override
    public List<Weather> getWeekList(Document document) {

        ArrayList<Weather> weathers = new ArrayList<>();

        /*
         * 新版天气
         */
        // 时间列表
        Elements dayLists = document.getElementsByClass("date-container").get(0).getElementsByTag("li");
        // 天气状态列表
        Elements blueLists = document.getElementsByClass("blue-container").get(0).getElementsByTag("li");
        // 获取温度
        Elements scriptList = document.getElementsByTag("script");
        // 温度数据
        String tempData = scriptList.get(6).toString();
        // 最高气温数据字符串
        String tempDataHigh = tempData.replace(" ", "").split("eventDay=")[1].split(";\n" + "vareventNight")[0];
        // 最低气温数据字符串
        String tempDataLow = tempData.replace(" ", "").split("vareventNight=")[1].split(";\n" + "varfifDay=")[0];
        // 最高气温数组数据
        JSONArray tempDataHighArr = JSONObject.parseArray(tempDataHigh);
        // 最低气温数组数据
        JSONArray tempDataLowArr = JSONObject.parseArray(tempDataLow);

        // 计数
        int currentIndex = 0;
        // 时间数组
//        ArrayList<Weather> days = new ArrayList<>();
        for (Element dayLi : dayLists) {
            // 日期：4日
            String day = dayLi.getElementsByClass("date").text().split("\\D{1,2}")[0];
            // 获取日期
            String date = getDate(day);
            // 名字：今天
            String name = dayLi.getElementsByClass("date-info").text();
            currentIndex += 1;
            // 不查询历史，有时候第一个数据是昨天的数据
            int currentDay = new Date().getDate();
            if (currentIndex == 1 && currentDay != Integer.parseInt(day)) {
                continue;
            }
            // 天气情况：暴雨
            String status = blueLists.get(currentIndex - 1).getElementsByClass("weather-info").text();
            Weather weather = new Weather();
            weather.setName(name);
            weather.setDate(date);
            weather.setStatus(status);

            // 最高最低气温
            weather.setMax(String.valueOf(tempDataHighArr.get(currentIndex - 1)));
            weather.setMin(String.valueOf(tempDataLowArr.get(currentIndex - 1)));
            weathers.add(weather);
        }
        System.out.println(weathers);


        /*
         * 旧版天气
         */
        // 数据提取
//        Elements sevenBox = document.getElementById("7d").getElementsByClass("t");
//        Elements liColumns = sevenBox.get(0).getElementsByTag("li");
//        // 计数
//        int currentIndex = 0;
//        for (Element column : liColumns) {
//            Weather weather = new Weather();
//            // 获取 name
//            String name = column.getElementsByTag("h1").text().split("（")[1].split("）")[0];
//            String day = column.getElementsByTag("h1").text().split("\\D{1,2}")[0];
//            String date = getDate(day);
//            // 今日天气状态
//            String status = column.getElementsByClass("wea").text();
//            // 温度
//            String tem = column.getElementsByClass("tem").text();
//            String[] temArr = tem.split("\\D{1,2}");
//            // 最高气温
//            String maxTem;
//            // 最低气温
//            String minTem;
//            if (temArr.length == 2) {
//                maxTem = tem.split("\\D{1,2}")[0];
//                minTem = tem.split("\\D{1,2}")[1];
//            } else {
//                maxTem = tem.split("\\D{1,2}")[0];
//                minTem = tem.split("\\D{1,2}")[0];
//            }
//            // 获取今天以后分时间天气
//            int currentDay = new Date().getDate();
//            currentIndex += 1;
//            // 不查询历史，有时候第一个数据是昨天的数据
//            if(currentIndex == 1 && currentDay != Integer.parseInt(day)){
//                continue;
//            }
//
//            weather.setName(name);
//            weather.setStatus(status);
//            weather.setDate(date);
//            weather.setMax(maxTem);
//            weather.setMin(minTem);
//
//            weathers.add(weather);
//        }

        // 插入数据库
        for (Weather weather : weathers) {
            weatherMapper.insertWeather(weather);
        }

        return weathers;
    }

    @Override
    public List<Weather> selectAll() {
        return weatherMapper.selectAll();
    }

    /**
     * 获取对应日期
     *
     * @param day 第几天
     * @return
     */
    public String getDate(String day) {
        Date date = new Date();
        int today = date.getDate();
        int currentMonth = date.getMonth();
        //格式日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 进入下一月
        if (today > Integer.parseInt(day)) {
            date.setMonth(currentMonth + 1);
        }
        date.setDate(Integer.parseInt(day));
        return dateFormat.format(date);
    }
}
