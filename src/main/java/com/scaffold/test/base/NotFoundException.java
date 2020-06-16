package com.scaffold.test.base;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotFoundException implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public Result error() {
        Result result = new Result();
        // 4、接口不存在
        result.setCode(ResultCode.NOT_FOUND).setMessage("接口不存在");
        return result;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
