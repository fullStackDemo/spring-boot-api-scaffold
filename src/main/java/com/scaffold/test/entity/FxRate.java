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
 * @since 2020-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FxRate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 来源
     */
    private String source;

    /**
     * 时刻
     */
    private String rateDate;

    /**
     * 中间价
     */
    private double rate;

    /**
     * 当前卖出货币
     */
    private String sellCcy;

    /**
     * 当前买入货币
     */
    private String buyCcy;

    /**
     * 当前报价品种
     */
    private String ccyType;

    /**
     * 当前货币对
     */
    private String ccyPair;

    private String flag;

    private LocalDateTime createTime;


}
