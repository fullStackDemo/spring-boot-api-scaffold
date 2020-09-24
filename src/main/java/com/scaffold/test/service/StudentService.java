package com.scaffold.test.service;

import com.scaffold.test.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-06-14
 */
public interface StudentService extends IService<Student> {

    List<Student> findAll();

    Student findStudent(Student student);

    Student testStudent(String text);

}
