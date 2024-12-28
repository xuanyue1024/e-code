package com.ecode.exception;

/**
 * 题目异常类
 *
 * @author 竹林听雨
 * @date 2024/12/28
 */
public class ProblemException extends BaseException{
    private static final long serialVersionUID = -8050748477030614525L;

    public ProblemException(String msg) {
        super(msg);
    }
}
