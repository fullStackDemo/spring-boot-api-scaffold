package com.scaffold.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.FxType;
import com.scaffold.test.mapper.FxTypeMapper;
import com.scaffold.test.service.FxTypeService;
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
public class FxTypeServiceImpl extends ServiceImpl<FxTypeMapper, FxType> implements FxTypeService {

    @Autowired
    FxTypeMapper fxTypeMapper;

    @Override
    public List<JSONObject> getCcyPairType(FxType fxType) {
        List<FxType> buyList = fxTypeMapper.getCcyPairType(fxType);
        List<JSONObject> result = new ArrayList<>();
        // 数据处理
        for(FxType data: buyList){
            JSONObject object = new JSONObject();
            object.put("label", data.getCcyType());
            object.put("value", data.getCcyType());
            result.add(object);
        }
        return result;
    }
}
