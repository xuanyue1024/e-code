package com.ecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "班级增加题目DTO")
public class ClassProblemDTO implements Serializable {
    private static final long serialVersionUID = -5160651629889698567L;

    @ApiModelProperty(value = "班级id")
    private Integer classId;

    @ApiModelProperty(value = "题目id集合")
    private List<Integer> problemIds;
}
