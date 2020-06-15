package com.scaffold.test.entity;

import lombok.Data;

@Data
public class Mail {

    // 发送给谁
    private String to;

    // 发送主题
    private String subject;

    // 发送内容
    private String content;
}
