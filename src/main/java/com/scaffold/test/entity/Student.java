package com.scaffold.test.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author alex wong
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Student implements Serializable {

    private static final long serialVersionUID=1L;

//    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String name;

    private Integer age;

}
