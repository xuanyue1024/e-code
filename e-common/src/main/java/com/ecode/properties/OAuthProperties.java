package com.ecode.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "ecode.oauth")
public class OAuthProperties {

    /**
     * 这里使用 Map 存储，Key 就是平台名（如 github, gitee）
     * Value 则是具体的配置项
     */
    private Map<String, AuthSourceConfig> configs;

    /**
     * 回调URL
     */
    private String redirectUri;

    /**
     * 单个认证平台的配置项
     * 包含授权地址、获取 access token、获取用户信息等相关地址和凭证
     */
    @Data
    public static class AuthSourceConfig {
        /**
         * 授权地址，用于跳转到第三方授权页面
         */
        private String authorizeUrl;

        /**
         * 用于通过授权码换取 access token 的接口地址
         */
        private String accessTokenUrl;

        /**
         * 获取用户信息的接口地址（通常返回用户基本信息）
         */
        private String userInfoUrl;

        /**
         * 获取用户邮箱的接口地址。
         * 部分平台（如 GitHub）需要单独请求邮箱接口，若为空则尝试从 userInfoUrl 的返回中获取邮箱
         */
        private String emailUrl;

        /**
         * 第三方应用的客户端 ID
         */
        private String clientId;

        /**
         * 第三方应用的客户端密钥，注意不要在日志或前端泄露
         */
        private String clientSecret;
    }
}