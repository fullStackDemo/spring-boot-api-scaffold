package com.scaffold.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.Bus;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-08-26
 */
public interface BusService extends IService<Bus> {

    /**
     * 获取路线数据
     *
     * @return
     */
    Bus getRouteData();

}
