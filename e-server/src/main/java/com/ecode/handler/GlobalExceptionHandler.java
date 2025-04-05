package com.ecode.handler;


import com.ecode.constant.MessageConstant;
import com.ecode.exception.BaseException;
import com.ecode.exception.SSEException;
import com.ecode.result.Result;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yubico.webauthn.exception.AssertionFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.sql.SQLException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("业务异常:{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * @param ex 数据库异常
     * @return 后端统一返回结果
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLException ex){
        String message = ex.getMessage();
        log.error("数据库异常:{}",message);
        if (message.contains("Duplicate entry")){
            String username = message.split(" ")[2];
            return Result.error(username + MessageConstant.ALREADY_EXISTS);
        }else {
            return Result.error(MessageConstant.SQL_UNKNOWN_ERROR);
        }
    }

    /**
     * 格式异常（主要针对注解）
     *
     * @param ife 异常项
     * @return 后端统一返回结果
     */
    @ExceptionHandler
    public Result exceptionHandler(InvalidFormatException ife){
        String message = ife.getMessage();
        log.error("格式异常:{}",message);

        return Result.error(MessageConstant.INVALID_FORMAT_FAILURE);
    }

    @ExceptionHandler(SSEException.class)//todo 捕捉不到
    public SseEmitter handleException(SSEException ex) {
        SseEmitter emitter = new SseEmitter();

        try {
            // 发送 SSE 格式的错误消息
            emitter.send(SseEmitter.event()
                    .data(Result.error(ex.getMessage())));
            emitter.completeWithError(ex);
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    /**
     * passkey验证异常
     *
     * @param afe afe
     * @return 后端统一返回结果
     */
    @ExceptionHandler
    public Result exceptionHandler(AssertionFailedException afe){
        String message = afe.getMessage();
        log.error("passkey验证异常:{}",message);

        return Result.error(MessageConstant.PASSKEY_VERIFY_FAILED);
    }

    /*@ExceptionHandler
    public Result exceptionHandler(Exception ex){
        log.error("服务器异常:{}",ex.getMessage());
        return Result.error("服务器异常");

    }*/

}
