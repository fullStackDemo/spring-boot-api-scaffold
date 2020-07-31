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
public class WeatherStatus implements Serializable {

    private static final long serialVersionUID=1L;

      private Integer id;

    /**
     * 天气分类名字
     */
    private String name;

    /**
     * 天气分类值
     */
    private String value;

    /**
     * 天气icon
     */
    private String icon;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
