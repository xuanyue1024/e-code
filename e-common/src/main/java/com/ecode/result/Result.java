package com.ecode.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -3384174519542040924L;

    @Schema(description="成功与否;200成功,500失败",required = true)
    private Integer code; //编码：

    @Schema(description="消息")
    private String msg; //信息

    @Schema(description="返回数据")
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 200;
        result.msg = "成功";
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 200;
        result.msg = "成功";
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 500;
        return result;
    }

}
