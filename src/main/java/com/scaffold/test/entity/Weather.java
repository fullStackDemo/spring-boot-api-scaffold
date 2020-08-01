package com.scaffold.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author alex wong
 * @since 2020-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Weather implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String status;

    private String date;

    @TableField(exist = false)
    private String icon;

    @TableField(exist = false)
    private String week;

    // 最高气温
    private String max;

    // 最低气温
    private String min;

    private LocalDateTime createTime;


}
