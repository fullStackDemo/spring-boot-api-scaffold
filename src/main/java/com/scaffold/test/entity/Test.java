package com.scaffold.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author alex wong
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@JsonIgnoreProperties(value = {"subName", "age"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFilter("customFilter")
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    public interface TestInfo {
    }

    public interface TestDetail {
    }

    // 在 TestInfo 中显示 Id
//    @JsonView(TestInfo.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 在 TestDetail 中显示名字
//    @JsonView(TestDetail.class)
    private String name;

    private String age;

    private String subName;

    private List<Mate> mateList;
}
