package com.ecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="指定班级题目学生作答情况", description="")
public class ProblemStuInfoVO {
    @ApiModelProperty("作答次数")
    private Integer submitNumber;

    @ApiModelProperty("得分")
    private Integer score;

    @ApiModelProperty("通过次数")
    private Integer passNumber;
}
