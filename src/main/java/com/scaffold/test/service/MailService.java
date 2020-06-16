package com.scaffold.test.service;

import com.scaffold.test.entity.Mail;

import javax.mail.MessagingException;

public interface MailService {
    // 发送邮件
    void sendMail(Mail mail);

    // 发送Html邮件
    void sendHtmlMail(Mail mail) throws MessagingException;

    // 发送带附件的邮件
    void sendAttachmentsMail(Mail mail) throws MessagingException;
}
