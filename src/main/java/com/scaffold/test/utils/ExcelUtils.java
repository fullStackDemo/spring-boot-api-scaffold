package com.scaffold.test.utils;

import com.scaffold.test.entity.Student;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtils {

    // 生成 excel
    public static void createExcel(List<Student> list, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("统计表");
        // 生成表头
        createHeader(workbook, sheet);

        // 新增数据行
        int rowNum = 1;
        for (Student student : list) {
            XSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(student.getName());
            row.createCell(1).setCellValue(student.getAge());
            rowNum++;
        }

        String fileName = "test.xlsx";
        // 生成文件
        generateExcelFile(fileName, workbook);
        //浏览器下载
        browserDocument(fileName, workbook, response);
    }

    // 生成表头
    protected static void createHeader(XSSFWorkbook workbook, XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);
        //设置列宽，setDefaultColumnWidth的参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setDefaultColumnWidth(20 * 256);

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

    }

    // 生成 Excel 文件
    protected static void generateExcelFile(String fileName, XSSFWorkbook workbook) throws IOException {
        String folderPath = "F:\\project\\spring-boot-api-scaffold\\src\\main\\resources\\files\\";
        FileOutputStream fileOutputStream = new FileOutputStream(folderPath + fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    // 浏览器输出文件下载
    protected static void browserDocument(String fileName, XSSFWorkbook workbook, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        // Content-disposition控制用户请求所得的内容存为一个文件的时候提供一个默认的文件名
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


}
