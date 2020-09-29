package com.scaffold.test.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.BindException;

/**
 * 全局参数错误拦截
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数异常
     */
    @ResponseBody
    @ExceptionHandler(BindingException.class)
    public Result handleBindException(BindingException e) throws BindException {
        System.out.println(e);
        return null;
    }


}
