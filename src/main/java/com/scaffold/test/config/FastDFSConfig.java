package com.scaffold.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FastDFSConfig extends WebMvcConfigurerAdapter {

    // 代理文件资源到文件服务器
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/group*/**").addResourceLocations("http://192.168.6.196:8899/");
        super.addResourceHandlers(registry);
    }
}
