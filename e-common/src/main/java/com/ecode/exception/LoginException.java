package com.ecode.exception;

/**
 * 登录异常
 */
public class LoginException extends BaseException{
    private static final long serialVersionUID = 2920390803577286971L;

    public LoginException(String msg){
        super(msg);
    }
}
