package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

/**
 * 消息视图对象类：用于封装AI消息数据
 *
 * @author 竹林听雨
 * @date 2025/04/23
 */
@NoArgsConstructor
@Data
@Schema(description = "AI消息VO")
public class AiMessageVO {
    @Schema(description = "角色(user,assistant)")
    private String role;
    @Schema(description = "消息内容")
    private String content;

    public AiMessageVO(Message message) {
        this.role = switch (message.getMessageType()) {
            case USER -> "user";
            case ASSISTANT -> "assistant";
            case SYSTEM -> "system";
            default -> "";
        };
        this.content = message.getText();
    }
}