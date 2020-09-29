package com.scaffold.test.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

/**
 * 全局参数错误拦截
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数异常
     */
    @ExceptionHandler(value = BindException.class)
    public Result validationExceptionHandler(BindException exception){
        System.out.println(exception);
        return ResultGenerator.setFailResult("");
    }


}
