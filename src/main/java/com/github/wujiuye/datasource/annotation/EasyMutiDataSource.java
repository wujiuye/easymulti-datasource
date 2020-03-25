package com.github.wujiuye.datasource.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据源注解
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 * }
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RUNTIME)
@Documented
public @interface EasyMutiDataSource {

    /**
     * 多数据源的Key
     */
    enum MultipleDataSource {
        /**
         * NULL，使用默认数据源
         */
        Null,

        /**
         * 主从库
         */
        Master,
        Slave,

        /**
         * 1～10库
         */
        First,
        Second,
        Third,
        Fourth,
        Fifth,
        Sixth,
        Seventh,
        Eighth,
        Ninth,
        Tenth
    }

    /**
     * 多数据源指定
     *
     * @return
     */
    MultipleDataSource value() default MultipleDataSource.Null;

}
