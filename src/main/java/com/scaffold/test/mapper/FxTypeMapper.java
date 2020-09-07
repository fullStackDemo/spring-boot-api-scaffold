package com.scaffold.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scaffold.test.entity.FxType;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-09-07
 */
public interface FxTypeMapper extends BaseMapper<FxType> {

    int insertType(FxType fxType);

    List<FxType> getCcyPairType(FxType fxType);

}
