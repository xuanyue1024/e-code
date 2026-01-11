package com.ecode.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

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
        return success(200, null, null);
    }

    public static <T> Result<T> success(T object) {
        return success(200, object, null);
    }

    public static <T> Result<T> success(Integer code, T object, String msg) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = code;
        result.msg = Objects.requireNonNullElse(msg, "成功");
        return result;
    }

    public static <T> Result<T> error(String msg) {
        return error(msg, 500);
    }

    public static <T> Result<T> error(String msg, Integer code) {
        Result result = new Result();
        result.msg = msg;
        result.code = code;
        return result;
    }

}
