package com.scaffold.test.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author alex wong
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Student implements Serializable {

    private static final long serialVersionUID=1L;

    @Id
    @Range(min = 1, message = "id不能为空")
    private int id;

    @NotBlank(message = "name不能为空")
    private String name;

    @NotNull(message = "age不能为空")
    private Integer age;

    // 伙伴列表
    @Valid // 嵌套验证必须用@Valid
    @NotNull(message = "mateList不能为空")
    @Size(min = 1, message = "至少需要一个小伙伴")
    private List<Mate> mateList;

}
