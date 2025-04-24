package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "学生提交次数与通过率条形图")
public class ClassProblemSubmissionsVO implements Serializable {

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "总的提交次数")
    private String totalSubmissions;

    @Schema(description = "提交通过的次数")
    private String totalPasses;

    @Schema(description = "通过率")
    private Double passRate;
}
