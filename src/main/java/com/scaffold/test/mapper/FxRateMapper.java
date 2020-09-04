package com.scaffold.test.mapper;

import com.scaffold.test.entity.FxRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-09-04
 */
public interface FxRateMapper extends BaseMapper<FxRate> {
    int insertRate(FxRate fxRate);
}
