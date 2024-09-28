package com.ecode.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Docker属性
 *
 * @author 竹林听雨
 * @date 2024/09/28
 */
@Component
@ConfigurationProperties(prefix = "ecode.docker")
@Data
public class DockerProperties {
    //docker远程链接
    private String url;
    //代码运行超时
    private int timeout;
}
