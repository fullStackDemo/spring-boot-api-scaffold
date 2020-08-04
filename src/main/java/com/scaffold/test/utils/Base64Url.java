package com.scaffold.test.utils;

import com.alibaba.fastjson.JSON;

import java.util.Base64;

/**
 * @author alex
 */

public class Base64Url {

    /**
     * base64加密
     *
     * @param content
     * @return
     */
    public static String encode(Object content) {
        Base64.Encoder encoder = Base64.getUrlEncoder();
        byte[] data = JSON.toJSONString(content).getBytes();
        return encoder.encodeToString(data);
    }

    /**
     * base64解密
     * @param text
     * @return
     */
    public static String decode(String text) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        byte[] data = decoder.decode(text);
        return new String(data);
    }


    public static void main(String[] args){

        System.out.println(Base64Url.encode(JSON.parseObject("{\"sub\":\"{userName=test1, userId=fcfbb2b3d61944f2b31237d2338b54f3}\",\"userName\":\"test1\",\"exp\":1596556187,\"userId\":\"fcfbb2b3d61944f2b31237d2338b54f3\",\"iat\":1596548987,\"jti\":\"67d5f810-7e87-48a3-8bf8-8a09b857887c\"}")));

        System.out.println(Base64Url.decode("eyJzdWIiOiJ7dXNlck5hbWU9dGVzdDEsIHVzZXJJZD1mY2ZiYjJiM2Q2MTk0NGYyYjMxMjM3ZDIzMzhiNTRmM30iLCJ1c2VyTmFtZSI6InRlc3QxIiwiZXhwIjoxNTk2NTU2MTg3LCJ1c2VySWQiOiJmY2ZiYjJiM2Q2MTk0NGYyYjMxMjM3ZDIzMzhiNTRmMyIsImlhdCI6MTU5NjU0ODk4NywianRpIjoiNjdkNWY4MTAtN2U4Ny00OGEzLThiZjgtOGEwOWI4NTc4ODdjIn0"));


    }


}
