package com.scaffold.test.entity;

import lombok.Data;

import java.util.List;

@Data
public class TestList {

    private List<Test> list;

    private String name;

    private int age;

}
