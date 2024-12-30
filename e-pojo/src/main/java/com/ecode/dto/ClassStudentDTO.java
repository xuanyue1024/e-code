package com.ecode.dto;

import com.ecode.query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClassStudentDTO extends PageQuery {
    private static final long serialVersionUID = 7325559691274388382L;

    @ApiModelProperty("学生姓名")
    private String name;

    @ApiModelProperty("班级id")
    private Integer classId;
}
