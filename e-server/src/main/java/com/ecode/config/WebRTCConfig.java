package com.ecode.config;


import com.ecode.utils.WebRTCUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebRTC配置类
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-01  17:49
 */
@Configuration
public class WebRTCConfig {

    @Value("${ecode.webrtc.base-url}")
    private String baseUrl;

    @Value("${ecode.webrtc.app-name}")
    private String appName;

    @Bean
    public WebRTCUtil webRTCUtil() {
        return WebRTCUtil.builder()
                .appName(appName)
                .baseUrl(baseUrl)
                .build();
    }
}
