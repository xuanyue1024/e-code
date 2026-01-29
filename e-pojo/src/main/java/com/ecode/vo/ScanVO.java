package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 二维码生成VO
 */
@Data
@Builder
@Schema(description = "二维码生成VO")
public class ScanVO {

    @Schema(description = "登录id")
    private String sceneId;

    @Schema(description = "超时时间")
    private Long timeout;

}
