package com.plat.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtils {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    static {// 设置Date转换格式
        DateConverter dateConverter = new DateConverter(null);
        dateConverter.setPatterns(new String[] { StringUtil.PATTERN });
        ConvertUtils.register(dateConverter, Date.class);
        ConvertUtils.register(new AbstractConverter() {

            @SuppressWarnings("rawtypes")
            @Override
            public Object convert(Class type, Object value) {
                if (value instanceof Date) {
                    return StringUtil.DateToStr((Date) value);
                } else {
                    return super.convert(type, value);
                }
            }

            @SuppressWarnings("rawtypes")
            @Override
            protected Object convertToType(Class type, Object value) throws Throwable {
                return value.toString();
            }

            @SuppressWarnings("rawtypes")
            @Override
            protected Class getDefaultType() {
                return String.class;
            }
        }, String.class);
    }

    /**
     * 将src对象下相同属性的值转到dist下(对象类型可以不一致,属性类型也可以不一致)
     *
     * @param src
     * @param dist
     */
    public static void conver(Object src, Object dist) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dist, src);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将src对象的指定属性值转换到dist中(对象类型可以不一致,属性类型也可以不一致)
     *
     * @param src
     * @param dist
     * @param propertyNames
     */
    public static void copyProperty(Object src, Object dist, String... propertyNames) {
        try {
            for (String property : propertyNames) {
                org.apache.commons.beanutils.BeanUtils.copyProperty(dist, property,
                        org.apache.commons.beanutils.BeanUtils.getProperty(src, property));
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }
}
