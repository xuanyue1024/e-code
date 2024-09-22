package com.ecode.exception;

/**
 * 业务异常
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -1780529152139531006L;

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}