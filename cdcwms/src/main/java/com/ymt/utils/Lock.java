package com.ymt.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 同步锁
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年9月11日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    /**
     * 根据 request.getParameter 来锁数据
     */
    String LOCK_request = "request";
    /**
     * 根据静态值锁数据
     */
    String LOCK_static = "static";

    /**
     * 锁的类型(根据静态值或参数字段锁)
     */
    String type() default LOCK_request;

    /**
     * 锁的值
     */
    String value() default "id";
}
