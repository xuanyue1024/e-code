package com.ecode.handler;


import com.ecode.constant.MessageConstant;
import com.ecode.exception.BaseException;
import com.ecode.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

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
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * @param ex 数据库异常
     * @return 后端统一返回结果
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        log.info("数据库出现异常,{}",ex);
        if (message.contains("Duplicate entry")){
            String username = message.split(" ")[2];
            return Result.error(username + MessageConstant.ALREADY_EXISTS);
        }else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
