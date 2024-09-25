package com.ecode.exception;

/**
 * 邮件验证码异常
 *
 * @author 竹林听雨
 * @date 2024/09/25
 */
public class MailCaptchaException extends BaseException{
    private static final long serialVersionUID = -3986256804688302064L;

    public MailCaptchaException(String msg) {
        super(msg);
    }
}
