package com.scaffold.test.utils;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.entity.HttpParams;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 拉易网上传图床方法
 *
 * @author Alex
 */

@Slf4j
public class DragEasyUtils {

    private static String drageasyToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kcmFnZWFzeS5jb20iLCJhdWQiOiJ2c29udGVyIiwiaWF0IjoxNTk2MTg5MjU3LCJleHAiOjE1OTYxOTI4NTcsInVpZCI6Im9qUG41d0xYNy0zRzB2aFJEdFAtVDdHRWd5S1UifQ.I2vjDyo8bVm8OtmkaTZAqAne9SJn9yRIP0sVP2bPXCo";

    private static String drageasyUploadToken = "https://www.drageasy.com/util/getUploadToken";

    private static String drageasyUploadPath = "https://www.drageasy.com/util/qiniu_upp";

    /**
     * 获取上传TOKEN
     *
     * @return uploadToken
     */
    public static String getUploadToken() {
        // 参数实体
        HttpParams httpParams = new HttpParams();
        httpParams.setRequestUrl(drageasyUploadToken);
        // 请求头
        JSONObject header = new JSONObject();
        header.put("token", drageasyToken);
        httpParams.setRequestHeader(header);
        // 获取响应结果
        Object response = HttpUtils.post(httpParams);
        return response.toString();
    }


    /**
     * 获取图片路径
     *
     * @param file 文件对象
     * @return string
     */
    public static String getIconUrl(File file) {
        log.info("------上传图床中-----");
        HttpParams httpParams = new HttpParams();
        httpParams.setRequestUrl(drageasyUploadPath);
        // 请求头
        JSONObject header = new JSONObject();
        header.put("token", drageasyToken);
        httpParams.setRequestHeader(header);
        // 请求参数
        JSONObject params = new JSONObject();
        params.put("timestamp", System.currentTimeMillis());
        params.put("upload_token", DragEasyUtils.getUploadToken());
        params.put("file", file);
        httpParams.setRequestParams(params);
        httpParams.setRequestParamsType("formData");
        // 获取响应结果
        String iconUrl = null;
        try {
            // 延迟，否则连接容易被拒绝
            Thread.sleep(5000);
            String response = HttpUtils.post(httpParams);
            JSONObject data = JSONObject.parseObject(response);
            iconUrl = data.getString("path");
            log.info("iconUrl=" + iconUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconUrl;
    }


    public static void main(String[] args) {
        // 文件夹路径
        String iconPath = "/Users/wangzhao/work/projects/spring-boot-api-scaffold/src/main/resources/icons/weather";
        File file = new File(iconPath);
        File[] files = file.listFiles();
        if (files.length == 0) {
            return;
        }
        for (File iconFile : files) {
            try {
                // FileInputStream fileInputStream = new FileInputStream(iconFile);
                // MockMultipartFile multipartFile = new MockMultipartFile(iconFile.getName(), iconFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                String iconUrl = DragEasyUtils.getIconUrl(iconFile);
                System.out.println(iconUrl);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
