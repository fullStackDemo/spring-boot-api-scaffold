package com.scaffold.test.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.HttpParams;
import com.scaffold.test.entity.Route;
import com.scaffold.test.entity.RouteBus;
import com.scaffold.test.entity.RouteStop;
import com.scaffold.test.mapper.RouteBusMapper;
import com.scaffold.test.service.RouteBusService;
import com.scaffold.test.service.RouteStopService;
import com.scaffold.test.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
@Service
public class RouteBusServiceImpl extends ServiceImpl<RouteBusMapper, RouteBus> implements RouteBusService {

    @Autowired
    RouteStopService routeStopService;

    @Autowired
    RouteBusMapper routeBusMapper;

    @Override
    public List<RouteBus> getBusList(Route route) {
        // 获取当前路线数据
        String routeCode = route.getRouteCode();
        List<RouteStop> currentRouteStopList = routeStopService.findCurrentRouteStopList(routeCode);
        // 获取最后一站
        RouteStop lastStop = null;
        // 关注的站点
        RouteStop attentionStop = null;
        if (currentRouteStopList.size() > 0) {
            lastStop = currentRouteStopList.get(currentRouteStopList.size() - 1);
            for (RouteStop stop : currentRouteStopList) {
                if (stop.getIsAttention().equals("1")) {
                    attentionStop = stop;
                    break;
                }
            }
        }
        // 拼接数据, 关注的站点和终点站
        JSONArray dataArr = new JSONArray(2);
        for (int i = 0; i < 2; i++) {
            JSONObject object = new JSONObject();
            object.put("type", 1);
            object.put("scene", 1);
            object.put("route_code", routeCode);
            if (i == 0) {
                object.put("stop_code", lastStop.getStopCode());
            } else {
                object.put("stop_code", attentionStop.getStopCode());
            }
            dataArr.add(object);
        }
        String dataStr = dataArr.toString();


        // 请求数据
        HttpParams httpParams = new HttpParams();
        httpParams.setRequestUrl("https://ccmcx.tenpay.com/cgi-bin/ccmcx/ccmcx_route_bus.cgi");
        // 请求头
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", "wlx_skey=yx59CAAAAAEAAAAC6rQQXlJXm8ldJ8bGE4+voZHnNLwv9GfpVyDkhngPAIlfzUTfcgrNbNSg2ToCCCexjbnG38CnSO9WP+QkaG3PlA==; wlx_skey_type=1; wlx_open_id=oJaMK0b3TE5Cs7elHAM76cACqLqA; wlx_app_id=wxbb58374cdce267a6; version=3.13.5; s_tk=cbc5");
        httpParams.setRequestHeader(header);
        // 参数
        Map<String, Object> params = new HashMap<>();
        params.put("s_tk", "cbc5");
        params.put("from", "app");
        params.put("version", "3.13.5");
        params.put("city_code", 310000);
        params.put("user_latitude", 31.229246139526367);
        params.put("user_longitude", 121.5271224975586);
        params.put("req_from", "ccm_minipro");
        params.put("g_tk", 459517461);
        params.put("wlx_app_id", "wxbb58374cdce267a6");
        params.put("wlx_open_id", "oJaMK0b3TE5Cs7elHAM76cACqLqA");
        params.put("wlx_skey", "yx59CAAAAAEAAAAC6rQQXlJXm8ldJ8bGE4+voZHnNLwv9GfpVyDkhngPAIlfzUTfcgrNbNSg2ToCCCexjbnG38CnSO9WP+QkaG3PlA==");
        params.put("wlx_skey_type", 1);
        params.put("data", dataStr);
        httpParams.setRequestParams(params);
        httpParams.setRequestParamsType("formUrl");
        String responseDataStr = HttpUtils.post(httpParams);
        JSONObject responseData = JSONObject.parseObject(responseDataStr);

        // 行驶中的车辆信息
        List<RouteBus> liveBusList = new ArrayList<>();
        JSONArray routeList = (JSONArray) responseData.get("route_list");
        if (routeList.size() > 0) {
            for (Object routeObj : routeList) {
                JSONObject routeInfo = (JSONObject) routeObj;
                JSONArray busList = (JSONArray) routeInfo.get("bus_list");
                for (Object o : busList) {
                    JSONObject busInfo = (JSONObject) o;
                    // 路线数据
                    RouteBus routeBus = new RouteBus();
                    routeBus.setRouteCode(routeInfo.getString("route_code"));
                    routeBus.setStopCodeRefer(routeInfo.getString("stop_code"));
                    routeBus.setStopNameRefer(routeInfo.getString("stop_name"));
                    routeBus.setDistance(busInfo.getString("distance"));
                    routeBus.setDuration(busInfo.getString("duration"));
                    routeBus.setLatitude(busInfo.getString("latitude"));
                    routeBus.setLongitude(busInfo.getString("longitude"));
                    routeBus.setNextStopCode(busInfo.getString("next_stop_code"));
                    routeBus.setNextStopName(busInfo.getString("next_stop_name"));
                    routeBus.setNextStopIndex(busInfo.getString("next_stop_index"));
                    routeBus.setState(busInfo.getString("state"));
                    routeBus.setStopNum(busInfo.getString("stop_num"));
                    routeBus.setStopIndex(busInfo.getString("stop_index"));
                    liveBusList.add(routeBus);
                }
            }
        }

        for(RouteBus routeBus: liveBusList){
            routeBusMapper.insertRouteBus(routeBus);
        }

        return liveBusList;
    }

    @Override
    public void insertRouteBus(RouteStop routeStop) {}
}
