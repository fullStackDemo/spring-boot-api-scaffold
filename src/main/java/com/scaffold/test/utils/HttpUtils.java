package com.scaffold.test.utils;

import com.scaffold.test.entity.HttpParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author alex
 */

public class HttpUtils {

    /**
     * 获取request
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    /**
     * 获取response
     *
     * @return response
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getResponse();
    }

    /**
     * 获取session
     *
     * @return session
     */
    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getSession();
    }

    /**
     * POST
     */
    public static String post(HttpParams httpParams) {

        // 创建HTTP客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // POST
        HttpPost httpPost = new HttpPost(httpParams.getRequestUrl());
        // 请求头
        Map<String, Object> requestHeader = httpParams.getRequestHeader();
//        httpPost.setHeader("Token", (String) requestHeader.get("token"));
        for (String headerKey : requestHeader.keySet()) {
            httpPost.setHeader(headerKey, (String) requestHeader.get(headerKey));
        }
        // 响应
        CloseableHttpResponse response = null;
        String responseData = null;
        // 请求参数
        Map<String, Object> requestParams = httpParams.getRequestParams();
        // 参数类型
        String requestParamsType = httpParams.getRequestParamsType();
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        HttpEntity httpEntity;
        if (requestParamsType != null) {
            switch (requestParamsType) {
                case "formData":
                    // 创建参数队列
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    builder.addBinaryBody("file", (File) requestParams.get("file"));
                    builder.addTextBody("timestamp", String.valueOf(requestParams.get("timestamp")));
                    builder.addTextBody("upload_token", String.valueOf(requestParams.get("upload_token")));
                    httpEntity = builder.build();
                    httpPost.setEntity(httpEntity);
                    break;
                case "formUrl":
                    List<NameValuePair> params = new ArrayList<>();
                    for (String paramKey : requestParams.keySet()) {
                        params.add(new BasicNameValuePair(paramKey, requestParams.get(paramKey).toString()));
                    }
                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
        try {
            // 客户端执行请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 从响应中获取响应实体
                HttpEntity entity = response.getEntity();
                responseData = EntityUtils.toString(entity, "utf-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return responseData;
    }

}
