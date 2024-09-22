package com.ecode.exception;

/**
 * 无效登录类型异常
 *
 * @author 竹林听雨
 * @date 2024/09/22
 */
public class InvalidLoginTypeException extends BaseException{

    private static final long serialVersionUID = -5474606691088346085L;

    public InvalidLoginTypeException(String msg) {
        super(msg);
    }
}
