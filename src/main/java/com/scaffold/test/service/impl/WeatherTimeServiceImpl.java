package com.scaffold.test.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.WeatherTime;
import com.scaffold.test.mapper.WeatherTimeMapper;
import com.scaffold.test.service.WeatherTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 分时数据处理
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */

@Slf4j
@Service
public class WeatherTimeServiceImpl extends ServiceImpl<WeatherTimeMapper, WeatherTime> implements WeatherTimeService {


    @Autowired
    WeatherTimeMapper weatherTimeMapper;

    @Override
    public List<WeatherTime> getSevenDayTime(JSONObject day7Data) {
        // 获取分时数据
        log.info("-------------获取分时数据---------------");
        JSONArray hourData = (JSONArray) day7Data.get("7d");
        List<WeatherTime> weatherTimeList = new ArrayList<>();
        hourData.forEach(arr -> {
            JSONArray singleData = JSONArray.parseArray(arr.toString());
            singleData.forEach(m -> {
                String dataString = ((String) m).replace("日", ",");
                String[] dataArr = dataString.split(",");
                WeatherTime weatherTime = new WeatherTime();
                System.out.println(JSONArray.toJSONString(dataArr));
                for (int i = 0; i < dataArr.length; i++) {
                    switch (i) {
                        case 0:
                            weatherTime.setDate(getDate(dataArr[i]));
                            break;
                        case 1:
                            weatherTime.setTime(dataArr[i].split("时")[0]);
                            break;
                        case 3:
                            weatherTime.setStatus(dataArr[i]);
                            break;
                        case 4:
                            weatherTime.setTemp(dataArr[i]);
                            break;
                        default:
                            break;
                    }
                }
                weatherTime.setFlag(weatherTime.getDate() + "-" + weatherTime.getTime());
                weatherTimeList.add(weatherTime);
                log.info(JSONObject.toJSONString(weatherTime));
            });
        });

        // 插入数据库
        for (WeatherTime time : weatherTimeList) {
            weatherTimeMapper.insertTime(time);
        }

        return weatherTimeList;
    }

    @Override
    public List<WeatherTime> getCurrentDateTime(String date, String hour) {
        return weatherTimeMapper.findTimeByDate(date, hour);
    }

    /**
     * 获取对应日期
     * @param day
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
