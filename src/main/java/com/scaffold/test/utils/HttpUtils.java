package com.scaffold.test.utils;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.entity.HttpParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    public static Object post(HttpParams httpParams) {

        // 创建HTTP客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // POST
        HttpPost httpPost = new HttpPost(httpParams.getRequestUrl());
        // 请求头
        JSONObject requestHeader = (JSONObject) httpParams.getRequestHeader();
        httpPost.setHeader("Token", (String) requestHeader.get("token"));
        // 响应
        CloseableHttpResponse response = null;

        try {
            // 客户端执行请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 从响应中获取响应实体
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "utf-8");
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

        return response;
    }

}
