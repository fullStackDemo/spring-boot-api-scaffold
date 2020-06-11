package com.alex.service.impl;

import com.alex.dao.StudentMapper;
import com.alex.entity.Student;
import com.alex.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    public List<Student> findAll() {
        return studentMapper.selectAll();
    }
}
