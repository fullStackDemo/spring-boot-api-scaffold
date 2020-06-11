package com.alex.controller;

import com.alex.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
//@RequestMapping("/api/student")
public class Student {
    @Autowired
    private StudentService studentService;

    @GetMapping("/api/student/list")
    public List<com.alex.entity.Student> getAll(){
        return studentService.findAll();
    }
}
