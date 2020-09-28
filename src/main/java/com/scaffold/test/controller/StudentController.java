package com.scaffold.test.controller;


import com.scaffold.test.entity.Student;
import com.scaffold.test.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author alex wong
 */

@Slf4j
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
//    public void addStudent(Student student){
    public String addStudent(@Valid Student student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            if(bindingResult.hasErrors()){
                for (ObjectError error: bindingResult.getAllErrors()) {
                    log.error(error.getDefaultMessage());
                    return error.getDefaultMessage();
                }
            }
        }
        return studentService.saveStudent(student);
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

