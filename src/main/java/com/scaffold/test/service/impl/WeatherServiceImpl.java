package com.scaffold.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.Weather;
import com.scaffold.test.mapper.WeatherMapper;
import com.scaffold.test.service.WeatherService;
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
            String maxTem = tem.split("\\D{1,2}")[0];
            String minTem = tem.split("\\D{1,2}")[1];
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

        return null;
    }

    @Override
    public List<Weather> selectAll() {
        return null;
    }

    // 获取对应日期
    public String getDate(String day) {
        Date date = new Date();
        //格式日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date.setDate(Integer.parseInt(day));
        return dateFormat.format(date);
    }
}
