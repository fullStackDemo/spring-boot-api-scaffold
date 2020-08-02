package com.scaffold.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class WeatherServiceImpl extends ServiceImpl<WeatherMapper, Weather> implements WeatherService {

    @Autowired
    private WeatherMapper weatherMapper;

    @Override
    public List<Weather> getWeekList(Document document) {

        ArrayList<Weather> weathers = new ArrayList<>();

        // 数据提取
        Elements sevenBox = document.getElementById("7d").getElementsByClass("t");
        Elements liColumns = sevenBox.get(0).getElementsByTag("li");
        // 计数
        int currentIndex = 0;
        for (Element column : liColumns) {
            Weather weather = new Weather();
            // 获取 name
            String name = column.getElementsByTag("h1").text().split("（")[1].split("）")[0];
            String day = column.getElementsByTag("h1").text().split("\\D{1,2}")[0];
            String date = getDate(day);
            // 今日天气状态
            String status = column.getElementsByClass("wea").text();
            // 温度
            String tem = column.getElementsByClass("tem").text();
            String[] temArr = tem.split("\\D{1,2}");
            // 最高气温
            String maxTem;
            // 最低气温
            String minTem;
            if (temArr.length == 2) {
                maxTem = tem.split("\\D{1,2}")[0];
                minTem = tem.split("\\D{1,2}")[1];
            } else {
                maxTem = tem.split("\\D{1,2}")[0];
                minTem = tem.split("\\D{1,2}")[0];
            }
            // 获取今天以后分时间天气
            int currentDay = new Date().getDate();
            currentIndex += 1;
            // 不查询历史，有时候第一个数据是昨天的数据
            if(currentIndex == 1 && currentDay != Integer.parseInt(day)){
                continue;
            }

            weather.setName(name);
            weather.setStatus(status);
            weather.setDate(date);
            weather.setMax(maxTem);
            weather.setMin(minTem);

            weathers.add(weather);
        }

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
