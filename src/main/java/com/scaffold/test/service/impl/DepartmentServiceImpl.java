package com.scaffold.test.service.impl;

import com.scaffold.test.entity.Department;
import com.scaffold.test.mapper.DepartmentMapper;
import com.scaffold.test.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-07-29
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public int insert(Department department) {
        return departmentMapper.insertDepartment(department);
    }
}
