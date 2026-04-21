package com.ecode.service;

/**
 * 管理员会话管理服务。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
public interface AdminSessionService {

    /**
     * 下线指定用户的全部已签发 token。
     *
     * @param userId 用户id
     */
    void logoutUser(Integer userId);

    /**
     * 下线指定 token。
     *
     * @param token JWT token
     */
    void logoutToken(String token);
}
