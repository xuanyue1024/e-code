package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 管理员维护班级学生请求参数。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Schema(description = "管理员维护班级学生请求参数")
public class AdminClassStudentDTO implements Serializable {

    private static final long serialVersionUID = 320336722547211277L;

    @NotNull(message = "班级id不能为空")
    @Schema(description = "班级id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer classId;

    @NotEmpty(message = "学生id集合不能为空")
    @Schema(description = "学生id集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Integer> studentIds;
}
