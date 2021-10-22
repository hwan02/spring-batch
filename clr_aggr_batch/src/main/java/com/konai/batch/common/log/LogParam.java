package com.konai.batch.common.log;

import java.lang.annotation.*;

/**
 * Project : springbootmybatis
 * Developer : jbhan
 * Date : 2017-02-08
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogParam {

    /**
     * Value.
     *
     * @return the string
     */
    String value() default("");
}