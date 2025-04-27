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
@Schema(description = "班级题目通过率VO")
public class ClassProblemPassRateVO implements Serializable {

    @Schema(description = "题目标题")
    private String problemTitle;

    @Schema(description = "困难程度")
    private Integer difficulty;

    @Schema(description = "通过学生数")
    private Integer attemptedStudents;

    @Schema(description = "通过率")
    private Double passRate;

    @Schema(description = "通过提交次数")
    private Integer totalSubmissions;
}