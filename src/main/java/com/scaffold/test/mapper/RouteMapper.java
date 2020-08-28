package com.scaffold.test.mapper;

import com.scaffold.test.entity.Route;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
public interface RouteMapper extends BaseMapper<Route> {

    int insertRoute(Route route);
}
