package com.ecode.utils;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.InputStream;
import java.net.URI;

@Data
@Slf4j
@Builder
public class S3Util {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private S3Client s3Client;

    // 自定义 builder，确保 s3Client 在 build 时被正确初始化
    public static class S3UtilBuilder {
        public S3Util build() {
            // 确保必要字段非空（可选）
            if (endpoint == null || accessKey == null || secretKey == null || bucketName == null) {
                throw new IllegalStateException("Missing required fields for S3Util");
            }

            S3Client client = S3Client.builder()
                    .endpointOverride(URI.create(endpoint)) // RustFS 地址
                    .region(Region.US_EAST_1) // 可写死，RustFS 不校验 region
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(accessKey, secretKey)
                            )
                    )
                    .forcePathStyle(true) // 关键配置！RustFS 需启用 Path-Style
                    .build();

            return new S3Util(endpoint, accessKey, secretKey, bucketName, client);
        }
    }
    private S3Util(String endpoint, String accessKey, String secretKey, String bucketName, S3Client s3Client) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.s3Client = s3Client;
    }

    /**
     * 文件上传
     *
     * @param inputStream 流式上传
     * @param objectName 对象的完整键，如user/avatars/12345.jpg（目录层级+文件名)
     * @return
     */
    public boolean upload(InputStream inputStream, long contentLength, String contentType, String objectName) {
        try {
            // 3. 上传文件
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectName)
                            .contentLength(contentLength)
                            .contentType(contentType)
                            .build(),
                    RequestBody.fromInputStream(inputStream, contentLength)
            );
        } catch (S3Exception e) {
            log.error("S3上传失败", e);
            return false;
        }
        return true;
    }

    /**
     * 文件删除
     *
     * @param objectName
     */
    public boolean delete(String objectName) {
        try {
            // 6. 删除对象
            s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build());
            log.info("文件删除成功:{}", objectName);
        } catch (S3Exception e) {
            log.error("S3删除失败", e);
            return false;
        }
        return true;
    }

    /**
     * 获取公开文件完整链接
     *
     * @param objectName 文件名或完整URL
     * @return url
     */
    public String getPublicUrl(String objectName) {
        // 如果 objectName 本身就是完整 URL，直接返回
        if (StringUtils.isNotBlank(objectName) &&
                (objectName.startsWith("http://") || objectName.startsWith("https://"))) {
            return objectName;
        }

        // 否则按规则拼接
        return String.join("/", endpoint, bucketName, objectName);
    }
}
