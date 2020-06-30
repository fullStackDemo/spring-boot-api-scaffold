package com.scaffold.test.service;

import com.scaffold.test.entity.Student;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExcelService {

    void excelExport(List<Student> list, HttpServletResponse response) throws Exception;

}
