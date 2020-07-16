package com.scaffold.test;


import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScaffoldApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScaffoldApplicationTests {

    String dataSourceUserName = "root";

    String dataSourcePassword = "";

    /**
     * 基础加密
     */
    @Test
    public void getPass() {
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        basicTextEncryptor.setPassword("1qaz2wsx3edc");
        // 加密数据库相关连接数据
        String name = basicTextEncryptor.encrypt(dataSourceUserName);
        String password = basicTextEncryptor.encrypt(dataSourcePassword);
        log.info("---name---");
        log.info(name);
        log.info("---password---");
        log.info(password);
    }

    /**
     * 强加密
     */
    @Test
    public void getStrongPass() {
        StrongTextEncryptor strongTextEncryptor = new StrongTextEncryptor();
        //加密所需的salt(盐)
        strongTextEncryptor.setPassword("1qaz2wsx3edc");
        // 加密数据库相关连接数据
        String name = strongTextEncryptor.encrypt(dataSourceUserName);
        String password = strongTextEncryptor.encrypt(dataSourcePassword);
        log.info("-------strong--------");
        log.info("---name---");
        log.info(name);
        log.info("---password---");
        log.info(password);
    }

}
