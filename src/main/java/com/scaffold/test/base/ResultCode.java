package com.scaffold.test.base;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * @author alex
 */

public enum ResultCode {
    /**
     * 状态码
     */
    SUCCESS(200),
    //失败
    FAIL(400),
    //未认证（签名错误）
    UNAUTHORIZED(401),
    //接口不存在
    NOT_FOUND(404),
    // 账户在别的设备登录
    ALREADY_EXIST(1001),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }

}
