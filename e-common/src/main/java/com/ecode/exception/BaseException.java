package com.ecode.exception;

import lombok.Getter;

/**
 * 业务异常
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -1780529152139531006L;

    @Getter
    private final Integer code;

    public BaseException(String msg) {
        this(msg, null);
    }

    public BaseException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

}