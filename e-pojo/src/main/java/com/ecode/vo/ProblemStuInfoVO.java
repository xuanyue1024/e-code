package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="指定班级题目学生作答情况")
public class ProblemStuInfoVO {
    @Schema(description = "作答次数")
    private Integer submitNumber;

    @Schema(description = "得分")
    private Integer score;

    @Schema(description = "通过次数")
    private Integer passNumber;
}
