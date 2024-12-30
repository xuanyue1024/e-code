package com.ecode.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClassProblemPageQueryDTO extends GeneralPageQueryDTO{
    private static final long serialVersionUID = -7888334063203599574L;

    @ApiModelProperty(value = "班级id", required = true)
    private Integer classId;
}
