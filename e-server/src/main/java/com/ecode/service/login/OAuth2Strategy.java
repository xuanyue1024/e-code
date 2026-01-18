package com.ecode.service.login;

import com.ecode.dto.UserLoginDTO;
import com.ecode.vo.UserOauthVO;

public interface OAuth2Strategy {

    /**
     * 准备阶段：获取跳转地址 (仅 OAuth2 需要)
     *
     */
    String prepare();
    /**
     * 登录方法
     * @param userLoginDTO 登录请求参数
     * @return 登录结果
     */
    UserOauthVO callback(UserLoginDTO userLoginDTO);
}
