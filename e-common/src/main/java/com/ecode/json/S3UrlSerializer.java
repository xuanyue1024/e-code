package com.ecode.json;

import com.ecode.utils.S3Util;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class S3UrlSerializer extends JsonSerializer<String> {

    // 静态持有（由 Spring 注入一次）
    private static S3Util s3Util;

    // 👇 必须提供无参构造函数（Jackson 要求）
    public S3UrlSerializer() {}

    // Spring 会在启动时调用这个方法注入 bean
    @Autowired
    public void setS3Util(S3Util s3Util) {
        S3UrlSerializer.s3Util = s3Util;
    }

    @Override
    public void serialize(String objectName, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (objectName == null) {
            gen.writeNull();
            return;
        }
        if (s3Util == null) {
            // 安全兜底：避免 NPE（开发阶段可抛异常）
            gen.writeString(objectName);
            return;
        }
        gen.writeString(s3Util.getPublicUrl(objectName));
    }
}