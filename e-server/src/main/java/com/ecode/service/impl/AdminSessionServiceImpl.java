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
        // 使用 Sa-Token 原生能力注销账号维度会话，避免维护额外 token 黑名单状态。
        StpUtil.logout(userId);
    }

    @Override
    public void logoutToken(String token) {
        // 精确注销单个 token，适用于管理员只踢出某个设备而保留同账号其他登录态。
        StpUtil.logoutByTokenValue(token);
    }
}
