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
 * @since 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Route implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 路线名字
     */
    private String routeName;

    /**
     * 路线代码
     */
    private String routeCode;

    /**
     * 起始时间
     */
    private String firstTime;

    /**
     * 结束时间
     */
    private String lastTime;

    /**
     * 价格
     */
    private String priceBase;

    /**
     * 起始站点
     */
    private String startStopName;

    /**
     * 结束站点
     */
    private String endStopName;

    /**
     * 最近站点名字
     */
    private String nearestStopName;

    /**
     * 最近时间
     */
    private String nearestStopDuration;

    /**
     * 最近距离
     */
    private String nearestStopDistance;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;


}
