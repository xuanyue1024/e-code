package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "题目详细信息展示")
public class RunCodeVO implements Serializable {

    private static final long serialVersionUID = -1954758120289998378L;

    @Schema(description = "测试样例通过数(总的4个,=4个满分,一个1分)")
    private Integer passCount;

    @Schema(description = "得分")
    private Integer score;

    @Schema(description = "统一差异格式unifiedDiff列表(4个)")
    private List<String> diff;
}
