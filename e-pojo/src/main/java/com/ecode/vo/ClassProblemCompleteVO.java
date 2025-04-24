package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "班级题目完成饼图")
public class ClassProblemCompleteVO implements Serializable {
    // 班级题目完成统计表VO
    @Schema(description = "班级名称")
    private String className;

    @Schema(description = "总的题目数")
    private Integer totalProblems;

    @Schema(description = "完成的题目数")
    private Integer completedProblems;
}
