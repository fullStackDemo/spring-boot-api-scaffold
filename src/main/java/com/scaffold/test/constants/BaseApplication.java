package com.scaffold.test.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 读取基础配置信息
 * */
@Component
@Configuration
@Data
public class BaseApplication {

    // 导出路径
    @Value("${user.export}")
    private String exportPath;

    // mac导出地址
    @Value("${user.mac-export}")
    private String macExportPath;

    @Value("${spring.datasource.url}")
    String dataSourceUrl;

    @Value("${spring.datasource.username}")
    String dataSourceUserName;

    @Value("${spring.datasource.password}")
    String dataSourcePassword;

    @Value("${spring.datasource.driver-class-name}")
    String dataSourceDriverClassName;

    // 邮件发送者地址
    @Value("${mail.fromMail.addr}")
    String mailSenderAddr;



}
