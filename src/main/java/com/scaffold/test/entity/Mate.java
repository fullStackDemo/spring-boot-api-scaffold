package com.scaffold.test.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 伙伴
 * @author alex wong
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Mate implements Serializable {

    private static final long serialVersionUID=1L;

    @NotBlank(message = "小伙伴的name不能为空")
    private String name;

    @NotNull(message = "小伙伴的age不能为空")
    private Integer age;

}
