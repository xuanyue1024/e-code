package com.ecode.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    PLEASE_REGISTER(6001, "第三方登录失败,尚未注册,请先注册"),
    LIVE_NO_START(400, "直播未开始");
    private final int value;
    private final String desc;
}
