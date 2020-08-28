package com.scaffold.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scaffold.test.entity.RouteStop;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
public interface RouteStopMapper extends BaseMapper<RouteStop> {

    int insertRoute(RouteStop routeStop);

    List<RouteStop> findRouteStopList(String routeCode);

}
