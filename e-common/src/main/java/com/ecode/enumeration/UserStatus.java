package com.ecode.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author 竹林听雨
 * @date 2024/09/24
 */
@Getter
public enum UserStatus {
    ENABLE(1,"启用"),
    DISABLE(0, "禁用");

    @EnumValue
    private final int value;

    @JsonValue
    private final String desc;

    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
