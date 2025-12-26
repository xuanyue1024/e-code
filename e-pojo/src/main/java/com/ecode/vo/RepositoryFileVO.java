package com.ecode.vo;

import com.ecode.json.S3UrlSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryFileVO {
    // 文件名称
    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件链接")
    @JsonSerialize(using = S3UrlSerializer.class)
    private String url;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
