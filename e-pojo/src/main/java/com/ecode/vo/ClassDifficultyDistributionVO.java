package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "班级学生完成不同难度题目的分布-雷达图/堆叠柱状图")
public class ClassDifficultyDistributionVO implements Serializable {

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "简单题目通过数量")
    private Integer easyPassed;

    @Schema(description = "中等题目通过数量")
    private Integer mediumPassed;

    @Schema(description = "困难题目通过数量")
    private Integer hardPassed;

    @Schema(description = "简单题目总数")
    private Integer easyTotal;

    @Schema(description = "中等题目总数")
    private Integer mediumTotal;

    @Schema(description = "困难题目总数")
    private Integer hardTotal;
}