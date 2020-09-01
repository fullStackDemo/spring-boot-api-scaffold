package com.scaffold.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scaffold.test.entity.RouteBus;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
public interface RouteBusMapper extends BaseMapper<RouteBus> {

    int insertRouteBus(RouteBus routeBus);

    int deleteRouteBus(String routeCode);

    List<RouteBus> getLiveBusStatus(RouteBus routeBus);
}
