package com.ecode.service.login;

import com.ecode.dto.UserLoginDTO;
import com.ecode.entity.User;
import com.yubico.webauthn.exception.AssertionFailedException;

import java.io.IOException;

public interface LoginStrategy {

    /**
     * 准备阶段：获取跳转地址 (仅 OAuth2 需要)
     * 对于密码和短信登录，返回空
     */
    default String prepare() {
        return null;
    }
    /**
     * 登录方法
     * @param userLoginDTO 登录请求参数
     * @return 登录结果
     */
    User login(UserLoginDTO userLoginDTO) throws IOException, AssertionFailedException;
}
