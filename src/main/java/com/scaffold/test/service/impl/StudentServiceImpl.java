package com.scaffold.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.Student;
import com.scaffold.test.mapper.StudentMapper;
import com.scaffold.test.service.StudentService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private StudentMapper studentMapper;

    @Override
    @Cacheable(value = "cacheData")
    public List<Student> findAll(){
        return studentMapper.selectAll();
    }

    @Override
    @Cacheable(value = "cacheData")
    public Student findStudent(Student student) {
        int id = student.getId();
        if(id == 0){
            return null;
        }
        return studentMapper.findStudent(student);
    }
}
