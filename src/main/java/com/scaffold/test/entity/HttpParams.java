package com.scaffold.test.entity;

import lombok.Data;

import java.util.Map;

/**
 * HTTP请求参数实体
 * @author alex
 */

@Data
public class HttpParams {

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求头信息
     */
    private Map<String, Object> requestHeader;

    /**
     * 请求参数
     */
    private Map<String, Object> requestParams;

    /**
     * 请求参数类型
     */
    private String requestParamsType;

}
