package com.ecode.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI接口属性
 *
 * @author 竹林听雨
 * @date 2024/12/27
 */
@Component
@ConfigurationProperties(prefix = "ecode.ai")
@Data
public class AIProperties {
    /**
     *题目推荐链接URL
     */
    private String problemRecommendationUrl;
}
