package com.scaffold.test.service;

import com.scaffold.test.entity.FxRate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-09-04
 */
public interface FxRateService extends IService<FxRate> {

    void readExcel();
}
