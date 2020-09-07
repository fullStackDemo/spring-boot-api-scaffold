package com.scaffold.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.entity.FxPair;
import com.scaffold.test.mapper.FxPairMapper;
import com.scaffold.test.service.FxPairService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-09-07
 */
@Service
public class FxPairServiceImpl extends ServiceImpl<FxPairMapper, FxPair> implements FxPairService {

    @Autowired
    FxPairMapper fxPairMapper;

    @Override
    public List<JSONObject> getSellList() {
        List<FxPair> sellList = fxPairMapper.getSellList();
        List<JSONObject> result = new ArrayList<>();
        // 数据处理
        for(FxPair fxPair: sellList){
            JSONObject object = new JSONObject();
            object.put("label", fxPair.getBuyCcy() + "-99");
            object.put("value", fxPair.getBuyCcy());
            result.add(object);
        }
        return result;
    }

    @Override
    public List<JSONObject> getBuyList(FxPair fxPair) {
        List<FxPair> buyList = fxPairMapper.getBuyList(fxPair);
        List<JSONObject> result = new ArrayList<>();
        // 数据处理
        for(FxPair data: buyList){
            JSONObject object = new JSONObject();
            object.put("label", data.getBuyCcy() + "-99");
            object.put("value", data.getBuyCcy());
            result.add(object);
        }
        return result;
    }
}
