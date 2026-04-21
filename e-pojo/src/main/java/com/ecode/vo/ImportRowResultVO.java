package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Excel 导入单行处理结果。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Excel 导入单行处理结果")
public class ImportRowResultVO implements Serializable {

    private static final long serialVersionUID = -1726692463447850862L;

    @Schema(description = "Excel 行号，从 1 开始")
    private Integer rowNumber;

    @Schema(description = "处理状态：CREATED-新增，SKIPPED-跳过，FAILED-失败")
    private String status;

    @Schema(description = "处理说明")
    private String message;
}
