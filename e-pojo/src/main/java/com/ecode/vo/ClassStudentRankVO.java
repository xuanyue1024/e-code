package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "班级学生成绩排名VO")
public class ClassStudentRankVO implements Serializable {

    @Schema(description = "学生ID")
    private Integer studentId;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "尝试题目数量")
    private Integer attemptedProblems;

    @Schema(description = "总提交次数")
    private Integer totalSubmissions;

    @Schema(description = "通过提交次数")
    private Integer passedSubmissions;

    @Schema(description = "平均分数")
    private Double avgScore;

    @Schema(description = "通过率百分比")
    private Double passRatePercentage;

    @Schema(description = "最后使用语言")
    private String lastUsedLanguage;
}