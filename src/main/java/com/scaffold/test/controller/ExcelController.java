package com.scaffold.test.controller;

import com.scaffold.test.entity.Student;
import com.scaffold.test.service.ExcelService;
import com.scaffold.test.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ExcelService excelService;


    @GetMapping("export")
    public void export(Map<String, Object> params, HttpServletResponse response) throws IOException {
        List<Student> list = studentService.findAll();
//        ExcelUtils.createExcel(list, response);
        excelService.excelExport(list, response);
    }
}
