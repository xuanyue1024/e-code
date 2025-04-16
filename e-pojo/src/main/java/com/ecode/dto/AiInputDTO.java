package com.ecode.dto;

import com.ecode.enumeration.AiAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Ai输入DTO
 *
 * @author 竹林听雨
 * @date 2024/12/27
 */
@Data
public class AiInputDTO implements Serializable {

    private static final long serialVersionUID = 6099069246684387027L;

    @Schema(description = "身份令牌")
    private String token;

    @Schema(description = "动作")
    private AiAction aiAction;

    @Schema(description = "内容")
    private String content;
}
