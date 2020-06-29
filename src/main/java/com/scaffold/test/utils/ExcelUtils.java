package com.scaffold.test.utils;

import com.scaffold.test.entity.Weather;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.List;

public class ExcelUtils {

    // 生成 excel
    public static void createExcel(List<Weather> list) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");
        // 生成表头
        createHeader(workbook, sheet);


    }

    // 生成表头
    private static void createHeader(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setDefaultColumnWidth的参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setDefaultColumnWidth(20 * 256);

        //设置为居中加粗
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        HSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("日期");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("天气");
        cell.setCellStyle(cellStyle);

    }


}
