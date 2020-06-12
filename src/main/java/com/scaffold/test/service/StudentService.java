package com.scaffold.test.service;

import com.scaffold.test.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {

    public List<Student> findAll();
}
