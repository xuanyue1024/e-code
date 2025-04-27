package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 问题详细内容VO,编辑前查询信息
 *
 * @author 竹林听雨
 * @date 2025/4/27
 */
@Data
@Schema(description = "题目详细信息展示")
public class ProblemEditVO extends ProblemVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8620456099506182450L;


    @Schema(description = "测试输入1")
    private String inputTest1;

    @Schema(description = "测试输出1")
    private String outputTest1;

    @Schema(description = "测试输入2")
    private String inputTest2;

    @Schema(description = "测试输出2")
    private String outputTest2;

    @Schema(description = "测试输入3")
    private String inputTest3;

    @Schema(description = "测试输出3")
    private String outputTest3;

    @Schema(description = "测试输入4")
    private String inputTest4;

    @Schema(description = "测试输出4")
    private String outputTest4;

}
