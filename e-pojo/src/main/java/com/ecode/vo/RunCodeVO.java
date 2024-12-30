package com.ecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 运行代码结果vo
 *
 * @author 竹林听雨
 * @date 2024/12/30
 */
@Data
@Builder
@ApiModel("题目详细信息展示")
public class RunCodeVO implements Serializable {

    private static final long serialVersionUID = -1954758120289998378L;

    @ApiModelProperty("测试样例通过数(总的4个,=4个满分,一个1分)")
    private Integer passCount;

    @ApiModelProperty("得分")
    private Integer score;

    @ApiModelProperty("统一差异格式unifiedDiff列表(4个)")
    private List<String> diff;
}
