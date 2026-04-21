package com.ecode.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.ecode.service.AdminSessionService;
import org.springframework.stereotype.Service;

/**
 * 管理员会话管理服务实现。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Service
public class AdminSessionServiceImpl implements AdminSessionService {

    @Override
    public void logoutUser(Integer userId) {
        StpUtil.logout(userId);
    }

    @Override
    public void logoutToken(String token) {
        StpUtil.logoutByTokenValue(token);
    }
}
