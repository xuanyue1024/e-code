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

    @Data
    public static class AuthSourceConfig {
        private String authorizeUrl;
        private String accessTokenUrl;
        private String userInfoUrl;

        //针对一些平台如github邮箱需要单独获取,为空则从userInfoUrl获取
        private String emailUrl;

        private String clientId;
        private String clientSecret;
        private String redirectUri;
        
        // 如果某些平台有特殊参数（比如微软的 tenantId），也可以加在这里
        private String tenantId; 
    }
}