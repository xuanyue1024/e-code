package com.ecode.json;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * 弹幕消息<br>
 * {@link com.ecode.dto.DanmakuDTO}
 */
public class DanmakuMessage {

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "弹幕内容")
    private String msg;

    @Schema(description = "弹幕颜色")
    private String color = "#ffffff";

    @Schema(description = "时间戳")
    private Long ts;

    @Schema(description = "角色(主播-0)")
    private Integer role;
}