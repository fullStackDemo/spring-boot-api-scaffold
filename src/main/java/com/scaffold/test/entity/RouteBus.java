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
public class RouteBus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Double distance;

    private Integer duration;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    private String nextStopCode;

    private Integer nextStopIndex;

    private String nextStopName;

    private String stopIndex;

    private Integer stopNum;

    private String state;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
