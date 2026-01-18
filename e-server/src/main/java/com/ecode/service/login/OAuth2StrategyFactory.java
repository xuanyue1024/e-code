package com.ecode.service.login;

import com.ecode.constant.MessageConstant;
import com.ecode.exception.BaseException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 登录策略工厂
 *
 * @author 竹林听雨
 * @date 2024/09/22
 */
@Component
public class OAuth2StrategyFactory {

    private final Map<String, OAuth2Strategy> strategyMap;

    // 通过构造方法注入策略
    public OAuth2StrategyFactory(Map<String, OAuth2Strategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * 获取具体的登录策略
     * @param loginType 登录类型
     * @return LoginStrategy 登录策略
     */
    public OAuth2Strategy getStrategy(String oAuth2Type) {
        OAuth2Strategy strategy = strategyMap.get(oAuth2Type);
        if (strategy == null) {
            throw new BaseException(MessageConstant.INVALID_LOGIN_TYPE + oAuth2Type);
        }
        return strategy;
    }
}
