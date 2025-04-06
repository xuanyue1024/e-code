package com.ecode.exception;

/**
 * 权限异常类
 * 用于处理权限相关的异常情况。
 *
 * @author 竹林听雨
 * @date 2025/04/07
 */
public class PermissionException extends BaseException{
    private static final long serialVersionUID = 6576912594145508360L;

    public PermissionException(String msg) {
        super(msg);
    }
}
