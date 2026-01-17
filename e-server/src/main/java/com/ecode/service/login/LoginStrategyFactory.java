package com.ecode.service.login;

import com.ecode.constant.MessageConstant;
import com.ecode.exception.BaseException;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 登录策略工厂
 *
 * @author 竹林听雨
 * @date 2024/09/22
 */
@Component
public class LoginStrategyFactory {

    private final Map<String, LoginStrategy> strategyMap;
    private final HttpMessageConverters messageConverters;

    // 通过构造方法注入策略
    public LoginStrategyFactory(Map<String, LoginStrategy> strategyMap, HttpMessageConverters messageConverters) {
        this.strategyMap = strategyMap;
        this.messageConverters = messageConverters;
    }

    /**
     * 获取具体的登录策略
     * @param loginType 登录类型
     * @return LoginStrategy 登录策略
     */
    public LoginStrategy getStrategy(String loginType) {
        LoginStrategy strategy = strategyMap.get(loginType);
        if (strategy == null) {
            throw new BaseException(MessageConstant.INVALID_LOGIN_TYPE + loginType);
        }
        return strategy;
    }
}
