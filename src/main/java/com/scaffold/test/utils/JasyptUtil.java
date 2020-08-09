package com.scaffold.test.utils;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * Jasypt 3.0.3 加密
 * @author alex
 */
public class JasyptUtil {

    /**
     * 配置项
     * @param password 加密使用的盐值
     * @return config
     */
    public static SimpleStringPBEConfig cryptor(String password){
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    /**
     * 加密
     * @param salt 加密使用的盐值
     * @param value 被加密的值
     * @return string
     */
    public static String encypt(String salt, String value){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(salt));
        return encryptor.encrypt(value);
    }

    /**
     * 解密
     * @param salt 加密使用的盐值
     * @param value 加密后的密文
     * @return string
     */
    public static String decypt(String salt, String value){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(salt));
        return encryptor.decrypt(value);
    }


    public static void main(String[] args) {

        String encryptPwd = encypt("test", "serverTimezone");
        System.out.println(encryptPwd);
        String decryptPwd = decypt("test", encryptPwd);
        System.out.println(decryptPwd);

    }

}
