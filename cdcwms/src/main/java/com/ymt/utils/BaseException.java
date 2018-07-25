package com.ymt.utils;

public abstract class BaseException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 8176728454726046039L;

    public abstract String getExceptionCode();

    public BaseException(String message) {
        super(message);
    }
}
