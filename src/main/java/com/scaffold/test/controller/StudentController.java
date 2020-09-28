package com.scaffold.test.controller;


import com.scaffold.test.entity.Student;
import com.scaffold.test.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author alex wong
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("list")
    public List<Student> getAll(){
        return studentService.findAll();
    }

    @GetMapping("add")
    public void addStudent(Student student){
        studentService.saveStudent(student);
    }

    @GetMapping("find")
    public Student findStudent(Student student){
        return studentService.findStudent(student);
    }

    @GetMapping("delete")
    public void deleteStudent(Student student){
        studentService.deleteStudent(student);
    }

    @GetMapping("test")
    public Student test(@RequestParam String text){
        return studentService.testStudent(text);
    }

}

