package com.scaffold.test.mapper;

import com.scaffold.test.entity.FxPair;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-09-07
 */
public interface FxPairMapper extends BaseMapper<FxPair> {

    int insertPair(FxPair fxPair);

}
