package com.scaffold.test.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;

/**
 * 拉易网上传图床方法
 */

@Component
public class DragEasyUtils {


    // token
    private static String drageasyToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kcmFnZWFzeS5jb20iLCJhdWQiOiJ2c29udGVyIiwiaWF0IjoxNTk2MTg5MjU3LCJleHAiOjE1OTYxOTI4NTcsInVpZCI6Im9qUG41d0xYNy0zRzB2aFJEdFAtVDdHRWd5S1UifQ.I2vjDyo8bVm8OtmkaTZAqAne9SJn9yRIP0sVP2bPXCo";

    String drageasyUploadToken = "https://www.drageasy.com/util/getUploadToken";

    String drageasyUploadPath = "https://www.drageasy.com/util/qiniu_upp";

    /**
     * 获取上传TOKEN
     *
     * @return
     */
    public static String getUploadToken() {

        HttpClient client = new DefaultHttpClient();

        return "";
    }


    public static void main(String[] args) {
        DragEasyUtils.getUploadToken();
    }

}
