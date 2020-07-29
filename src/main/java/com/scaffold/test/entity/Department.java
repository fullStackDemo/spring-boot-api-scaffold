package com.scaffold.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author alex wong
 * @since 2020-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 对应钉钉的部门id
     */
    private Long depId;

    /**
     * 对应钉钉的部门父id
     */
    private Long parentId;

    /**
     * 钉钉部门名字
     */
    private String depName;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 是否有效 1 有效 0 无效
     */
    private Integer yn;


}
