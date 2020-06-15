package com.scaffold.test.service;

import com.scaffold.test.entity.Mail;

public interface MailService {
    // 发送邮件
    public void sendMail(Mail mail);
}
