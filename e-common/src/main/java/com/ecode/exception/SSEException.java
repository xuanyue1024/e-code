package com.ecode.exception;

/**
 * SSE格式请求异常类
 *
 * @author 竹林听雨
 * @date 2024/12/28
 */
public class SSEException extends BaseException{
    private static final long serialVersionUID = -8050748477030614525L;

    public SSEException(String msg) {
        super(msg);
    }
}
