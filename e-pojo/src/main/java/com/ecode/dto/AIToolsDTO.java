package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIToolsDTO implements Serializable {

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "班级ID")
    private Integer classId;

    @Schema(description = "班级名称")
    private String className;

    @Schema(description = "标签ID")
    private Integer tagId;

    @Schema(description = "标签名称")
    private String tagName;

    @Schema(description = "问题ID")
    private Integer problemId;

    @Schema(description = "问题标题")
    private String problemTitle;

    @Schema(description = "问题描述")
    private Integer classScore;
}
