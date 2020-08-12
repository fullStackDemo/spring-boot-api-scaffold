package com.scaffold.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    @TableId(value = "user_id", type = IdType.AUTO)
    private String userId;

    private String userName;

    private String password;

    private Date createTime;

    private Date updateTime;

    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private String token;

    /**
     * 是否被踢出
     */
    @TableField(exist = false)
    private Boolean kickout = false;

    @TableField(exist = false)
    private String uuid;

}
