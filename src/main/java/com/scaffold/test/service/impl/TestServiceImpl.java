package com.scaffold.test.service.impl;

import com.scaffold.test.entity.Test;
import com.scaffold.test.mapper.TestMapper;
import com.scaffold.test.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-06-14
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

}
