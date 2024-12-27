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
    //阿里云百炼应用id
    private String bailianId;
    //阿里云百炼API KEY
    private String bailianApiKey;
}
