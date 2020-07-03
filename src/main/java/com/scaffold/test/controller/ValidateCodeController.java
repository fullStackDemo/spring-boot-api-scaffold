package com.scaffold.test.controller;

import com.scaffold.test.base.Result;
import com.scaffold.test.base.ValidateCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class ValidateCodeController {


    // 生成验证码图片
    @RequestMapping("/getCaptchaImage")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expire", "0");
            response.setHeader("Pragma", "no-cache");

            ValidateCode validateCode = new ValidateCode();

            // 直接返回图片
            validateCode.getRandomCodeImage(request, response);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    // 生成验证码,返回的是 base64
    @RequestMapping("/getCaptchaBase64")
    @ResponseBody
    public Object getCaptchaBase64(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> result = new HashMap();
        Result data = new Result();

        try {
            ValidateCode validateCode = new ValidateCode();

            // 返回base64
            String base64String = validateCode.getRandomCodeBase64(request, response);
            result.put("url", "data:image/png;base64," + base64String);
            result.put("message", "created successfull");
            data.setData(result);

        } catch (Exception e) {
            System.out.println(e);
        }

        return data;
    }

}
