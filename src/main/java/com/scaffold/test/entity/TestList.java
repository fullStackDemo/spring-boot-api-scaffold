package com.scaffold.test.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestList {


    private String name;

    private int age;

    private List<Test> list;

}
