package com.ymt.utils;

/**
 * 库存不足异常
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年5月23日
 */
public class StockException extends BaseException {
    public static final String Exception_code = "2";
    /**
     *
     */
    private static final long serialVersionUID = 6060874887793105459L;

    public StockException(String message) {
        super(message);
    }

    @Override
    public String getExceptionCode() {
        return Exception_code;
    }

}
