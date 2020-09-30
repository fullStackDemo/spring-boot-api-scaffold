package com.scaffold.test.config.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Jsons.class)
public @interface Json {

    Class<?> type();

    String include() default "";

    String filter() default "";

}
