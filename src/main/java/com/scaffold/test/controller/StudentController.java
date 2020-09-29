package com.scaffold.test.controller;


import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.entity.Student;
import com.scaffold.test.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
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
    public List<Student> getAll() {
        return studentService.findAll();
    }

    @GetMapping("add")
    public String addStudent(@Validated Student student) {
//        if(bindingResult.hasErrors()){
//            if(bindingResult.hasErrors()){
//                for (ObjectError error: bindingResult.getAllErrors()) {
//                    log.error(error.getDefaultMessage());
//                    return error.getDefaultMessage();
//                }
//            }
//        }
        return studentService.saveStudent(student);
    }

    /**
     * 添加学生
     * @param student
     * @return
     */
    @PostMapping("post")
    public Result postStudent(@Validated @RequestBody Student student) {
        return ResultGenerator.setSuccessResult(student);
    }

    @GetMapping("find")
    public Student findStudent(Student student) {
        return studentService.findStudent(student);
    }

    @GetMapping("delete")
    public void deleteStudent(Student student) {
        studentService.deleteStudent(student);
    }

    @GetMapping("test")
    public Student test(@RequestParam String text) {
        return studentService.testStudent(text);
    }

}

