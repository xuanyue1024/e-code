package com.ecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "代码运行提交数据模型")
public class RunCodeDTO implements Serializable {

    private static final long serialVersionUID = 4619556128447460185L;

    @ApiModelProperty("代码文本")
    private String code;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("输入内容")
    private String input;
}
