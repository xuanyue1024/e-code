package com.ecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="ProblemUpdateDTO对象", description="")
public class ProblemUpdateDTO extends ProblemAddDTO{
    private static final long serialVersionUID = -5419034326902870843L;

    @ApiModelProperty(value = "题目id")
    private Integer id;

}
