package com.scaffold.test.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author alex wong
 * @since 2020-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Websites implements Serializable {

    private static final long serialVersionUID=1L;

    private int id;

    private String name;

    private String url;

    private String country;


}
