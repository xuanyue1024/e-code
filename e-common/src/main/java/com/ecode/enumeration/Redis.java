package com.ecode.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public enum Redis {
    // OAuth2注册临时code
    OAUTH2_REGISTER_CODE("oauth2:registerCode:", 10L, TimeUnit.MINUTES),

    //OAuth2存储state
    OAUTH2_STATE("oauth2:state:", 10L, TimeUnit.MINUTES);
    private final String prefix;
    private final Long timeout;
    private final TimeUnit timeUnit;

    @Override
    public String toString() {
        return this.prefix;
    }
}
