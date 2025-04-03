package com.ecode.service.login;

import com.ecode.dto.UserLoginDTO;
import com.ecode.entity.User;
import com.yubico.webauthn.exception.AssertionFailedException;

import java.io.IOException;

public interface LoginStrategy {
    /**
     * 登录方法
     * @param userLoginDTO 登录请求参数
     * @return 登录结果
     */
    User login(UserLoginDTO userLoginDTO) throws IOException, AssertionFailedException;
}
