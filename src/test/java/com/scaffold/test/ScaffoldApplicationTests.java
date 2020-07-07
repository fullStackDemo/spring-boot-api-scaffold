package com.scaffold.test;


import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScaffoldApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScaffoldApplicationTests {



    @Autowired
    StringEncryptor encryptor;

//    @Value("${spring.datasource.url}")
//    String dataSourceUrl;
//
//    @Value("${spring.datasource.username}")
//    String dataSourceName;
//
//    @Value("${spring.datasource.password}")
//    String dataSourcePassword;
//
//    @Test
//    public void getPass() {
//        String url = encryptor.encrypt(dataSourceUrl);
//        String name = encryptor.encrypt(dataSourceName);
//        String password = encryptor.encrypt(dataSourcePassword);
//        log.info("---url---");
//        log.info(url);
//        log.info("---name---");
//        log.info(name);
//        log.info("---password---");
//        log.info(password);
//    }

}
