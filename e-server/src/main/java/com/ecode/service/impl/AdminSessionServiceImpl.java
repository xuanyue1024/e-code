package com.ecode.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.ecode.constant.JwtClaimsConstant;
import com.ecode.constant.MessageConstant;
import com.ecode.exception.BaseException;
import com.ecode.service.AdminSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 管理员会话管理服务实现。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Service
@RequiredArgsConstructor
public class AdminSessionServiceImpl implements AdminSessionService {

    private static final String FORCE_LOGOUT_USER_KEY = "ecode:admin:force-logout:user:";
    private static final String BLACK_TOKEN_KEY = "ecode:admin:black-token:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${sa-token.timeout:2592000}")
    private long tokenTimeout;

    @Override
    public void logoutUser(Integer userId) {
        setWithTokenTimeout(FORCE_LOGOUT_USER_KEY + userId, System.currentTimeMillis());
    }

    @Override
    public void logoutToken(String token) {
        setWithTokenTimeout(BLACK_TOKEN_KEY + token, Boolean.TRUE);
    }

    @Override
    public void checkCurrentToken() {
        String token = StpUtil.getTokenValue();
        if (!StringUtils.hasText(token)) {
            return;
        }
        if (redisTemplate.hasKey(BLACK_TOKEN_KEY + token)) {
            throw new BaseException(MessageConstant.TOKEN_FAILURE);
        }

        Integer loginId = StpUtil.getLoginIdAsInt();
        Object forceLogoutTime = redisTemplate.opsForValue().get(FORCE_LOGOUT_USER_KEY + loginId);
        if (forceLogoutTime == null) {
            return;
        }

        long loginTime = parseLong(StpUtil.getExtra(JwtClaimsConstant.LOGIN_TIME));
        long forceTime = parseLong(forceLogoutTime);
        if (loginTime <= forceTime) {
            throw new BaseException(MessageConstant.TOKEN_FAILURE);
        }
    }

    private void setWithTokenTimeout(String key, Object value) {
        if (tokenTimeout > 0) {
            redisTemplate.opsForValue().set(key, value, tokenTimeout, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    private long parseLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }
}
