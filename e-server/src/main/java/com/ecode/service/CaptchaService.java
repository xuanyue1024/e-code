package com.ecode.service;

/**
 * 验证码服务
 *
 * @author 竹林听雨
 * @date 2024/09/25
 */
public interface CaptchaService {

    /**
     * 发送验证码
     *
     * @param email 电子邮件
     * @return boolean
     */
    void sendCaptcha(String email);
}
