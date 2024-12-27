package com.ecode.enumeration;

import lombok.Getter;

/**
 * Ai动作
 *
 * @author 竹林听雨
 * @date 2024/12/27
 */
@Getter
public enum AiAction {
    NEW("new", "创建新对话"),
    NEXT("next", "继续原对话,没有记录则创建新对话");

    private final String value;
    private final String desc;

    AiAction(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

