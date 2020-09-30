package com.scaffold.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义过滤对象属性
 */

@Component
public class CustomFilterConfig {
    private static final Set<String> ignorableFieldNames = new HashSet<>();

    static {
        ignorableFieldNames.add("name");
        ignorableFieldNames.add("subName");
    }

    @Bean
    public MappingJackson2HttpMessageConverter messageConverter(){

        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider().setFailOnUnknownId(false);
        FilterProvider filters = simpleFilterProvider.addFilter("customFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
        objectMapper.setFilterProvider(filters);

        jacksonConverter.setObjectMapper(objectMapper);

        return jacksonConverter;
    }
}
