package com.ecode.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 扫码数据DTO
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-01-26  23:10
 */
@Data
@Schema(description = "扫码数据DTO")
public class ScanDTO {

    @NotBlank(message = "二维码数据不能为空")
    private String sceneId;

    @NotNull(message = "是否确认不能为空")
    private Boolean isConfirm;
}
