package com.ymt.utils;

/**
 * 物料不匹配异常
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年5月23日
 */
public class GoodsException extends BaseException {
    public static final String Exception_code = "3";

    /**
     *
     */
    private static final long serialVersionUID = -7873446549346717212L;

    public GoodsException(String message) {
        super(message);
    }

    @Override
    public String getExceptionCode() {
        return Exception_code;
    }

}
