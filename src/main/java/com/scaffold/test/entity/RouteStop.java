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
public class RouteStop implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String routeCode;

    /**
     * 站点name
     */
    private String stopName;

    /**
     * 站点code
     */
    private String stopCode;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 顺序
     */
    private Integer orderNum;

    /**
     * 站点ID
     */
    private String stationId;

    /**
     * 是否特殊关注
     */
    private String isAttention;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
