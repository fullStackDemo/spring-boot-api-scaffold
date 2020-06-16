package com.scaffold.test.mapper;

import com.scaffold.test.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-06-14
 */
public interface StudentMapper extends BaseMapper<Student> {

    List<Student> selectAll();

}
