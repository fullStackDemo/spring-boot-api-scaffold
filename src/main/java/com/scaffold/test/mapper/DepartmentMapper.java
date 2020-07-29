package com.scaffold.test.mapper;

import com.scaffold.test.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-07-29
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    int insertDepartment(Department department);

}
