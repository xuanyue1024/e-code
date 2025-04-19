package com.ecode.exception;

/**
 * AI异常类，继承自BaseException。
 *
 * @author 竹林听雨
 * @date 2025/04/19
 */
public class AiException extends BaseException{
    private static final long serialVersionUID = -8050748477030614525L;

    public AiException(String msg) {
        super(msg);
    }
}
