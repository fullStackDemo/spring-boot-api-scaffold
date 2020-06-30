package com.scaffold.test.utils;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

public class ExcelUtils {

    // 生成 excel
    public static void createExcel(String fileName, String folderPath, XSSFWorkbook workbook, HttpServletResponse response) throws IOException {
        // 生成文件
        generateExcelFile(fileName, folderPath, workbook);
        //浏览器下载
        browserDocument(fileName, workbook, response);
    }

    // 创建
    public static XSSFWorkbook createWorkBook() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        return workbook;
    }


    // 生成 Excel 文件
    protected static void generateExcelFile(String fileName, String folderPath, XSSFWorkbook workbook) throws IOException {
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
