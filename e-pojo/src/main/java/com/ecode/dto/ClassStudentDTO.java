package com.ecode.dto;

import com.ecode.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "ClassStudentDTO")
public class ClassStudentDTO extends PageQuery {
    private static final long serialVersionUID = 7325559691274388382L;

    @Schema(description = "学生姓名")
    private String name;

    @Schema(description = "班级id")
    private Integer classId;
}
