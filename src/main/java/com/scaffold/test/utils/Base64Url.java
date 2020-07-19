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

        System.out.println(Base64Url.encode(JSON.parseObject("{\"sub\":\"{userName=test, userId=0b13dd6ce7e54d8d84b748541299a927}\",\"password\":\"2ad0fa85d0e8fc5593bb9cb893750302\",\"userName\":\"test\",\"exp\":1595080348,\"userId\":\"0b13dd6ce7e54d8d84b748541299a927\",\"iat\":1595073148,\"jti\":\"fd5331d3-1897-491d-bf87-1f4a49e2b80b\"}")));

        System.out.println(Base64Url.decode("eyJzdWIiOiJ7dXNlck5hbWU9dGVzdCwgdXNlcklkPTBiMTNkZDZjZTdlNTRkOGQ4NGI3NDg1NDEyOTlhOTI3fSIsInBhc3N3b3JkIjoiMmFkMGZhODVkMGU4ZmM1NTkzYmI5Y2I4OTM3NTAzMDIiLCJ1c2VyTmFtZSI6InRlc3QiLCJleHAiOjE1OTUwODAzNDgsInVzZXJJZCI6IjBiMTNkZDZjZTdlNTRkOGQ4NGI3NDg1NDEyOTlhOTI3IiwiaWF0IjoxNTk1MDczMTQ4LCJqdGkiOiJmZDUzMzFkMy0xODk3LTQ5MWQtYmY4Ny0xZjRhNDllMmI4MGIifQ"));

        System.out.println(Base64Url.decode("OpVIfLE4IeV4UXdzpocTO6k1f5tnkolBHEkHEL9vPtM"));

    }


}
