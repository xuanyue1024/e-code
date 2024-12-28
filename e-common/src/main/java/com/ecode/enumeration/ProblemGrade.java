package com.ecode.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 题目难度枚举
 *
 * @author 竹林听雨
 * @date 2024/12/28
 */
@Getter
@AllArgsConstructor
public enum ProblemGrade {
    EASY(0, "简单"),
    GENERAL(1, "困难"),
    DIFFICULT(3, "一般");

    @EnumValue
    private final int value;
    @JsonValue
    @ApiModelProperty("难度")
    private final String desc;

}
