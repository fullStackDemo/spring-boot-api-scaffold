package com.scaffold.test.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.entity.WeatherTime;
import com.scaffold.test.mapper.WeatherTimeMapper;
import com.scaffold.test.service.WeatherTimeService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
public class WeatherTimeServiceImpl implements WeatherTimeService {


    @Autowired
    WeatherTimeMapper weatherTimeMapper;

    /**
     * 旧版天气
     *
     * @param day7Data 七日数据
     * @return List
     */
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
                            weatherTime.setTime(Integer.parseInt(dataArr[i]));
                            break;
                        case 3:
                            weatherTime.setStatus(dataArr[i]);
                            break;
                        case 4:
                            weatherTime.setTemp(Integer.parseInt(dataArr[i]));
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

    /**
     * 新版天气
     * 获取当天逐小时分时数据
     *
     * @return List
     */
    @Override
    public void insertCurrentTime() {
        // 当天分时数据
        String url_time = "http://www.weather.com.cn/weather1dn/101020100.shtml";
        log.info("-------定时获取当天逐小时数据--------");
        List<WeatherTime> weatherTimeList = new ArrayList<>();

        // 分时数据变量数据
        // 天气状况变量
        JSONObject statusData = JSONObject.parseObject("{\"0\":\"晴\",\"1\":\"多云\",\"2\":\"阴\",\"3\":\"阵雨\",\"4\":\"雷阵雨\",\"5\":\"雷阵雨伴有冰雹\",\"6\":\"雨夹雪\",\"7\":\"小雨\",\"8\":\"中雨\",\"9\":\"大雨\",\"10\":\"暴雨\",\"11\":\"大暴雨\",\"12\":\"特大暴雨\",\"13\":\"阵雪\",\"14\":\"小雪\",\"15\":\"中雪\",\"16\":\"大雪\",\"17\":\"暴雪\",\"18\":\"雾\",\"19\":\"冻雨\",\"20\":\"沙尘暴\",\"21\":\"小到中雨\",\"22\":\"中到大雨\",\"23\":\"大到暴雨\",\"24\":\"暴雨到大暴雨\",\"25\":\"大暴雨到特大暴雨\",\"26\":\"小到中雪\",\"27\":\"中到大雪\",\"28\":\"大到暴雪\",\"29\":\"浮尘\",\"30\":\"扬沙\",\"31\":\"强沙尘暴\",\"32\":\"浓雾\",\"49\":\"强浓雾\",\"53\":\"霾\",\"54\":\"中度霾\",\"55\":\"重度霾\",\"56\":\"严重霾\",\"57\":\"大雾\",\"58\":\"特强浓雾\",\"97\":\"雨\",\"98\":\"雪\",\"99\":\"无\",\"301\":\"雨\",\"302\":\"雪\",\"00\":\"晴\",\"01\":\"多云\",\"02\":\"阴\",\"03\":\"阵雨\",\"04\":\"雷阵雨\",\"05\":\"雷阵雨伴有冰雹\",\"06\":\"雨夹雪\",\"07\":\"小雨\",\"08\":\"中雨\",\"09\":\"大雨\"}");
        // 风向变量数据
        JSONArray windData = JSONObject.parseArray("[\"无持续风向\",\"东北风\",\"东风\",\"东南风\",\"南风\",\"西南风\",\"西风\",\"西北风\",\"北风\",\"旋转风\"]");
        // 风等级变量数据
        JSONArray windLevelData = JSONObject.parseArray("[\"<3级\",\"3-4级\",\"4-5级\",\"5-6级\",\"6-7级\",\"7-8级\",\"8-9级\",\"9-10级\",\"10-11级\",\"11-12级\"]");

        try {
            // 1 只能获取静态数据
            Document document = Jsoup.connect(url_time).get();

            // 分时数据Script
            Elements hourDataScripts = document.getElementsByTag("script");
            // 遍历查找含有eventNight的文件
            // 温度数据
            String hourData = "";
            for (Element el: hourDataScripts) {
                if(el.toString().contains("hour3data")){
                    hourData = el.toString();
                    break;
                }
            }
            // hourData字符串
            String hourDataStr = hourData.replace(" ", "").split("varhour3data=")[1].split(";varhour3week")[0];
            JSONArray hourDataArr = JSONObject.parseArray(hourDataStr);

            // 七日全部分时数据：二维数组转一维数组
            ArrayList<JSONObject> mapList = new ArrayList<>();
            for (Object list : hourDataArr) {
                mapList.addAll((Collection<JSONObject>) list);
            }

            // 遍历数据
            for (JSONObject m : mapList) {
                System.out.println();
                WeatherTime weatherTime = new WeatherTime();
                // 日期时刻 2020080512
                String dateTime = m.getString("jf");
                String date = dateTime.substring(0, 4) + '-' + dateTime.substring(4, 6)+ '-' + dateTime.substring(6, 8);
                // 唯一标识
                weatherTime.setFlag(dateTime);
                // 时刻 12
                weatherTime.setTime(Integer.parseInt(dateTime.substring(8, 10)));
                // 天气状况
                weatherTime.setStatus(statusData.getString(m.getString("ja")));
                // 温度
                weatherTime.setTemp(Integer.parseInt(m.getString("jb")));
                // 日期
                weatherTime.setDate(date);
                // 风向
                weatherTime.setWind(windData.getString(Integer.parseInt(m.getString("jd"))));
                // 风力等级
                weatherTime.setWindLevel(windLevelData.getString(Integer.parseInt(m.getString("jc"))));

                weatherTimeList.add(weatherTime);
            }


            // 插入数据库
            for (WeatherTime time : weatherTimeList) {
                weatherTimeMapper.insertTime(time);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<WeatherTime> getCurrentDateTime(String date, int hour) {
        return weatherTimeMapper.findTimeByDate(date, hour);
    }

    /**
     * 获取对应日期
     *
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
