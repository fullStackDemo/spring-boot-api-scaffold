package com.scaffold.test.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scaffold.test.entity.FxType;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-09-07
 */
public interface FxTypeService extends IService<FxType> {

    List<JSONObject> getCcyPairType(FxType fxType);

}
