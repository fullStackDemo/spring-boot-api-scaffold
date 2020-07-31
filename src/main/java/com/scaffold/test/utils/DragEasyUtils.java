package com.scaffold.test.utils;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.entity.HttpParams;

/**
 * 拉易网上传图床方法
 * @author Alex
 */

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


    public static void main(String[] args) {
        String uploadToken = DragEasyUtils.getUploadToken();
    }

}
