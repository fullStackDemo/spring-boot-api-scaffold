package com.scaffold.test.service.impl;

import com.scaffold.test.constants.BaseApplication;
import com.scaffold.test.entity.Student;
import com.scaffold.test.service.ExcelService;
import com.scaffold.test.utils.ExcelUtils;
import com.scaffold.test.utils.UUIDUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final String excelSuffix = ".xlsx";

    @Autowired
    private BaseApplication baseApplication;

    @Override
    public void excelExport(List<Student> list, HttpServletResponse response) throws IOException {

        XSSFWorkbook workbook = ExcelUtils.createWorkBook();
        XSSFSheet sheet = workbook.createSheet("统计表");


        // 表头thead
        XSSFRow row = sheet.createRow(0);
        //设置列宽，setDefaultColumnWidth的参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setDefaultColumnWidth(120 * 256);

        //设置为居中加粗
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("年龄");
        cell.setCellStyle(cellStyle);

        // 新增tbody数据行
        int rowNum = 1;
        for (Student student : list) {
            XSSFRow row2 = sheet.createRow(rowNum);
            row2.createCell(0).setCellValue(student.getName());
            row2.createCell(1).setCellValue(student.getAge());
            rowNum++;
        }

        String fileName = UUIDUtils.getUUID() + excelSuffix;
        String folderPath = baseApplication.getExportPath();
        // mac
        if (!System.getProperty("os.name").contains("Window")) {
            folderPath = baseApplication.getMacExportPath();
        }
        ExcelUtils.createExcel(fileName, folderPath, workbook, response);

    }
}
