package com.scaffold.test.service;

import com.scaffold.test.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-07-29
 */
public interface DepartmentService extends IService<Department> {

    int insert(Department department);
}
