package com.scaffold.test.mapper;

import com.scaffold.test.entity.FxType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-09-07
 */
public interface FxTypeMapper extends BaseMapper<FxType> {

    int insertType(FxType fxType);

}
