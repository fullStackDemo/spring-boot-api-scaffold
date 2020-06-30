package com.scaffold.test.service.impl;

import com.scaffold.test.entity.Student;
import com.scaffold.test.service.ExcelService;
import com.scaffold.test.utils.ExcelUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {



    @Override
    public void excelExport(List<Student> list, HttpServletResponse response) {

        XSSFWorkbook sheets = ExcelUtils.createWorkBook("统计表");


    }
}
