package com.scaffold.test.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    private static final long serialVersionUID=1L;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
