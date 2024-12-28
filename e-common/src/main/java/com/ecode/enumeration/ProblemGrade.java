package com.ecode.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
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
    GENERAL(1, "一般"),
    DIFFICULT(2, "困难");

    @EnumValue
    private final int value;
    private final String desc;

}
