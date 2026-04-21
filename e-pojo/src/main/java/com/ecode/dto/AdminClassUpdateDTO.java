package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员更新班级请求参数。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Schema(description = "管理员更新班级请求参数")
public class AdminClassUpdateDTO implements Serializable {

    private static final long serialVersionUID = -2866925164781187232L;

    @NotNull(message = "班级id不能为空")
    @Schema(description = "班级id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @NotNull(message = "教师id不能为空")
    @Schema(description = "教师id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teacherId;

    @NotBlank(message = "班级名称不能为空")
    @Schema(description = "班级名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
