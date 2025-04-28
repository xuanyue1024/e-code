package com.ecode.dto;

import com.ecode.enumeration.AiType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Ai输入DTO
 *
 * @author 竹林听雨
 * @date 2024/12/27
 */
@Data
public class AiInputDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6099069246684387027L;


    @Schema(description = "聊天id")
    @NotNull(message = "聊天id不能为空")
    private String chatId;

    @Schema(description = "对话内容")
    @NotNull(message = "对话内容不能为空")
    private String prompt;

    @Schema(description = "对话类型: CHAT-普通聊天, CODE-代码助手")
    @NotNull(message = "对话类型不能为空")
    private AiType type;

    @Schema(description = "是否深度思考", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean isThinking;

    @Schema(description = "是否联网搜索", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean isSearch;

    @Schema(description = "解答题目页面题目id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer problemId;
}
