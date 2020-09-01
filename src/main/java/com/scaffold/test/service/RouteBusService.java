package com.scaffold.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.Route;
import com.scaffold.test.entity.RouteBus;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-08-27
 */
public interface RouteBusService extends IService<RouteBus> {

    List<RouteBus> getBusList(Route route);

    /**
     * 获取现在路线中行驶的公交车信息
     * @param routeBus
     */
    List<RouteBus> getLiveBusStatus(RouteBus routeBus);

}
