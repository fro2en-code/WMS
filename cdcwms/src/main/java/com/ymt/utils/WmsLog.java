package com.ymt.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志类注解(仅用在controller方法上)
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年9月6日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WmsLog {
    /**
     * 获得请求值关键key
     */
    String value() default "id";
}
