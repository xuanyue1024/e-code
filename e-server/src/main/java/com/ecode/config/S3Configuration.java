package com.ecode.config;


import com.ecode.properties.S3Properties;
import com.ecode.utils.S3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建S3Util对象
 */
@Configuration
@Slf4j
public class S3Configuration {

    @Bean
    @ConditionalOnMissingBean
    public S3Util S3Util(S3Properties s3Properties){
        log.info("开始创建S3文件上传工具类对象：{}",s3Properties.getEndpoint());
        return S3Util.builder()
                .endpoint(s3Properties.getEndpoint())
                .accessKey(s3Properties.getAccessKey())
                .secretKey(s3Properties.getSecretKey())
                .bucketName(s3Properties.getBucketName())
                .build();
    }
}