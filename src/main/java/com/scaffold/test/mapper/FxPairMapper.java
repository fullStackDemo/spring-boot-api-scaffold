package com.scaffold.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scaffold.test.entity.FxPair;

import java.util.List;

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

    // 货币对 左侧
    List<FxPair> getSellList();

    // 货币对 右侧
    List<FxPair> getBuyList(FxPair fxPair);

}
