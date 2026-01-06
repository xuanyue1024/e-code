package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI消息ID视图对象类：用于封装AI消息相关ID信息
 *
 * @author 竹林听雨
 * @date 2025/04/24
 */
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Schema(description = "AI消息VO")
public class AiChatIdsVO {

    @Schema(description = "聊天ID")
    private String chgatId;

    @Schema(description = "会话标题")
    private String title;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
