package com.scaffold.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.Weather;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-06-18
 */
public interface WeatherService extends IService<Weather> {

    // HTML解析获取七日天气预报数据
    List<Weather> getWeekList(Document document);

    // 获取数据
    List<Weather> selectAll();
}
