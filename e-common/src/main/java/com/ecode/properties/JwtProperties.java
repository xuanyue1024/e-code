package com.ecode.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt属性
 *
 * @author 竹林听雨
 * @date 2024/09/22
 */
@Component
@ConfigurationProperties(prefix = "ecode.jwt")
@Data
public class JwtProperties {

    /**
     * 用户登录生成jwt令牌相关配置
     */

    //设置jwt签名加密时使用的秘钥
    private String userSecretKey;
    //设置jwt过期时间
    private long userTtl;
    //设置前端传递过来的令牌名称
    private String userTokenName;


}
