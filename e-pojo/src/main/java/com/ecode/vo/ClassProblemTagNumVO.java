package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassProblemTagNumVO类，用于表示问题难度数值视图对象。
 * 班内题目难度分布
 *
 * @author 竹林听雨
 * @date 2025/04/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "班内各标签题目数量饼图")
public class ClassProblemTagNumVO {
    @Schema(description = "标签名称")
    private String tagName;

    @Schema(description = "题目数量数量")
    private Integer problemCount;
}
