package com.scaffold.test.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
