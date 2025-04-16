package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "代码调试提交数据模型")
public class DebugCodeDTO implements Serializable {

    private static final long serialVersionUID = 4619556128447460185L;

    @Schema(description = "代码文本")
    private String code;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "输入内容")
    private String input;
}
