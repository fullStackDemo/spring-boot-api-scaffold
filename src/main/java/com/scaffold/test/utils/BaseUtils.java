package com.scaffold.test.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.scaffold.test.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseUtils {

    // 获取所有的请求头
    public static Map<String, Object> getHeaders() {
        HttpServletRequest request = HttpUtils.getRequest();

        if (request == null) return null;

        HashMap<String, Object> headerInfo = new HashMap<>();

        // 获取请求头所有KEY
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerInfo.put(key, value);
        }
        return headerInfo;
    }

    // 获取请求头单个数据
    public static String getHeader(String key) {
        HttpServletRequest request = HttpUtils.getRequest();

        if (request == null) return null;

        return request.getHeader(key);
    }

    // 获取token
    public static String getToken() {
        return getHeader("token");
    }

    // 获取用户信息
    public static User getCurrentUser() {
        User user = new User();
        String token = getToken();
        if (!token.equals("null")) {
            try {
                DecodedJWT claims = JWT.decode(token);
                user.setUserName(claims.getClaim("userName").asString());
                user.setUserId(claims.getClaim("userId").asString());
            } catch (Exception e) {
                return null;
            }
        }
        return user;
    }

    //获取当前用户的id
    public static String getCurrentUserId() {
        User user = getCurrentUser();
        if (user == null) return null;
        return getCurrentUser().getUserId();
    }

}
