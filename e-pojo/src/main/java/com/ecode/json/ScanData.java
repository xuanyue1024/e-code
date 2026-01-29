package com.ecode.json;

import com.ecode.enumeration.ScanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Redis存储-扫码数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScanData {

    @Schema(description = "扫码状态")
    private ScanStatus status;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "IP地址")
    private String ip;
}
