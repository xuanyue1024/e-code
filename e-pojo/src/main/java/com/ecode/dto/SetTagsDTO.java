package com.ecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="设置标签集合DTO", description="")
public class SetTagsDTO {
    @ApiModelProperty(value = "题目id")
    private Integer problemId;

    @ApiModelProperty(value = "标签id集合")
    private List<Integer> tagIds;
}
