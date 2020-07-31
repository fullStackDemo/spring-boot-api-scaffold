package com.scaffold.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WeatherTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 天气状态
     */
    private String status;

    /**
     * 温度
     */
    private String temp;

    /**
     * 时刻
     */
    private String time;
    /**
     * 日期
     */
    private String date;

    /**
     * 标记
     */
    private String flag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
