package com.mp.test.service.impl;

import com.mp.test.entity.Student;
import com.mp.test.mapper.StudentMapper;
import com.mp.test.service.StudentService;
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
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
