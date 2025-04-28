package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassProblemDifficultyNumVO类，用于表示问题难度数值视图对象。
 * 班内题目难度分布
 *
 * @author 竹林听雨
 * @date 2025/04/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "班内题目难度分布饼图")
public class ClassProblemDifficultyNumVO {
    @Schema(description = "难度(简单,一般,困难)")
    private String difficulty;

    @Schema(description = "题目数量")
    private Integer problemNum;
}
