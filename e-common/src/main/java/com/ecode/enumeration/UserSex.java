package com.ecode.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author 竹林听雨
 * @date 2024/09/24
 */
@Getter
public enum UserSex {
    MALE(0, "男"),
    FEMALE(1, "女");

    @EnumValue
    private final int value;
    private final String desc;

    UserSex(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
