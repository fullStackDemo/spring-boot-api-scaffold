package com.scaffold.test.service;

public interface MailService {
    // 发送邮件
    public void sendMail(String to, String subject, String content);
}
