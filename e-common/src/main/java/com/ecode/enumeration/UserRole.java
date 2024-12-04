package com.ecode.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author 竹林听雨
 * @date 2024/11/23
 */
@Getter
public enum UserRole {
    ADMIN("admin", "管理员"),
    TEACHER("teacher", "老师"),
    STUDENT("student", "学生");


    @EnumValue
    private final String value;
    @JsonValue
    private final String desc;

    UserRole(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UserRole fromDesc(String desc) {
        for (UserRole role : UserRole.values()) {
            if (role.getDesc().equals(desc)) {
                return role;
            }
        }
        throw new IllegalArgumentException("无效的用户角色: " + desc);
    }
}
