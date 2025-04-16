package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "班级增加题目DTO")
public class ClassProblemDTO implements Serializable {
    private static final long serialVersionUID = -5160651629889698567L;

    @Schema(description = "班级id")
    private Integer classId;

    @Schema(description = "题目id集合")
    private List<Integer> problemIds;
}
