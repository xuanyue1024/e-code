package com.ecode.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 弹幕DTO
 *{@link com.ecode.json.DanmakuMessage}
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-04  00:29
 */
@Data
@Schema(description = "弹幕DTO")
public class DanmakuDTO {

    @Schema(description = "班级ID")
    @NotNull(message = "班级ID不能为空")
    private Integer classId;

    @Schema(description = "弹幕内容")
    @Size(min = 1, max = 20, message ="弹幕内容必须在1到20个字符之间")
    private String msg;

    @Schema(description = "弹幕颜色")
    private String color;

    @Schema(description = "弹幕大小")
    @Min(value = 10, message = "弹幕大小必须大于10")
    @Max(value = 60, message = "弹幕大小必须小于等于60")
    private Integer size;
}
