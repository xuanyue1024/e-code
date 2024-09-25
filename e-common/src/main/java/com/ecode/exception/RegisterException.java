package com.ecode.exception;

/**
 * 注册异常
 *
 * @author 竹林听雨
 * @date 2024/09/25
 */
public class RegisterException extends BaseException{

    private static final long serialVersionUID = -6681041935499298285L;

    public RegisterException(String msg) {
        super(msg);
    }
}
