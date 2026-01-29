package com.ecode.vo;


import com.ecode.enumeration.ScanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扫码登录ws数据返回
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-01-27  00:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "扫码登录ws数据返回")
public class WsScanVO {
    @Schema(description = "状态")
    private ScanStatus status;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "消息")
    private String msg;

    @Schema(description = "元数据")
    private Object metaData;
}
