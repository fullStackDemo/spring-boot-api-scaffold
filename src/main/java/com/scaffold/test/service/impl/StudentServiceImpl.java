package com.scaffold.test.service.impl;

import com.scaffold.test.entity.Student;
import com.scaffold.test.mapper.StudentMapper;
import com.scaffold.test.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMappper;

    @Override
    public List<Student> findAll() {
        return studentMappper.selectAll();
    }
}
