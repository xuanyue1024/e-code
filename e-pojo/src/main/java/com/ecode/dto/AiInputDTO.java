package com.ecode.dto;

import com.ecode.enumeration.AiAction;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("身份令牌")
    private String token;

    @ApiModelProperty("动作")
    private AiAction aiAction;

    @ApiModelProperty("内容")
    private String content;
}
