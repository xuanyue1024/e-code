package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "代码运行提交数据模型")
public class RunCodeDTO implements Serializable {

    private static final long serialVersionUID = 4619556128447460185L;

    @Schema(description = "代码文本")
    private String code;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "班级题目id")
    private Integer classProblemId;

    @Schema(description = "题目id")
    private Integer problemId;

    @Schema(description = "班级id")
    private Integer classId;
}