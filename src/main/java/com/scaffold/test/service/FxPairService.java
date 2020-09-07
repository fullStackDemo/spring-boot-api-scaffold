package com.scaffold.test.service;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.entity.FxPair;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-09-07
 */
public interface FxPairService extends IService<FxPair> {

    List<JSONObject> getSellList();

    List<JSONObject> getBuyList(FxPair fxPair);

}
