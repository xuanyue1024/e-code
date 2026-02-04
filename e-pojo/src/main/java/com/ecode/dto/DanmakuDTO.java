package com.ecode.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "弹幕内容不能为空")
    private String msg;

    @Schema(description = "弹幕颜色")
    private String color;
}
