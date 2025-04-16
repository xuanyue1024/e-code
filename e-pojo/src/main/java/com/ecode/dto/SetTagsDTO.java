package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description="设置标签集合DTO")
public class SetTagsDTO {
    @Schema(description = "题目id")
    private Integer problemId;

    @Schema(description = "标签id集合")
    private List<Integer> tagIds;
}
