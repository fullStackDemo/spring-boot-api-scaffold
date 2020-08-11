package com.scaffold.test.config;

import com.scaffold.test.filter.KickOutFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤
 */

@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean KickOutFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setFilter(new KickOutFilter());
        filterRegistrationBean.setName("KickOutFilter");
        filterRegistrationBean.addUrlPatterns("/api/*");
        return  filterRegistrationBean;
    }

}
