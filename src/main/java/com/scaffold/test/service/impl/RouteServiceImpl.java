package com.scaffold.test.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.Bus;
import com.scaffold.test.entity.HttpParams;
import com.scaffold.test.entity.Route;
import com.scaffold.test.entity.RouteStop;
import com.scaffold.test.mapper.RouteMapper;
import com.scaffold.test.service.RouteService;
import com.scaffold.test.service.RouteStopService;
import com.scaffold.test.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */

@Slf4j
@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route> implements RouteService {

    @Autowired
    RouteStopService routeStopService;

    @Autowired
    RouteMapper routeMapper;

    @Override
    public Route getRouteData() {
        HttpParams httpParams = new HttpParams();
        httpParams.setRequestUrl("https://ccmcx.tenpay.com/cgi-bin/ccmcx/ccmcx_bus_route_detail.cgi");
        // 请求头
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", "wlx_skey=yx59CAAAAAEAAAAC6rQQXlJXm8ldJ8bGE4+voZHnNLwv9GfpVyDkhngPAIlfzUTfcgrNbNSg2ToCCCexjbnG38CnSO9WP+QkaG3PlA==; wlx_skey_type=1; wlx_open_id=oJaMK0b3TE5Cs7elHAM76cACqLqA; wlx_app_id=wxbb58374cdce267a6; version=3.13.5; s_tk=cbc5");
        httpParams.setRequestHeader(header);
        // 参数
        Map<String, Object> params = new HashMap<>();
        params.put("s_tk", "cbc5");
        params.put("from", "app");
        params.put("version", "3.13.5");
        params.put("auto_refresh", 1);
        params.put("polyline_qry", 2);
        params.put("route_code", "d3ulY7HyJmfscUBCvHDX4dBnV8Te5ZK4orhtzbSf0e0=");
        params.put("city_code", 310000);
        params.put("user_latitude", 31.229246139526367);
        params.put("user_longitude", 121.5271224975586);
        params.put("req_from", "ccm_minipro");
        params.put("g_tk", 459517461);
        params.put("wlx_app_id", "wxbb58374cdce267a6");
        params.put("wlx_open_id", "oJaMK0b3TE5Cs7elHAM76cACqLqA");
        params.put("wlx_skey", "yx59CAAAAAEAAAAC6rQQXlJXm8ldJ8bGE4+voZHnNLwv9GfpVyDkhngPAIlfzUTfcgrNbNSg2ToCCCexjbnG38CnSO9WP+QkaG3PlA==");
        params.put("wlx_skey_type", 1);
        params.put("source", 0);
        httpParams.setRequestParams(params);
        httpParams.setRequestParamsType("formUrl");
        String responseDataStr = HttpUtils.post(httpParams);
        JSONObject responseData = JSONObject.parseObject(responseDataStr);

        // 返回结果
        Route result = new Route();
        result.setRouteName(responseData.getString("route_name"));
        result.setRouteCode(responseData.getString("route_code"));
        result.setStartStopName(responseData.getString("start_stop_name"));
        result.setEndStopName(responseData.getString("end_stop_name"));
        result.setFirstTime(responseData.getString("first_time"));
        result.setLastTime(responseData.getString("last_time"));
        result.setPriceBase(responseData.getString("price_base"));

        result.setNearestStopDistance(responseData.getString("nearest_stop_distance"));
        result.setNearestStopDuration(responseData.getString("nearest_stop_duration"));
        // 获取各个站点列表
        JSONArray stopList = (JSONArray) responseData.get("stop_list");
        for (Object o : stopList) {
            JSONObject stop = (JSONObject) o;
            if (stop.get("stop_code").equals(responseData.getString("nearest_stop_code"))) {
                result.setNearestStopName(stop.getString("stop_name"));
            }
            // 插入数据
            RouteStop routeStop = new RouteStop();
            routeStop.setIsAttention(stop.getString("is_attention"));
            routeStop.setLatitude(stop.getString("latitude"));
            routeStop.setLongitude(stop.getString("longitude"));
            routeStop.setOrderNum(stop.getInteger("order_num"));
            routeStop.setStationId(stop.getString("station_id"));
            routeStop.setStopName(stop.getString("stop_name"));
            routeStop.setStopCode(stop.getString("stop_code"));
            routeStop.setRouteCode(result.getRouteCode());
            routeStop.setFlag(result.getRouteCode() + stop.getString("stop_code"));
            routeStopService.insertStop(routeStop);

        }
        log.info(JSONObject.toJSONString(result));

        // 入库
        routeMapper.insertRoute(result);
        return result;
    }
}
