package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ClassProblemPageQueryDTO extends GeneralPageQueryDTO{
    private static final long serialVersionUID = -7888334063203599574L;

    @Schema(description = "班级id", required = true)
    private Integer classId;
}
