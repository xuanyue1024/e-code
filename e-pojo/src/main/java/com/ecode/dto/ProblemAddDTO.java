package com.ecode.dto;

import com.ecode.enumeration.ProblemGrade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 题目增加DTO
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
@Data
@Schema(description = "ProblemAddDTO对象")
public class ProblemAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "题目内容（md格式）")
    private String content;

    @Schema(description = "要求，为空按默认值（md格式）")
    private String require;

    @Schema(description = "标签组id")
    private Integer problemTagId;

    @Schema(description = "题目等级（0简单，1一般，2困难）")
    private ProblemGrade grade;

    @Schema(description = "最大运行内存（MB)默认512")
    private String maxMemory;

    @Schema(description = "最大运行时间（s）默认5")
    private Integer maxTime;

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
