package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "代码评估结果参数")
public class EvaluateResultVO implements Serializable {
    @Schema(description = "打分(0-5)")
    private Integer score;

    @Schema(description = "点评内容(Markdown格式)")
    private String comment;
}
