package com.ecode.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI类型枚举类：定义了不同的AI类型
 *
 * @author 竹林听雨
 * @date 2025/04/20
 */
@Getter
@AllArgsConstructor
public enum AiType {
    /**
     * 普通聊天
     */
    CHAT(0, "chat"),
    /**
     * 代码助手
     */
    CODE(1, "code");

    private final int value;
    @EnumValue
    private final String desc;

}
