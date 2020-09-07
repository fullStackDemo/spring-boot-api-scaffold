package com.scaffold.test.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {

    /**
     * 导入CSV并解析
     *
     * @param file
     * @return
     */
    public static List<String> importCsv(File file) {
        // CSV数据
        List<String> dataList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        // 解析CSV文件
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "GBK");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                dataList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }


    /**
     * 导出
     *
     * @param file
     * @param dataList
     * @return
     */
    public static boolean exportCsv(File file, List<String> dataList) {

        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        Boolean exportSuccess = false;

        try {
            fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            if (dataList != null && !dataList.isEmpty()) {
                for (String data : dataList) {
                    bufferedWriter.append(data).append("\r");
                }
            }
            exportSuccess = true;
        } catch (Exception e) {
            exportSuccess = false;
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    fileOutputStream = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                    outputStreamWriter = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    bufferedWriter = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return exportSuccess;
    }


}