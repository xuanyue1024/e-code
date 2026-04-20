package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "代码评估输入参数")
public class EvaluateAnswerDTO implements Serializable {
    @NotNull(message = "题目ID不能为空")
    @Schema(description = "题目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer problemId;

    @NotBlank(message = "代码内容不能为空")
    @Schema(description = "学生作答的代码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
}
