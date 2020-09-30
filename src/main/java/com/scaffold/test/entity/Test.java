package com.scaffold.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * @author alex wong
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"subName", "age"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Test implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonProperty("customer")
    private String name;

    private String age;

    private String subName;
}
